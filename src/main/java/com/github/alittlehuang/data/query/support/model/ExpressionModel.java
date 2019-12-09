package com.github.alittlehuang.data.query.support.model;

import com.github.alittlehuang.data.query.specification.Expression;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class ExpressionModel<T> extends AttributePathModel<T> implements Expression<T>, Serializable {

    Object[] args = Expression.EMPTY_ARGS;
    Expression<T> subexpression;
    Function function = Function.NONE;
    String functionName;

    public ExpressionModel() {
    }

    public ExpressionModel(String[] names) {
        super(names);
    }

    public ExpressionModel(Expression<T> expression) {
        super(expression);
        this.args = expression.getArgs();
        this.subexpression = expression.getSubexpression();
        this.function = expression.getFunction();
        this.functionName = expression.getFunctionName();
    }

    public static <T> ExpressionModel<T> convertExpression(Expression<T> expression) {
        if (expression.getClass() == ExpressionModel.class) {
            return (ExpressionModel<T>) expression;
        } else {
            return new ExpressionModel<>(expression);
        }
    }


    public static <T> List<ExpressionModel<T>> convertExpression(List<? extends Expression<T>> expression) {

        if (expression == null || expression.isEmpty()) {
            return Collections.emptyList();
        }

        return expression.stream().map(ExpressionModel::convertExpression).collect(Collectors.toList());

    }
}
