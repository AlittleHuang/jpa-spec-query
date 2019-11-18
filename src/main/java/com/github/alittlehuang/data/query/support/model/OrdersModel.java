package com.github.alittlehuang.data.query.support.model;

import com.github.alittlehuang.data.query.specification.Direction;
import com.github.alittlehuang.data.query.specification.Expression;
import com.github.alittlehuang.data.query.specification.Orders;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrdersModel<T> extends ExpressionModel<T> implements Orders<T>, Serializable {
    protected Direction direction = Direction.ASC;

    public OrdersModel() {
    }

    public OrdersModel(Expression<T> expression, Class<? extends T> javaType) {
        super(expression, javaType);
    }
}
