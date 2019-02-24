package com.github.jpa.spec.repostory;

import com.github.jpa.spec.Criteria;
import com.github.jpa.spec.CriteriaImpl;
import com.github.jpa.spec.query.api.Query;
import com.github.jpa.spec.query.impl.JpaStored;
import com.github.jpa.spec.query.impl.QueryImpl;

import javax.persistence.EntityManager;

public class TypeRepostory<T> {

    protected final Class<T> entityType;
    protected final EntityManager entityManager;

    public TypeRepostory(Class<T> entityType, EntityManager entityManager) {
        this.entityType = entityType;
        this.entityManager = entityManager;
    }

    public Query<T> getCriteria(){
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
