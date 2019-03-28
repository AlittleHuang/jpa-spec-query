package com.github.jpa.support;

import com.github.data.query.support.AbstractQueryStored;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;

import javax.persistence.EntityManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractJpaStored<T> extends AbstractQueryStored<T> {
    protected static final int DEFAULT_PAGE_SIZE = 10;

    protected EntityManager entityManager;
    protected Class<T> type;

    public AbstractJpaStored(EntityManager entityManager, Class<T> type) {
        this.entityManager = entityManager;
        this.type = type;
    }

    private static final Map<Class, JpaEntityInformation> INFORMATION_MAP = new ConcurrentHashMap<>();

    protected JpaEntityInformation<T, ?> getJpaEntityInformation() {
        //noinspection unchecked
        return INFORMATION_MAP.computeIfAbsent(
                type,
                type -> JpaEntityInformationSupport.getEntityInformation(type, entityManager)
        );
    }

}
