package org.hkm.order.service.impl;

import org.hkm.common.Result;
import org.hkm.config.RedisKey;
import org.hkm.order.entity.Order;
import org.hkm.order.entity.OrderGoods;
import org.hkm.order.feign.ProductService;
import org.hkm.order.feign.entity.ProductSKU;
import org.hkm.order.mapper.OrderGoodsMapper;
import org.hkm.order.mapper.OrderMapper;
import org.hkm.order.mq.CancleOrderProducer;
import org.hkm.order.mq.CreateOrderProducer;
import org.hkm.order.service.OrderService;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderService {

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private CreateOrderProducer createOrderProducer;

    @Autowired
    private CancleOrderProducer cancleOrderProducer;

    @Autowired
    private RedissonClient redisson;

    @Autowired
    private RedisKey redisKey;


    @Override
    public Result<Order> order(Long uid, Map<String, Integer> skus) {

        Order order = new Order();

        order.setOrderNo(generateOrderNum());
        order.setUId(uid);
        order.setCreateTime(System.currentTimeMillis());
        order.setUpdateTime(order.getCreateTime());
        order.setDiscountType(Order.DiscountType.NONE);
        order.setState(Order.State.CREATE);


        BigDecimal value = new BigDecimal(0);
        List<OrderGoods> goodses = new ArrayList<>();
        for (String skuIdStr : skus.keySet()) {
            Long skuId = Long.parseLong(skuIdStr);

            // get stock first
            Result<Integer> result = productService.reduceStock(skuId, skus.get(skuIdStr));
            if (!result.isSuccess()) {
                return Result.failure();
            }

            OrderGoods goods = new OrderGoods();

            // cal amount, price*num
            Result<ProductSKU> skuR = productService.getProductSku(skuId);
            if (!skuR.isSuccess()) {
                return Result.failure();
            }
            ProductSKU sku = skuR.getData();
            BigDecimal thisPrice = sku.getPrice();
            thisPrice = thisPrice.multiply(new BigDecimal(skus.get(skuIdStr)));
            value = value.add(thisPrice);

            goods.setCount(skus.get(skuIdStr));
            goods.setSkuId(skuId);
            goodses.add(goods);
        }

        calAmount(order, value);

        int r = orderMapper.insertOrder(order);
        logger.info("{}", r);
        for (OrderGoods goods : goodses) {
            goods.setOrderId(order.getId());
            orderGoodsMapper.insertOrderGoods(goods);
        }

        // cache request param, while state-> cancle, easy to revert stock
        cacheOrderCoreData(order.getId(), skus);

        // send create mq
        createOrderProducer.send(order.getId());

        // check payment state later
        paymentTimeout(order.getId());

        return Result.success(order);
    }


    @Override
    public Result<Order> getById(Long orderId) {

        RMap map = redisson.getMap(RedisKey.parseValue(redisKey.getOrder().getOrdermap(), orderId));

        Order o = null;

        if (map.isEmpty()) {
            o = this.orderMapper.findById(orderId);
            if (o == null) {// none, cache 30s
                map.put("id", orderId);
                map.expire(30, TimeUnit.SECONDS);
                return Result.failure();
            }
            o.map(map);
            map.expire(35, TimeUnit.MINUTES);
            return Result.success(o);
        }
        o = new Order();
        o.entity(map);
        return Result.success(o);
    }

    @Override
    public Result<Long> cancle(Long orderId) {

        // update db
        Order order = new Order();
        order.setState(Order.State.CANCLED);
        order.setId(orderId);
        this.orderMapper.updateState(order);

        // send cancle mq
        cancleOrderProducer.send(orderId);

        // clear cache
        redisson.getMap(RedisKey.parseValue(redisKey.getOrder().getOrdermap(), orderId)).delete();

        return Result.success(orderId);
    }

    @Override
    public Result<Order.State> getStateById(Long orderId) {

        Order order = getById(orderId).getData();

        return order == null ? Result.failure() : Result.success(order.getState());
    }


    private Long generateOrderNum() {
        return System.currentTimeMillis();
    }

    private void calAmount(Order order, BigDecimal value) {
        order.setValue(value);
        if (order.getDiscountType().equals(Order.DiscountType.NONE)) {
            order.setDiscount(new BigDecimal(0));
            order.setAmount(value);
        }
    }

    private void cacheOrderCoreData(Long orderId, Map<String, Integer> skuIdAndNum) {
        RMap rmap = redisson.getMap(RedisKey.parseValue(redisKey.getOrder().getSimplify(), orderId));
        rmap.putAll(skuIdAndNum);
        rmap.expire(40, TimeUnit.MINUTES);
    }

    private void paymentTimeout(Long orderId) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Result<Order.State> r = getStateById(orderId);
                if (r.getData() != null) {
                    if (r.getData().equals(Order.State.CREATE)) {
                        cancle(orderId);
                    }
                }
            }
        }, 10000);
    }


}
