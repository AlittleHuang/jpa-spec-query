package com.github.data.query.support;

import com.github.data.query.specification.Attribute;
import lombok.experimental.Delegate;

public class SimpleExpressions<T, R> implements Expressions<T, R> {

    @Delegate
    private Attribute<T> attribute;

    private Function type;

    private Object[] args;

    public SimpleExpressions(Attribute<T> attribute, Function type, Object... args) {
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
    public Function getFunction() {
        return type;
    }

    @Override
    public Object[] getArgs() {
        return args;
    }

    @Override
    public R apply(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V, U extends Expressions<? super R, ? extends V>> Expressions<T, V> to(U expression) {
        throw new UnsupportedOperationException();
    }
}
