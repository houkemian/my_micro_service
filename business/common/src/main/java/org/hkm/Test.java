package org.hkm;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.hkm.rocketmq.DelayLevel;

public class Test {

    public static void main(String[] args) throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("producerGroup");

        producer.setNamesrvAddr("192.168.3.35:9876");

        producer.start();

        int total = 10;

        for (int i = 0; i < total; i++) {

            Message msg = new Message("cancle_order", "Hello scheduled message +i".getBytes());

            msg.setDelayTimeLevel(DelayLevel.MINUTE_1.level());

            producer.send(msg,10000);


        }

        producer.shutdown();


    }

}
