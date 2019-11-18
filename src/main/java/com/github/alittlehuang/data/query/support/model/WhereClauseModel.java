package com.github.alittlehuang.data.query.support.model;

import com.github.alittlehuang.data.query.specification.ConditionalOperator;
import com.github.alittlehuang.data.query.specification.Expression;
import com.github.alittlehuang.data.query.specification.WhereClause;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.criteria.Predicate.BooleanOperator.AND;

/**
 * @author ALittleHuang
 */
@Data
public class WhereClauseModel<T> implements WhereClause<T>, Serializable {

    protected final ExpressionModel<T> expression;

    protected boolean compound;

    protected Object parameter;

    protected Predicate.BooleanOperator booleanOperator = AND;

    protected ConditionalOperator conditionalOperator;

    protected boolean negate = false;

    protected final List<WhereClauseModel<T>> compoundItems;

    protected final Class<T> javaType;

    public WhereClauseModel(Expression<T> expression, Class<T> javaType) {
        this.javaType = javaType;
        if (expression == null || expression.getClass() == ExpressionModel.class) {
            this.expression = (ExpressionModel<T>) expression;
        } else {
            this.expression = new ExpressionModel<>(expression, javaType);
        }
        if (expression == null) {
            compound = true;
            compoundItems = new ArrayList<>();
        } else {
            compound = false;
            compoundItems = null;
        }
    }

    public WhereClauseModel(Class<T> javaType) {
        this.javaType = javaType;
        this.expression = null;
        compound = true;
        compoundItems = new ArrayList<>();
    }

    public WhereClauseModel(WhereClause<T> whereClause, Class<T> javaType) {

        this.javaType = javaType;
        this.expression = new ExpressionModel<>(whereClause.getExpression(), javaType);
        this.compound = whereClause.isCompound();
        this.parameter = whereClause.getParameter();
        this.booleanOperator = whereClause.getBooleanOperator();
        this.conditionalOperator = whereClause.getConditionalOperator();
        this.negate = whereClause.isNegate();
        this.compoundItems = new ArrayList<>();

        for (WhereClause<T> compoundItem : whereClause.getCompoundItems()) {
            compoundItems.add(convert(compoundItem, javaType));
        }

    }

    @NotNull
    public static <T> WhereClauseModel<T> convert(WhereClause<T> whereClause, Class<T> javaType) {
        WhereClauseModel<T> add;
        if (whereClause.getClass() == WhereClauseModel.class) {
            add = (WhereClauseModel<T>) whereClause;
        } else {
            add = new WhereClauseModel<>(whereClause, javaType);
        }
        return add;
    }


}
