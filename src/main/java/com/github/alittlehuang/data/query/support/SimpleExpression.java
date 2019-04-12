package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.specification.Expression;
import com.github.alittlehuang.data.query.specification.Expressions;

public class SimpleExpression<T, R> implements Expressions<T, R> {

    private Expression<T> expressions;

    private Expression.Function type;

    private Object[] args;

    private String functionName;

    public SimpleExpression(Expressions<T, ?> expressions, Expression.Function type, Object... args) {
        this.expressions = expressions;
        this.type = type;
        this.args = args;
    }

    public SimpleExpression(String functionName, Expressions<T, ?> expressions, Expression.Function type, Object... args) {
        this.expressions = expressions;
        this.type = type;
        this.args = args;
    }

    public SimpleExpression(String path) {
        this.expressions = new Expressions<T, R>() {
            private String[] names = path.split("\\.");

            @Override
            public String[] getNames() {
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
    public String[] getNames() {
        return expressions.getNames();
    }

    @Override
    public Expression<T> getSubexpression() {
        return expressions;
    }

    @Override
    public String getFunctionName() {
        return functionName;
    }
}
