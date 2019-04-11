package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.specification.Expression;
import com.github.alittlehuang.data.query.specification.WhereClause;
import com.github.alittlehuang.data.query.specification.ConditionalOperator;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.criteria.Predicate.BooleanOperator.AND;

public class SimpleWhereClause<T> implements WhereClause<T> {

    protected final Expression<T> expression;

    protected boolean compound;

    protected Object parameter;//参数

    protected Predicate.BooleanOperator booleanOperator = AND;

    protected ConditionalOperator conditionalOperator;//条件运算符

    protected boolean negate = false;//取反

    protected final List<WhereClause<T>> compoundItems;

    public SimpleWhereClause(Expression<T> expression) {
        this.expression = expression;
        if ( expression == null) {
            compound = true;
            compoundItems = new ArrayList<>();
        } else {
            compound = false;
            compoundItems = null;
        }
    }

    @Override
    public Expression<T> getExpression() {
        return expression;
    }

    @Override
    public boolean isCompound() {
        return compound;
    }

    @Override
    public Object getParameter() {
        return parameter;
    }

    @Override
    public Predicate.BooleanOperator getBooleanOperator() {
        return booleanOperator;
    }

    @Override
    public ConditionalOperator getConditionalOperator() {
        return conditionalOperator;
    }

    @Override
    public boolean isNegate() {
        return negate;
    }

    @Override
    public List<WhereClause<T>> getCompoundItems() {
        return compoundItems;
    }
}
