package org.hkm.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hkm.product.entity.ProductSKU;

import java.util.Map;

@Mapper
public interface ProductSkuMapper {

    ProductSKU getById(Long id);

    float getStockById(Long id);

    int updateStock(@Param("param") Map<String, Object> param);

    int revertStock(ProductSKU sku);
}
