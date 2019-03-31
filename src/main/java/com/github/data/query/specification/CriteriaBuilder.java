package com.github.data.query.specification;

import com.github.data.query.support.Expressions;

import javax.persistence.LockModeType;
import javax.persistence.criteria.JoinType;

public interface CriteriaBuilder<T, THIS extends CriteriaBuilder<T, THIS>> extends WhereClauseBuilder<T, THIS> {

    THIS addSelect(String... paths);

    THIS addSelect(Expressions<T, ?> paths);

    THIS addSelect(Expressions<T, ?> paths, AggregateFunctions aggregate);

    THIS addGroupings(String... paths);

    THIS addGroupings(Expressions<T, ?> paths);

    THIS addOrdersAsc(String... paths);

    THIS addOrdersAsc(Expressions<T, ?> paths);

    THIS addOrdersDesc(String... paths);

    THIS addOrdersDesc(Expressions<T, ?> paths);

    THIS fetch(String... paths);

    THIS fetch(String paths, JoinType joinType);

    THIS fetch(Expressions<T, ?> paths);

    THIS fetch(Expressions<T, ?> paths, JoinType joinType);

    THIS setOffset(long offset);

    THIS setMaxResult(long maxResult);

    /**
     * set pageable
     *
     * @param page Starting from zero
     * @param size max size per page
     * @return self
     */
    THIS setPageable(long page, long size);

    THIS setLockModeType(LockModeType lockModeType);

    Criteria getCriteria();

}
