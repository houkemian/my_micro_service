package org.hkm.config.db;

import org.apache.zookeeper.ZooKeeper;
import org.hkm.config.AppProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class MiddlewareConfig {

    @Autowired
    private AppProperties prop;

    @Bean
    public RedissonClient redissionClient() {

        Config config = new Config();
        config.setCodec(new StringCodec());

        AppProperties.Redis redis = prop.getRedis();

        config.useMasterSlaveServers().setMasterAddress(redis.getMaster())
                .addSlaveAddress(redis.getSlaves().toArray(new String[0])).setDatabase(redis.getDb());
        return Redisson.create(config);
    }

}
