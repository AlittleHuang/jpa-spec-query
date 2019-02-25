package com.github.data.query.specification;

import javax.persistence.LockModeType;

public interface CriteriaBuilder<T, THIS extends CriteriaBuilder<T, THIS>> extends WhereClauseBuilder<T, THIS> {

    THIS addSelect(String... paths);

    THIS addSelect(Path<T, ?> paths);

    THIS addGroupings(String... paths);

    THIS addGroupings(Path<T, ?> paths);

    THIS addOrdersAsc(String... paths);

    THIS addOrdersAsc(Path<T, ?> paths);

    THIS addOrdersDesc(String... paths);

    THIS addOrdersDesc(Path<T, ?> paths);

    THIS addFetchs(String... paths);

    THIS addFetchs(Path<T, ?> paths);

    THIS setLockModeType(LockModeType lockModeType);

}
