package org.hkm.product.service;

import org.hkm.product.entity.ProductSKU;
import org.redisson.api.RMap;

import java.util.List;

public interface ProductService {


    /**
     *
     * @param id
     * @return ProductSKU
     */
    ProductSKU getById(Long id);

    /**
     *
     * @param id
     * @return RMap<String, Object>
     */
    RMap<String, Object> getByIdFromCache(Long id);

    int getStockById(Long id);

    int reduceStock(Long id, int num);

}
