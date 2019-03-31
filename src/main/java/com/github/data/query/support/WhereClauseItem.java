package com.github.data.query.support;

import com.github.data.query.specification.ConditionalOperator;
import com.github.data.query.specification.AttrExpression;
import com.github.data.query.specification.WhereClause;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.criteria.Predicate.BooleanOperator.AND;

public class WhereClauseItem<T> implements WhereClause<T> {

    protected final AttrExpression<T> path;

    protected boolean compound;

    protected Object value;

    protected Predicate.BooleanOperator booleanOperator = AND;

    protected ConditionalOperator conditionalOperator;//条件运算符

    protected boolean negate = false;//取反

    protected final List<WhereClause<T>> compoundItems;

    public WhereClauseItem(AttrExpression<T> path) {
        this.path = path;
        if (path == null) {
            compound = true;
            compoundItems = new ArrayList<>();
        } else {
            compound = false;
            compoundItems = null;
        }
    }

    @Override
    public AttrExpression<T> getExpression() {
        return path;
    }

    @Override
    public boolean isCompound() {
        return compound;
    }

    @Override
    public Object getValue() {
        return value;
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
