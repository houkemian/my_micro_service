package org.hkm.product.service.impl;

import org.redisson.api.RDeque;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class SeckillQueueServiceImpl extends AbstractSeckillService {


    private static ExecutorService stockProcessor = Executors.newCachedThreadPool();

    @Autowired
    private RedissonClient redissonClient;


    @Override
    public void processAfterReduceStock(int productId, int count, int leftStock) {
        RDeque<Integer> queue = redissonClient.getDeque(getQueueKey(productId));
        queue.addFirst(count);
    }

    public void stockProcess() {


        //获取参与秒杀的商品的ID集合
        RList<String> pidRList = redissonClient.getList("seckill:product:ids");

        if (pidRList == null || pidRList.size() < 1) {
            pidRList.add("1");
            pidRList.add("2");
            pidRList.add("3");
        }

        // 线程1用来拉去queue的数据
        stockProcessor.submit(new Runnable() {
            @Override
            public void run() {
                List<String> pidList = pidRList.readAll();
                if (pidList == null || pidList.size() < 1) {
                    System.out.println("没有缓存秒杀商品ID");
                    return;
                }

                // 将id转换为deque的key
                List<String> dequeKeys = pidList.stream().map(s -> getQueueKey(s)).collect(Collectors.toList());
                Map<String, String> map = pidList.stream().collect(Collectors.toMap(k -> getQueueKey(k), v -> v));

                while (skSwitch) {
                    for (String queueKey : map.keySet()) {
                        // 拉取queue数据
                        RDeque<Integer> deque = redissonClient.getDeque(queueKey);
                        if (deque.size() < 1) {
                            continue;
                        }
                        List<Integer> data = deque.pollLast(1);

                        // 处理库存任务，创建订单
                        stockProcessor.submit(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("处理任务，数据：" + data.get(0));
                            }
                        });
                    }
                }
            }
        });
    }


    private static String getQueueKey(int productId) {
        return "reduce_stock:queue:" + productId;
    }

    private static String getQueueKey(String productId) {
        return "reduce_stock:queue:" + productId;
    }


}
