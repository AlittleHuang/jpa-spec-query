package com.github.data.query.specification;

import javax.persistence.LockModeType;
import java.util.List;

public interface Criteria<T> {

    WhereClause getWhereClause();

    List<? extends FieldPath<T>> getSelections();

    List<? extends FieldPath<T>> getGroupings();

    List<? extends Orders<T>> getOrders();

    List<? extends FieldPath<T>> getFetchs();

    Integer getOffset();

    Integer getMaxResults();

    LockModeType getLockModeType();

}
