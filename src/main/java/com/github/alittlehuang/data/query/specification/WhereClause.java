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

    static boolean isEmpty(WhereClause<?> whereClause) {
        if ( whereClause.isCompound() ) {
            List<? extends WhereClause<?>> compoundItems = whereClause.getCompoundItems();
            if ( compoundItems != null ) {
                for ( WhereClause<?> item : compoundItems ) {
                    if ( !isEmpty(item) ) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return whereClause.getExpression() == null;
        }
    }

}
