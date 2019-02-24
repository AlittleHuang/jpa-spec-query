package com.github.data.query.support;

import com.github.data.query.specification.ConditionalOperator;
import com.github.data.query.specification.WhereClause;
import lombok.Getter;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.criteria.Predicate.BooleanOperator.AND;

@Getter
public class WhereClauseItem implements WhereClause {

    final SimpleFieldPath path;

    boolean compound;

    Object value;

    Predicate.BooleanOperator booleanOperator = AND;

    ConditionalOperator conditionalOperator;//条件运算符

    boolean negate = false;//取反

    final List<WhereClauseItem> compoundItems;

    public WhereClauseItem(SimpleFieldPath path) {
        this.path = path;
        if (path == null) {
            compound = true;
            compoundItems = new ArrayList<>();
        } else {
            compound = false;
            compoundItems = null;
        }
    }
}
