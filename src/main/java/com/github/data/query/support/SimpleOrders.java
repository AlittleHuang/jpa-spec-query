package com.github.data.query.support;

import com.github.data.query.specification.Expressions;
import com.github.data.query.specification.Getter;
import com.github.data.query.specification.Orders;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.springframework.data.domain.Sort;

public class SimpleOrders<T> implements Orders<T> {

    @lombok.Getter
    @Setter
    private Sort.Direction direction;

    private Expressions<T> attribute;

    public SimpleOrders(Sort.Direction direction, String path) {
        attribute = new SimpleExpressions<>(path);
        this.direction = direction;
    }

    public SimpleOrders(Sort.Direction direction, Getter<T, ?> getter) {
        this.attribute = getter;
        this.direction = direction;
    }

    @Override
    public Object[] getArgs() {
        return attribute.getArgs();
    }

    @Override
    public Type getType() {
        return attribute.getType();
    }

    @Override
    public String[] getNames(Class<? extends T> cls) {
        return attribute.getNames(cls);
    }
}
