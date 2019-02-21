package com.github.jpa.spec.com.github.jpa.repostory;

import com.github.jpa.spec.Criteria;
import com.github.jpa.spec.CriteriaImpl;

import javax.persistence.EntityManager;

public class CommonRepostory {

    protected final EntityManager em;

    public CommonRepostory(EntityManager em) {
        this.em = em;
    }

    public <T> Criteria<T> getCriteria(Class<T> entityType){
        return new CriteriaImpl<>(em, entityType);
    }

}
