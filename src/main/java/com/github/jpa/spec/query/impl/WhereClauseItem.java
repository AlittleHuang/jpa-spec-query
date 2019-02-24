package com.github.jpa.spec.query.impl;

import com.github.jpa.spec.query.api.ConditionalOperator;
import com.github.jpa.spec.query.api.WhereClause;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.criteria.Predicate.BooleanOperator.AND;

@Getter
@RequiredArgsConstructor
public class WhereClauseItem<T> implements WhereClause<T> {

    final SimpleFieldPath path;

    Object value;

    Predicate.BooleanOperator booleanOperator = AND;

    ConditionalOperator conditionalOperator;//条件运算符

    boolean negate = false;//取反

    List<WhereClauseItem<T>> compoundItems = new ArrayList<>();

}
