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
    private OrderKeys order;

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

    @Data
    public static class OrderKeys {
        private String ordermap;
        private String simplify;
        private Lock lock;

        @Data
        public static class Lock {
        }
    }

}
