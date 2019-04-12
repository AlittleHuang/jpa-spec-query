package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.specification.Direction;
import com.github.alittlehuang.data.query.specification.Expression;
import com.github.alittlehuang.data.query.specification.Expressions;
import com.github.alittlehuang.data.query.specification.Orders;
import lombok.Getter;
import lombok.Setter;

public class SimpleOrders<T> implements Orders<T> {

    @Getter
    @Setter
    private Direction direction;

    private Expression<T> attribute;

    public SimpleOrders(Direction direction, String path) {
        attribute = new SimpleExpression<>(path);
        this.direction = direction;
    }

    public SimpleOrders(Direction direction, Expressions<T, ?> expression) {
        this.attribute = expression;
        this.direction = direction;
    }

    @Override
    public Object[] getArgs() {
        return attribute.getArgs();
    }

    @Override
    public Function getFunction() {
        return attribute.getFunction();
    }

    @Override
    public String[] getNames() {
        return attribute.getNames();
    }

    @Override
    public Expression<T> getSubexpression() {
        return attribute;
    }
}
