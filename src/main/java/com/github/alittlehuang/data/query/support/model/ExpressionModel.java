package com.github.alittlehuang.data.query.support.model;

import com.github.alittlehuang.data.query.specification.Expression;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ExpressionModel<T> extends AttributePathModel<T> implements Expression<T>, Serializable {

    Object[] args = Expression.EMPTY_ARGS;
    Expression<T> subexpression;
    Function function = Function.NONE;
    String functionName;

    public ExpressionModel() {
    }

    public ExpressionModel(Expression<T> expression, Class<? extends T> javaType) {
        super(expression, javaType);
        this.args = expression.getArgs();
        this.subexpression = expression.getSubexpression();
        this.function = expression.getFunction();
        this.functionName = expression.getFunctionName();
    }

    public static <T> ExpressionModel<T> convert(Expression<T> expression, Class<? extends T> javaType) {
        if (expression.getClass() == ExpressionModel.class) {
            return (ExpressionModel<T>) expression;
        } else {
            return new ExpressionModel<>(expression, javaType);
        }
    }

}
