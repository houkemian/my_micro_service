package org.hkm.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hkm.common.Result;
import org.hkm.order.entity.Order;
import org.hkm.order.feign.ProductService;
import org.hkm.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /*
    {"1":{"1":3,"2":5}}
     */
    @PostMapping
    public Result<Order> order(@RequestBody String body) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Map<String, Integer>> param = mapper.readValue(body, Map.class);
            Long uid = 0l;
            Map<String, Integer> skuMap = null;
            for (String key : param.keySet()) {
                uid = Long.parseLong(key);
                skuMap = param.get(key);
            }
            return orderService.order(uid, skuMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Result.failure();

    }


}
