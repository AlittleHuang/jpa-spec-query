package com.github.alittlehuang.data.query.specification;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * @author ALittleHuang
 */
public interface WhereClause<T> {

    List<? extends WhereClause<T>> getCompoundItems();

    Expression<T> getExpression();

    Object getParameter();

    Predicate.BooleanOperator getBooleanOperator();

    boolean isCompound();

    ConditionalOperator getConditionalOperator();

    boolean isNegate();


}
