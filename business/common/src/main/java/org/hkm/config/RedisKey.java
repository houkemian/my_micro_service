package org.hkm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "rediskey")
public class RedisKey {

    public static String parseValue(String key, Object appendValue) {
        return key + ":" + appendValue.toString();
    }

    private ProductKeys product;

    @Data
    public static class ProductKeys {
        private String skumap;
        private Lock lock;

        @Data
        public static class Lock {
            private String select;
            private String stock;
        }

    }

}
