package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.specification.Expression;

public class SimpleExpression<T, R> implements Expressions<T, R> {

    private Expressions<T, ?> expressions;

    private Expression.Function type;

    private Object[] args;

    public SimpleExpression(Expressions<T, ?> expressions, Expression.Function type, Object... args) {
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
    public Expression.Function getFunction() {
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
    public <V> Expressions<T, V> to(Expressions<? extends R, ? super V> expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getNames(Class<? extends T> type) {
        return expressions.getNames(type);
    }

    @Override
    public Expression<T> getSubexpression() {
        return expressions;
    }
}
