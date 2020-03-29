package org.hkm.product.service.impl;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.hkm.config.RedisKey;
import org.hkm.product.entity.ProductSKU;
import org.hkm.product.mapper.ProductSkuMapper;
import org.hkm.product.service.ProductService;
import org.hkm.product.zk.ZkWatcher;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class ProductSkuServiceImpl implements ProductService {


    private static Logger logger = LoggerFactory.getLogger(ProductSkuServiceImpl.class);

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private RedissonClient redisson;

    @Autowired
    private RedisKey redisKey;

    @Autowired
    private ZooKeeper zk;

    private static Map<Long, Boolean> soldOutLocalCache = new ConcurrentHashMap<>();

    @Override
    public ProductSKU getById(Long id) {
        RMap<String, Object> skuMap = get(id, 0);
        ProductSKU sku = new ProductSKU();
        sku.entity(skuMap);
        return sku;
    }

    @Override
    public RMap<String, Object> getByIdFromCache(Long id) {
        return get(id, 0);
    }

    @Override
    public int getStockById(Long id) {
        RMap<String, Object> sku = getByIdFromCache(id);
        Float stock = parseStock(sku);
        return stock.intValue();
    }

    @Override
    public int reduceStock(Long id, int num) {

        RMap<String, Object> sku = getByIdFromCache(id);

        Float stock = parseStock(sku);

        //sold out
        Boolean soldOut = soldOutLocalCache.get(id);
        if (soldOut == null) {
            soldOutLocalCache.put(id, stock<=0);
            createSoldOutNode(id);
        } else if (soldOut) {
            logger.info("stoped by jvm cache");
            return 0;
        }


        // stock not enough, no need to lock
        if (stock < num) {
            logger.info("stoped by first stock check");
            return 0;
        }

        RLock lock = redisson.getLock(RedisKey.parseValue(redisKey.getProduct().getLock().getStock(), id));
        try {
            boolean locked = lock.tryLock(10, TimeUnit.SECONDS);
            if (!locked) {
                logger.info("stoped by lock");
                return 0;
            }
            // check if stock enough again
            sku = getByIdFromCache(id);
            stock = parseStock(sku);
            if (stock < num) {
                logger.info("stoped by second stock check");
                return 0;
            }

            // decrease stock
            sku.addAndGet("stock", -num);

            logger.info("reduce success ");

            // if left 0 stock -> set jvm cache && notice other instaces to sync jvm cache
            if (stock == num) {
                logger.info("sold out now");
                changeSoldOut(id);
            }

            Map<String, Object> param = new HashMap<>();
            param.put("id", id);
            param.put("now", stock);
            param.put("reduce", num);

            // TODO update db asyn
            productSkuMapper.updateStock(param);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return num;
    }

    private boolean getFromDB(long id, RMap<String, Object> cache) {

        // lock for only one instance can connect to the db
        RLock lock = redisson.getLock(RedisKey.parseValue(redisKey.getProduct().getLock().getSelect(), id));
        try {
            boolean locked = lock.tryLock();
            if (!locked) {
                logger.debug("didn't get the lock, i will wait");
                return false;
            }
            ProductSKU sku = this.productSkuMapper.getById(id);
            logger.debug("put sku[{}] to redis", id);

            // fond data, expire 1h
            if (sku != null) {
                sku.map(cache);
                cache.expire(1, TimeUnit.HOURS);
            } else {//none, expire 10s
                cache.put("id", id);
                cache.expire(10, TimeUnit.SECONDS);
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    private RMap<String, Object> get(long id, int retry) {
        // load from redis
        RMap<String, Object> skuMap = redisson.getMap(RedisKey.parseValue(redisKey.getProduct().getSkumap(), id));
        //load nothing from redis
        if (skuMap == null || skuMap.isEmpty()) {
            // load from db with a lock
            boolean loaded = getFromDB(id, skuMap);

            // retry 3 times
            if (retry >= 3) {
                return skuMap;
            }
            // didn't get the lock, load from redis later
            if (!loaded) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return get(id, retry++);
            }
        } else {// load from redis, expire 1 more hour
            skuMap.expire(1, TimeUnit.HOURS);
            logger.debug("loaded from redis direct");
        }
        return skuMap;
    }

    private Float checkStockEnough(RMap<String, Object> sku, int reduce) {
        Float stock = parseStock(sku);
        int now = stock.intValue();
        if (now < reduce) {
            return -1f;
        }
        return stock;
    }

    private Float parseStock(RMap<String, Object> sku) {
        Object stockObj = sku.get("stock");
        Float stock = Float.parseFloat(stockObj.toString());
        return stock;
    }


    private void changeSoldOut(long id) {
        String zkPath = ZkWatcher.zkSoldOutPrefix + id;
        soldOutLocalCache.put(id, true);
        try {
            this.zk.setData(zkPath, "true".getBytes(), -1);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createSoldOutNode(Long id) {
        String zkPath = ZkWatcher.zkSoldOutPrefix + id;

        try {
            if (this.zk.exists(zkPath, true) == null) {
                this.zk.create(zkPath,"false".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void setSoldOutFlag(Long id, Boolean soldOut) {
        logger.info("{} changed", id);
        soldOutLocalCache.put(id, soldOut);
    }

}
