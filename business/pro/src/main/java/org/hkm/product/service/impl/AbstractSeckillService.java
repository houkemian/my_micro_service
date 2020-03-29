package org.hkm.product.service.impl;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.hkm.product.service.SeckillService;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public abstract class AbstractSeckillService implements SeckillService {




//    abstract RedissonClient getRedissonClient();

    /**
     * 实现扣减库存的后续逻辑
     * @param productId
     * @param count
     * @param leftStock
     */
    abstract void processAfterReduceStock(int productId, int count, int leftStock);

    /**
     * 开始处理任务
     */
    abstract void stockProcess();

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ZooKeeper zk;

    // 库存本地缓存，true为缺货
    private Map<Integer, Boolean> localStockCache = new ConcurrentHashMap<>();

    public static final String zkSoldOutPrefix = "/p_seckill_soldout_state/";


    /**
     * 扣减库存，后续业务传给abstract方法{processAfterReduceStock}处理
     * @param productId
     * @param count
     * @return
     */
    public boolean reduceStock(int productId, int count) {
        boolean ckPrama = isNumberBiggerThanZero(productId, count);
        if (!ckPrama) {
            return ckPrama;
        }

        // 本地缓存查询
        Boolean soldOut = localStockCache.get(productId);
        String zkPath = zkSoldOutPrefix+productId;
        if (soldOut==null) {
            soldOut = false;
            localStockCache.put(productId, soldOut);
            initSoldoutNode(productId);
        }

        if (soldOut) {
            return false;
        }


        RLock lock = redissonClient.getLock("lock:reduce_product:" + productId);
        try {
            boolean locked = lock.tryLock(10000, TimeUnit.MILLISECONDS);
            if (!locked) {
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            RMap productMap = getProductMap(redissonClient, productId);
            if (!checkStockEnough(productMap, count)) {
                return false;
            }

            int leftStock = hincrStock(productMap, -count);
            if (leftStock==0) {
                localStockCache.put(productId, true);
                changeSoldOut(productId);
            }
            if (leftStock<0) {
                hincrStock(productMap, count);
                return false;
            }

            processAfterReduceStock(leftStock, productId, count);

            return true;
        } finally {
            lock.unlock();
        }
    }

    public void reloadStock(){
        Random r = new Random();
        int x = r.nextInt(700)+100;
        redissonClient.getMap("product:1").put("stock", x);
        this.localStockCache.clear();
    }


    static boolean skSwitch = false;

    public void stopStockProcess() {
        skSwitch = false;
    }

    public void startStockProcess() {
        skSwitch = true;
        stockProcess();
    }

    public String getProductKey(int productId) {
        return "product:" + productId;
    }

    public String getStockFieldName() {
        return "stock";
    }

    public RMap getProductMap(RedissonClient redissonClient, int protuctId) {
        return redissonClient.getMap(this.getProductKey(protuctId));
    }

    public float getStock(RedissonClient redissonClient, int productId, RMap productMap) {

        if (redissonClient == null && productMap == null) {
            return -1;
        }

        if (productMap == null) {
            productMap = getProductMap(redissonClient, productId);
        }

        Object stockObj = productMap.get(getStockFieldName());
        if (stockObj == null) {
            return -1;
        }

        return Float.parseFloat(stockObj.toString());

    }

    public int hincrStock(RMap productMap, int count) {
        Object result = productMap.addAndGet(getStockFieldName(), count);
        if (result == null) {
            return -1;
        }
        Float f = Float.parseFloat(result.toString());
        return f.intValue();
    }

    public boolean checkStockEnough(RMap productMap, int count) {
        return getStock(null, 0, productMap) >= count;
    }

    public void setSoldOutFlag(Integer productId, Boolean soldOut) {
        this.localStockCache.put(productId, soldOut);
    }

    private  void initSoldoutNode(int productId) {
        String zkPath = zkSoldOutPrefix+productId;
        try {
            if (this.zk.exists(zkPath,true) == null){
                this.zk.create(zkPath, "false".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void changeSoldOut(int productId) {
        String zkPath = zkSoldOutPrefix+productId;
        localStockCache.put(productId, true);
        try {
            this.zk.setData(zkPath, "true".getBytes(), -1);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
