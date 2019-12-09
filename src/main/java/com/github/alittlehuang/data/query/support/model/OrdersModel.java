package com.github.alittlehuang.data.query.support.model;

import com.github.alittlehuang.data.query.specification.Direction;
import com.github.alittlehuang.data.query.specification.Expression;
import com.github.alittlehuang.data.query.specification.Orders;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrdersModel<T> extends ExpressionModel<T> implements Orders<T>, Serializable {
    protected Direction direction = Direction.ASC;

    public OrdersModel() {
    }

    public OrdersModel(Expression<T> expression) {
        super(expression);
    }

    public OrdersModel(String[] names) {
        super(names);
    }

    @NotNull
    public static <T> OrdersModel<T> convertOrders(@NotNull Orders<T> orders) {
        if (orders.getClass() == OrdersModel.class) {
            return (OrdersModel<T>) orders;
        }

        OrdersModel<T> result = new OrdersModel<>(orders);
        result.setDirection(orders.getDirection());
        return result;
    }

    public static <T> List<OrdersModel<T>> convertOrders(List<? extends Orders<T>> expression) {

        if (expression == null || expression.isEmpty()) {
            return Collections.emptyList();
        }

        return expression.stream().map(OrdersModel::convertOrders).collect(Collectors.toList());

    }
}
