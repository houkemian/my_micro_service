package org.hkm.rocketmq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.hkm.config.RocketMQProperties;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseProducer {

    @Autowired
    protected RocketMQProperties prop;

    protected DefaultMQProducer producer;

    protected String producerGroup;

    public BaseProducer(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    void init() {
        producer = new DefaultMQProducer(producerGroup);

        producer.setNamesrvAddr(prop.getNameSrvAddr());

        try {
            producer.start();
        } catch (MQClientException e) {
        }
    }

    public void send(Message message) {
        if (producer == null) {
            init();
        }

        try {
            producer.send(message);
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
