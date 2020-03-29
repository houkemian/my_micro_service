package org.hkm.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name="product")
public interface ProductService {

    @PostMapping("/p_sku/stock/{id}/reduce/{num}")
    int reduceStock(@PathVariable("id") Long id, @PathVariable("num") int num);

}
