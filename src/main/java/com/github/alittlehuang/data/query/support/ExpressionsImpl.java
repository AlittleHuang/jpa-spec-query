package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.specification.Expression;
import com.github.alittlehuang.data.query.specification.Expressions;
import com.github.alittlehuang.data.query.support.model.*;

/**
 * @author ALittleHuang
 */
public class ExpressionsImpl<T, R> implements Expressions<T, R> {

    private Expressions<T, ?> expressions;

    private Expression.Function function = Expression.Function.NONE;

    private Object[] args = Expression.EMPTY_ARGS;

    private String functionName;

    public ExpressionsImpl(Expressions<T, ?> expressions, Expression.Function function, Object... args) {
        this.expressions = expressions;
        this.function = function;
        this.args = args;
    }

    public ExpressionsImpl(String function, Expressions<T, ?> expressions, Expression.Function type, Object... args) {
        this.expressions = expressions;
        this.function = type;
        this.args = args;
        functionName = function;
    }

    public ExpressionsImpl(String path) {
        String[] names = path.split("\\.");
        this.expressions = new Expressions<T, Object>() {

            @Override
            public Object apply(T t) {
                return null;
            }

            @Override
            public String[] getNames(Class<? extends T> type) {
                return names;
            }
        };
    }

    public Expression.Function getFunction() {
        return function;
    }

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
    public AttributePathModel<T> toAttributePathModel(Class<T> javaType) {
        return new AttributePathModel<>(getNames(javaType));
    }

    @Override
    public ExpressionModel<T> toExpressionModel(Class<T> javaType) {
        ExpressionModel<T> result = new ExpressionModel<>(getNames(javaType));
        result.setFunction(function);
        result.setFunctionName(functionName);
        result.setArgs(args);
        return result;
    }

    @Override
    public FetchAttributeModel<T> toFetchAttributeModel(Class<T> javaType) {
        return new FetchAttributeModel<>(getNames(javaType));
    }

    @Override
    public OrdersModel<T> toOrdersModel(Class<T> javaType) {
        OrdersModel<T> result = new OrdersModel<>(getNames(javaType));
        result.setFunction(function);
        result.setFunctionName(functionName);
        result.setArgs(args);
        return result;
    }

    @Override
    public SelectionModel<T> toSelectionModel(Class<T> javaType) {
        SelectionModel<T> result = new SelectionModel<>(getNames(javaType));
        result.setFunction(function);
        result.setFunctionName(functionName);
        result.setArgs(args);
        return result;
    }

}
