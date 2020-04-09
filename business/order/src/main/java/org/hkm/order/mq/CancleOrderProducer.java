package org.hkm.order.mq;

import org.apache.rocketmq.common.message.Message;
import org.hkm.rocketmq.BaseProducer;
import org.springframework.stereotype.Component;

@Component
public class CancleOrderProducer extends BaseProducer {

    public CancleOrderProducer() {
        super("cancleOrderGroup");
    }

    public void send(Long orderId){
        Message message = new Message();
        message.setTopic(prop.getTopic().getOrder().getCancle());
        message.setBody(orderId.toString().getBytes());
        send(message);
    }

}