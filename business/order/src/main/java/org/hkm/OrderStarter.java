package org.hkm;

import org.hkm.config.AppProperties;
import org.hkm.config.RedisKey;
import org.hkm.config.RocketMQProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication(scanBasePackages = {"org.hkm"})
@EnableEurekaClient
@EnableFeignClients
@EnableConfigurationProperties({AppProperties.class, RedisKey.class, RocketMQProperties.class})
public class OrderStarter {


    public static void main(String[] args) {
        run(OrderStarter.class, args);
    }



}

