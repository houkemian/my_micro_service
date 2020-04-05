package org.hkm.order.service;

import org.hkm.common.Result;
import org.hkm.order.entity.Order;

import java.util.Map;

public interface OrderService {

    Result<Order> order(Long uid, Map<String, Integer> skus);

}
