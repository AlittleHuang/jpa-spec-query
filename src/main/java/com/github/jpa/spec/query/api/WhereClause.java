package com.github.jpa.spec.query.api;


import javax.persistence.criteria.Predicate;
import java.util.List;

public interface WhereClause<T> {

    List<? extends WhereClause<T>> getCompoundItems();

    FieldPath getPath();

    Object getValue();

    Predicate.BooleanOperator getBooleanOperator();

    ConditionalOperator getConditionalOperator();

    boolean isNegate();


}
