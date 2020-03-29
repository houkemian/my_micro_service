package org.hkm.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.hkm.order.entity.Order;
import org.hkm.order.entity.OrderGoods;

@Mapper
public interface OrderGoodsMapper {
    int insertOrderGoods(OrderGoods orderGoods);
}
