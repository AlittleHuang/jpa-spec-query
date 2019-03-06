package com.github.data.query.specification;

import javax.persistence.LockModeType;
import java.util.List;

public interface Criteria<T> {

    WhereClause<T> getWhereClause();

    List<? extends Attribute<T>> getSelections();

    List<? extends Attribute<T>> getGroupings();

    List<? extends Orders<T>> getOrders();

    List<? extends FetchAttribute<T>> getFetchs();

    Long getOffset();

    Long getMaxResults();

    LockModeType getLockModeType();

}
