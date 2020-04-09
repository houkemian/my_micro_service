package org.hkm.product.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerStarter implements ApplicationListener<WebServerInitializedEvent> {

    @Autowired
    private CancleOrderConsumer cancleOrderConsumer;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        cancleOrderConsumer.start();
    }
}
