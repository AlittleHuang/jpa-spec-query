package com.github.jpa.spec.query.api;

import com.github.jpa.spec.Getters;

public interface CriteriaBuilder<T, THIS extends CriteriaBuilder<T, THIS>> {
    THIS addSelect(String... paths);

    @SuppressWarnings("unchecked")
    THIS addSelect(Getters<T, ?>... paths);

    THIS addGroupings(String... paths);


    @SuppressWarnings("unchecked")
    THIS addGroupings(Getters<T, ?>... paths);

    THIS addOrdersAsc(String... paths);

    @SuppressWarnings("unchecked")
    THIS addOrdersAsc(Getters<T, ?>... paths);


    THIS addOrdersDesc(String... paths);

    @SuppressWarnings("unchecked")
    THIS addOrdersDesc(Getters<T, ?>... paths);

    THIS addFetchs(String... paths);

    @SuppressWarnings("unchecked")
    THIS addFetchs(Getters<T, ?>... paths);
}
