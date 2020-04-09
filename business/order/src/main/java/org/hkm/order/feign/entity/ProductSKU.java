package org.hkm.order.feign.entity;

import lombok.Data;
import org.hkm.base.BaseEntity;

import java.math.BigDecimal;

@Data
public class ProductSKU extends BaseEntity {

    private Long id;

    private Long spuId;

    private BigDecimal price;

    private String color;
    private String name;

    private Float stock;

}
