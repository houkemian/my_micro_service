package org.hkm.order.feign;

import org.hkm.common.Result;
import org.hkm.order.feign.entity.ProductSKU;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name="product")
public interface ProductService {

    @PostMapping("/p_sku/stock/{id}/reduce/{num}")
    Result<Integer> reduceStock(@PathVariable("id") Long id, @PathVariable("num") int num);

    @GetMapping("/p_sku/{id}")
    Result<ProductSKU> getProductSku(@PathVariable("id") Long id);

}
