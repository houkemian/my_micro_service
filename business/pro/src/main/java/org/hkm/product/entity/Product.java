package org.hkm.product.entity;

import lombok.Data;
import org.hkm.base.BaseEntity;

@Data
public class Product extends BaseEntity {

    private int id;

    private String name;

    private String brand;


}
