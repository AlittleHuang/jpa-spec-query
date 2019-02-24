package com.github.jpa.spec.repostory;

import com.github.jpa.spec.query.api.Query;
import com.github.jpa.spec.query.impl.JpaStored;
import com.github.jpa.spec.query.impl.QueryImpl;

import javax.persistence.EntityManager;

public class CommonRepostory {

    protected final EntityManager entityManager;

    public CommonRepostory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <T> Query<T> query(Class<T> entityType) {
        return new QueryImpl<>(new JpaStored<>(entityManager, entityType));
    }

    public <T> T persist(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    public <T> T merge(T entity) {
        return entityManager.merge(entity);
    }

}
