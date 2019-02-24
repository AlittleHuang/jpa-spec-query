package com.github.jpa.spec.repostory;

import com.github.jpa.spec.Criteria;
import com.github.jpa.spec.CriteriaImpl;

import javax.persistence.EntityManager;

public class CommonRepostory {

    protected final EntityManager entityManager;

    public CommonRepostory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <T> Criteria<T> getCriteria(Class<T> entityType){
        return new CriteriaImpl<>(this.entityManager, entityType);
    }

    public <T> T persist(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    public <T> T merge(T entity) {
        return entityManager.merge(entity);
    }

}
