package com.github.alittlehuang.data.jpa.repostory;

import com.github.alittlehuang.data.jpa.support.JpaQueryStored;
import com.github.alittlehuang.data.query.page.PageFactory;
import com.github.alittlehuang.data.query.specification.Query;
import com.github.alittlehuang.data.query.support.QueryImpl;

import javax.persistence.EntityManager;

/**
 * @author ALittleHuang
 */
public class CommonRepository {

    protected final EntityManager entityManager;

    public CommonRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <T> Query<T> query(Class<T> entityType) {
        return new QueryImpl<>(new JpaQueryStored<>(entityManager, entityType, PageFactory.getDefault()));
    }

    public <T> T persist(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    public <T> T merge(T entity) {
        return entityManager.merge(entity);
    }

}
