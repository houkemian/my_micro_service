package org.hkm.order.mybatis;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.hkm.order.entity.Order;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(Order.DiscountType.class)
public class OrderDiscountTypeTypeHandler extends BaseTypeHandler<Order.DiscountType> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Order.DiscountType discountType, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, discountType.val());
    }

    @Override
    public Order.DiscountType getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return Order.DiscountType.discountType(resultSet.getInt(s));
    }

    @Override
    public Order.DiscountType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return Order.DiscountType.discountType(resultSet.getInt(i));
    }

    @Override
    public Order.DiscountType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return Order.DiscountType.discountType(callableStatement.getInt(i));
    }
}
