package com.github.jpa.spec.query.api;

import javax.persistence.LockModeType;
import java.util.List;

public interface Criteria<T> {//查询标准

    WhereClause getWhereClause();

    List<? extends FieldPath<T>> getSelections();

    List<? extends FieldPath<T>> getGroupings();

    List<? extends Orders<T>> getOrders();

    List<? extends FieldPath<T>> getFetchs();

    Integer getOffset();

    Integer getMaxResults();

    LockModeType getLockModeType();

}
