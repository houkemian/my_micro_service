package org.hkm.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.hkm.order.entity.Order;

@Mapper
public interface OrderMapper {


    int insertOrder(Order order);

    Order findById(Long id);

    int updateState(Order order);

}
