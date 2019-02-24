package com.github.jpa.spec.query.api;

import javax.persistence.LockModeType;

public interface CriteriaBuilder<T, THIS extends CriteriaBuilder<T, THIS>> {
    THIS addSelect(String... paths);

    @SuppressWarnings("unchecked")
    THIS addSelect(Path<T, ?> paths);

    THIS addGroupings(String... paths);

    @SuppressWarnings("unchecked")
    THIS addGroupings(Path<T, ?> paths);

    THIS addOrdersAsc(String... paths);

    @SuppressWarnings("unchecked")
    THIS addOrdersAsc(Path<T, ?> paths);

    THIS addOrdersDesc(String... paths);

    @SuppressWarnings("unchecked")
    THIS addOrdersDesc(Path<T, ?> paths);

    THIS addFetchs(String... paths);

    THIS addFetchs(Path<T, ?> paths);

    THIS setLockModeType(LockModeType lockModeType);
}
