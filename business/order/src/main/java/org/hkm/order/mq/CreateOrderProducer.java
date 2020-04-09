package org.hkm.order.mq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.hkm.config.RocketMQProperties;
import org.hkm.rocketmq.BaseProducer;
import org.hkm.rocketmq.DelayLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderProducer extends BaseProducer {

    public CreateOrderProducer() {
        super("createOrderGroup");
    }

    public void send(Long orderId) {

        Message message = new Message();

        message.setBody(orderId.toString().getBytes());

        message.setTopic(prop.getTopic().getOrder().getCreate());

        send(message);

    }


}
