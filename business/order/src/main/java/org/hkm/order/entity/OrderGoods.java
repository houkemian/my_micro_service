package org.hkm.order.entity;

import lombok.Data;
import org.hkm.base.BaseEntity;

@Data
public class OrderGoods extends BaseEntity {

    private Long id;

    private Long orderId;

    private Long skuId;

    private Integer count;


}
