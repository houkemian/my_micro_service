package org.hkm.product.controller;


import org.hkm.common.Result;
import org.hkm.product.entity.ProductSKU;
import org.hkm.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/p_sku")
public class ProductSKUController {

    private Logger logger = LoggerFactory.getLogger(ProductSKUController.class);

    @Autowired
    private ProductService productService;

    @PostMapping("/stock/{id}/reduce/{num}")
    public Result<Integer> reduceStock(@PathVariable("id")Long id, @PathVariable("num")int num){
        return this.productService.reduceStock(id,num);
    }

    @GetMapping("/stock/{id}")
    public int getStock(@PathVariable("id")Long id){
        return this.productService.getStockById(id);
    }

    @GetMapping("{id}")
    public Result<ProductSKU> get(@PathVariable("id") Long id) {
        return productService.getById(id);
    }





}
