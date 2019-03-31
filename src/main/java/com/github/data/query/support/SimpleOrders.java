package com.github.data.query.support;

import com.github.data.query.specification.AttrExpression;
import com.github.data.query.specification.Orders;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

public class SimpleOrders<T> implements Orders<T> {

    @Getter
    @Setter
    private Sort.Direction direction;

    private AttrExpression<T> attribute;

    public SimpleOrders(Sort.Direction direction, String path) {
        attribute = new SimpleExpressions<>(path);
        this.direction = direction;
    }

    public SimpleOrders(Sort.Direction direction, Expressions<T, ?> expression) {
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
    public String[] getNames(Class<? extends T> cls) {
        return attribute.getNames(cls);
    }


}
