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
public class OrderStateTypeHandler extends BaseTypeHandler<Order.State> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Order.State state, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, state.val());
    }

    @Override
    public Order.State getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return Order.State.state(resultSet.getInt(s));
    }

    @Override
    public Order.State getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return Order.State.state(resultSet.getInt(i));
    }

    @Override
    public Order.State getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return Order.State.state(callableStatement.getInt(i));
    }
}
