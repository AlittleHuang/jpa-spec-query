package com.github.data.query.specification;


import javax.persistence.criteria.Predicate;
import java.util.List;

public interface WhereClause<T> {

    List<? extends WhereClause<T>> getCompoundItems();

    AttrExpression<T> getExpression();

    Object getValue();

    Predicate.BooleanOperator getBooleanOperator();

    boolean isCompound();

    ConditionalOperator getConditionalOperator();

    boolean isNegate();


}
