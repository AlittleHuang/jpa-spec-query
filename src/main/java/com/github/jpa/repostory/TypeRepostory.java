package com.github.jpa.repostory;

import com.github.data.query.specification.Query;
import com.github.data.query.support.QueryImpl;
import com.github.jpa.support.JpaStored;

import javax.persistence.EntityManager;

public class TypeRepostory<T> {

    protected final Class<T> entityType;
    protected final EntityManager entityManager;

    public TypeRepostory(Class<T> entityType, EntityManager entityManager) {
        this.entityType = entityType;
        this.entityManager = entityManager;
    }

    public Query<T> query() {
        return new QueryImpl<>(new JpaStored<>(entityManager,entityType));
    }

    public T persist(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    public T merge(T entity) {
        return entityManager.merge(entity);
    }


}
