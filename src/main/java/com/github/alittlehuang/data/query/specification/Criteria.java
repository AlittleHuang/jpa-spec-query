package com.github.alittlehuang.data.query.specification;

import javax.persistence.LockModeType;
import java.util.List;

public interface Criteria<T> {

    WhereClause<T> getWhereClause();

    List<? extends Selection<T>> getSelections();

    List<? extends Attribute<T>> getGroupings();

    List<? extends Orders<T>> getOrders();

    List<? extends FetchAttribute<T>> getFetchAttributes();

    Long getOffset();

    Long getMaxResults();

    LockModeType getLockModeType();

    Class<T> getJavaType();

}
