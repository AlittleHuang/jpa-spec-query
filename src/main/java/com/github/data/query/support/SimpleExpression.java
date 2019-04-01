package com.github.data.query.support;

import com.github.data.query.specification.AttrExpression;

public class SimpleExpression<T, R> implements Expressions<T, R> {

    private Expressions<T, R> expressions;

    private Function type;

    private Object[] args;

    public SimpleExpression(Expressions<T, R> expressions, Function type, Object... args) {
        this.expressions = expressions;
        this.type = type;
        this.args = args;
    }

    public SimpleExpression(String path) {
        this.expressions = new Expressions<T, R>() {
            private String[] names = path.split("\\.");

            @Override
            public String[] getNames(Class<? extends T> root) {
                return names;
            }

            @Override
            public R apply(T t) {
                throw new UnsupportedOperationException();
            }
        };
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

    @Override
    public String[] getNames(Class<? extends T> type) {
        return expressions.getNames(type);
    }

    @Override
    public AttrExpression getSubexpression() {
        return expressions;
    }
}
