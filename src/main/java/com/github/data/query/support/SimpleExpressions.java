package com.github.data.query.support;

import com.github.data.query.specification.Attribute;
import com.github.data.query.specification.Expressions;
import lombok.experimental.Delegate;

public class SimpleExpressions<T> implements Expressions<T> {

    @Delegate
    private Attribute<T> attribute;

    private Type type;

    private Object[] args;

    public SimpleExpressions(Attribute<T> attribute, Type type, Object... args) {
        this.attribute = attribute;
        this.type = type;
        this.args = args;
    }

    public SimpleExpressions(Attribute<T> attribute) {
        this.attribute = attribute;
    }

    public SimpleExpressions(String attribute) {
        this.attribute = new SimpleAttribute<>(attribute);
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Object[] getArgs() {
        return args;
    }
}
