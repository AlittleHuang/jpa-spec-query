package com.github.jpa.repostory;

import com.github.data.query.specification.Query;
import com.github.data.query.support.QueryImpl;
import com.github.jpa.support.JpaQueryStored;

import javax.persistence.EntityManager;

public class CommonRepository {

    protected final EntityManager entityManager;

    public CommonRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <T> Query<T> query(Class<T> entityType) {
        return new QueryImpl<>(new JpaQueryStored<>(entityManager, entityType));
    }

    public <T> T persist(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    public <T> T merge(T entity) {
        return entityManager.merge(entity);
    }

}
