package com.github.alittlehuang.data.jpa.repostory;

import com.github.alittlehuang.data.query.specification.Query;
import com.github.alittlehuang.data.query.support.QueryImpl;
import com.github.alittlehuang.data.jpa.support.JpaQueryStored;

import javax.persistence.EntityManager;

public class TypeRepository<T> {

    protected final Class<T> entityType;
    protected final EntityManager entityManager;

    public TypeRepository(Class<T> entityType, EntityManager entityManager) {
        this.entityType = entityType;
        this.entityManager = entityManager;
    }

    public Query<T> query() {
        return new QueryImpl<>(new JpaQueryStored<>(entityManager,entityType));
    }

    public T persist(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    public T merge(T entity) {
        return entityManager.merge(entity);
    }


}
