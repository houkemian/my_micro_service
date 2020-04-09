package org.hkm.product;

import org.apache.zookeeper.ZooKeeper;
import org.hkm.config.AppProperties;
import org.hkm.config.RedisKey;
import org.hkm.config.RocketMQProperties;
import org.hkm.product.entity.Product;
import org.hkm.product.mq.CancleOrderConsumer;
import org.hkm.product.zk.ZkWatcher;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.Properties;

import static org.springframework.boot.SpringApplication.run;


@SpringBootApplication(scanBasePackages = {"org.hkm"})
@EnableEurekaClient
@EnableConfigurationProperties({AppProperties.class, RedisKey.class, RocketMQProperties.class})
public class ProductStarter {

    public static void main(String[] args) {
        run(ProductStarter.class, args);
    }

    @Autowired
    private AppProperties prop;

    @Bean
    public ZooKeeper zk(){
        try {
            ZkWatcher watcher = new ZkWatcher();

            ZooKeeper zk = new ZooKeeper(prop.getZookeeper().getAddress(),prop.getZookeeper().getSessionTimeout(),watcher);

            watcher.setZk(zk);
            return zk;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }



}
