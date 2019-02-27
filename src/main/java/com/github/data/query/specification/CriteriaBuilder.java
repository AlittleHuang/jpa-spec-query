package com.github.data.query.specification;

import javax.persistence.LockModeType;

public interface CriteriaBuilder<T, THIS extends CriteriaBuilder<T, THIS>> extends WhereClauseBuilder<T, THIS> {

    THIS addSelect(String... paths);

    THIS addSelect(Getter<T, ?> paths);

    THIS addGroupings(String... paths);

    THIS addGroupings(Getter<T, ?> paths);

    THIS addOrdersAsc(String... paths);

    THIS addOrdersAsc(Getter<T, ?> paths);

    THIS addOrdersDesc(String... paths);

    THIS addOrdersDesc(Getter<T, ?> paths);

    THIS fetch(String... paths);

    THIS fetch(Getter<T, ?> paths);

    THIS setLockModeType(LockModeType lockModeType);

    Criteria getCriteria();

}
