package org.hkm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(value = "rocketmq")
public class RocketMQProperties {

    private String nameSrvAddr;

    private Topic topic;

    @Data
    public static class Topic {

        private Order order;

        @Data
        public static class Order {
            private String cancle;
            private String create;
        }

    }

}
