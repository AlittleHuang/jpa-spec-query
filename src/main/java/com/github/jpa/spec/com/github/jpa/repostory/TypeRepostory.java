package com.github.jpa.spec.com.github.jpa.repostory;

import com.github.jpa.spec.Criteria;
import com.github.jpa.spec.CriteriaImpl;

import javax.persistence.EntityManager;

public abstract class TypeRepostory<T> {

    protected final Class<T> entityType;
    protected final EntityManager entityManager;

    public TypeRepostory(Class<T> entityType, EntityManager entityManager) {
        this.entityType = entityType;
        this.entityManager = entityManager;
    }

    public Criteria getCriteria(){
        return new CriteriaImpl<>(entityManager, entityType);
    }


}
