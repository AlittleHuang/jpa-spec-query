package com.github.data.query.specification;


import javax.persistence.criteria.Predicate;
import java.util.List;

public interface WhereClause {

    List<? extends WhereClause> getCompoundItems();

    FieldPath getPath();

    Object getValue();

    Predicate.BooleanOperator getBooleanOperator();

    boolean isCompound();

    ConditionalOperator getConditionalOperator();

    boolean isNegate();


}
