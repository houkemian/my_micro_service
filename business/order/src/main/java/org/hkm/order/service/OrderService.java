package org.hkm.order.service;

import java.util.Map;

public interface OrderService {

    Long order(Long uid, Map<String, Integer> skus);

}
