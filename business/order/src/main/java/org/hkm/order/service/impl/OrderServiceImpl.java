package org.hkm.order.service.impl;

import org.hkm.order.entity.Order;
import org.hkm.order.entity.OrderGoods;
import org.hkm.order.mapper.OrderGoodsMapper;
import org.hkm.order.mapper.OrderMapper;
import org.hkm.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderGoodsMapper orderGoodsMapper;


    @Override
    public Long order(Long uid, Map<String, Integer> skus) {

        Order order = new Order();

        order.setOrderNo(generateOrderNum());
        order.setUId(uid);
        order.setCreateTime(System.currentTimeMillis());
        order.setUpdateTime(order.getCreateTime());
        order.setDiscountType(Order.DiscountType.NONE);
        order.setState(Order.State.CREATE);


        int amount = 0;
        List<OrderGoods> goodses = new ArrayList<>();
        for (String skuIdStr : skus.keySet()) {
            Long skuId = Long.parseLong(skuIdStr);
            // get sku's price
            // TODO amount += sku's price
            OrderGoods goods = new OrderGoods();
            goods.setCount(skus.get(skuIdStr));
            goods.setSkuId(skuId);
            goodses.add(goods);
        }
        order.setAmount(new BigDecimal(amount));
        if (order.getDiscountType().equals(Order.DiscountType.NONE)) {
            order.setDiscount(new BigDecimal(0));
            order.setValue(order.getAmount());
        }
        int r = orderMapper.insertOrder(order);
        logger.info("{}",r);
        for (OrderGoods goods : goodses) {
            goods.setOrderId(order.getId());
            orderGoodsMapper.insertOrderGoods(goods);
        }


        return order.getId();
    }




    private Long generateOrderNum(){
        return System.currentTimeMillis();
    }



}
