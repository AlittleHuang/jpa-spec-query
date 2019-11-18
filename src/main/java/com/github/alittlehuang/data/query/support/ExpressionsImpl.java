package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.specification.Expression;
import com.github.alittlehuang.data.query.specification.Expressions;
import com.github.alittlehuang.data.query.support.model.ExpressionModel;

/**
 * @author ALittleHuang
 */
public class ExpressionsImpl<T, R> implements Expressions<T, R> {

    private Expression<T> expressions;

    private Expression.Function type = Function.NONE;

    private Object[] args = Expression.EMPTY_ARGS;

    private String functionName;

    public ExpressionsImpl(Expressions<T, ?> expressions, Expression.Function type, Object... args) {
        this.expressions = expressions;
        this.type = type;
        this.args = args;
    }

    public ExpressionsImpl(String function, Expressions<T, ?> expressions, Expression.Function type, Object... args) {
        this.expressions = expressions;
        this.type = type;
        this.args = args;
        functionName = function;
    }

    public ExpressionsImpl(String path) {
        ExpressionModel<T> expressions = new ExpressionModel<>();
        expressions.setNames(path.split("\\."));
        this.expressions = expressions;
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
    public <V> Expressions<T, V> to(Expressions<? extends R, ? extends V> expression) {
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

    @Override
    public String getFunctionName() {
        return functionName;
    }

    @Override
    public String toString() {
        return expressions.toString();
    }
}
