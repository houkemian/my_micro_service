package org.hkm.product.mq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.hkm.config.RedisKey;
import org.hkm.config.RocketMQProperties;
import org.hkm.product.service.ProductService;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CancleOrderConsumer {

    private Logger logger = LoggerFactory.getLogger(CancleOrderConsumer.class);

    @Autowired
    private RocketMQProperties prop;

    @Autowired
    private RedisKey redisKey;

    @Autowired
    private RedissonClient redisson;

    @Autowired
    private ProductService productService;


    public void start(){
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("cancleOrderConsumer");
        consumer.setNamesrvAddr(prop.getNameSrvAddr());
        try {
            consumer.subscribe(prop.getTopic().getOrder().getCancle(),"*");
        } catch (MQClientException e) {
            e.printStackTrace();
        }

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

                for (MessageExt message : list) {
                    String msg = new String(message.getBody());
                    Long orderId;
                    try {
                        orderId = Long.parseLong(new String(msg));
                    } catch (NumberFormatException e) {
                        logger.info(e.getMessage()+" : {}", msg);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }

                    RMap<String,String> rmap = redisson.getMap(RedisKey.parseValue(redisKey.getOrder().getSimplify(), orderId));

                    rmap.forEach((k,v) -> {
                        Integer num = Integer.parseInt(v);
                        logger.info("invoke reverStock [sku : {}]  [num : {}]", k,num);
                        productService.revertStock(Long.parseLong(k), num,0);
                    });
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        try {
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

    }

}
