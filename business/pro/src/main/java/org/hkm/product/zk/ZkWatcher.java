package org.hkm.product.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.hkm.product.service.ProductService;
import org.hkm.product.service.impl.AbstractSeckillService;
import org.hkm.product.service.impl.ProductSkuServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ZkWatcher implements Watcher {

    private ZooKeeper zk;

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public static final String zkSoldOutPrefix = "/p_seckill_soldout_state/";

    @Override
    public void process(WatchedEvent watchedEvent) {

        if (watchedEvent.getType().equals(Event.EventType.NodeDataChanged)) {
            String path = watchedEvent.getPath();
            if (path.startsWith(zkSoldOutPrefix)) {
                if (zk == null) {
                    return;
                }
                try {
                    String data = new String(zk.getData(path, true, new Stat()));
                    String productId = path.substring(path.lastIndexOf("/") + 1);
                    Long pid = Long.parseLong(productId);
                    Boolean soldOut = Boolean.parseBoolean(data);
                    ProductSkuServiceImpl.setSoldOutFlag(pid, soldOut);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }




}
