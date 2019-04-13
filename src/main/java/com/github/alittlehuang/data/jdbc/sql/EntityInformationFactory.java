package com.github.alittlehuang.data.jdbc.sql;

import com.github.alittlehuang.data.metamodel.EntityInformation;

/**
 * @author ALittleHuang
 */
public interface EntityInformationFactory {

    /**
     * get an instance
     *
     * @param entityType
     * @param <T>
     * @return
     */
    <T> EntityInformation<T, ?> getEntityInfo(Class<T> entityType);

}
