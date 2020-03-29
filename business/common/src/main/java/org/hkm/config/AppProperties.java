package org.hkm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "hkm")
public class AppProperties {

    private String virtualHost;

    private Redis redis;

    private Zookeeper zookeeper;

    private String name;


    @Data
    public static class Redis {
        private String master;
        private List<String> slaves;
        private int db;
    }

    @Data
    public static class Zookeeper {
        private String address;
        private int sessionTimeout;
    }

}
