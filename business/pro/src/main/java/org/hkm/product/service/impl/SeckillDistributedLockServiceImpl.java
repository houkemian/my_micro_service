package org.hkm.product.service.impl;


import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.hkm.product.service.SeckillService;

import java.io.IOException;

public class SeckillDistributedLockServiceImpl implements SeckillService {


    public static void main(String[] args) {
        new SeckillDistributedLockServiceImpl();
    }

    ZooKeeper zk;



    {
        try {
            zk = new ZooKeeper("192.168.3.30:2181", 3000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getType().equals(Event.EventType.NodeDataChanged)){
                        String path = watchedEvent.getPath();
                        System.out.println(path);
                        try {
                            System.out.println(path +" : " + new String(zk.getData("/p_seckill_stock_state", true, new Stat())));
                        } catch (KeeperException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            zk.getData("/p_seckill_stock_state", true, new Stat());

        } catch (IOException | KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
