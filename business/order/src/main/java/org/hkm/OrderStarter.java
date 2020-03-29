package org.hkm;

import org.hkm.config.AppProperties;
import org.hkm.config.RedisKey;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication(scanBasePackages = {"org.hkm"})
@EnableEurekaClient
@EnableFeignClients
@EnableConfigurationProperties({AppProperties.class, RedisKey.class})
public class OrderStarter {


    public static void main(String[] args) {
        run(OrderStarter.class, args);
    }



}

