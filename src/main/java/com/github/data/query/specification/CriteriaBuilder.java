package com.github.data.query.specification;

import javax.persistence.LockModeType;
import javax.persistence.criteria.JoinType;

public interface CriteriaBuilder<T, THIS extends CriteriaBuilder<T, THIS>> extends WhereClauseBuilder<T, THIS> {

    THIS addSelect(String... paths);

    THIS addSelect(Getter<T, ?> paths);

    THIS addSelect(Getter<T, ?> paths, AggregateFunctions aggregate);

    THIS addGroupings(String... paths);

    THIS addGroupings(Getter<T, ?> paths);

    THIS addOrdersAsc(String... paths);

    THIS addOrdersAsc(Getter<T, ?> paths);

    THIS addOrdersDesc(String... paths);

    THIS addOrdersDesc(Getter<T, ?> paths);

    THIS fetch(String... paths);

    THIS fetch(String paths, JoinType joinType);

    THIS fetch(Getter<T, ?> paths);

    THIS fetch(Getter<T, ?> paths, JoinType joinType);

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
