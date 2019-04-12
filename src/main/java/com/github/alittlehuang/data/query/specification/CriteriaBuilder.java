package com.github.alittlehuang.data.query.specification;

import javax.persistence.LockModeType;
import javax.persistence.criteria.JoinType;

public interface CriteriaBuilder<T, THIS extends CriteriaBuilder<T, THIS>> extends WhereClauseBuilder<T, THIS> {

    THIS addSelect(String... paths);

    THIS addSelect(Expressions<T, ?> expression);

    THIS addSelect(Expressions<T, Number> expression, AggregateFunctions aggregate);

    THIS addGroupings(String... paths);

    THIS addGroupings(Expressions<T, ?> expression);

    THIS addOrdersAsc(String... paths);

    THIS addOrdersAsc(Expressions<T, ?> expression);

    THIS addOrdersDesc(String... paths);

    THIS addOrdersDesc(Expressions<T, ?> expression);

    THIS fetch(String... paths);

    THIS fetch(String paths, JoinType joinType);

    THIS fetch(Expressions<T, ?> expression);

    THIS fetch(Expressions<T, ?> expression, JoinType joinType);

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
