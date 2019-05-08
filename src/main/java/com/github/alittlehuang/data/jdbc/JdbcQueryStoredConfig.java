package com.github.alittlehuang.data.jdbc;

import com.github.alittlehuang.data.jdbc.operations.JdbcOperations;
import com.github.alittlehuang.data.jdbc.sql.EntityInformationFactory;
import com.github.alittlehuang.data.jdbc.sql.QuerySqlBuilderFactory;

import java.util.function.Function;

/**
 * @author ALittleHuang
 */
public interface JdbcQueryStoredConfig {

    <X, Y> Function<X, Y> getTypeConverter(Class<X> srcType, Class<Y> targetType);

    <T, U> void registerTypeConverter(Class<T> srcType, Class<U> targetType, Function<T, U> converter);

    QuerySqlBuilderFactory getQuerySqlBuilderFactory();

    void setQuerySqlBuilderFactory(QuerySqlBuilderFactory querySqlBuilderFactory);

    EntityInformationFactory getEntityInformationFactory();

    void setEntityInformationFactory(EntityInformationFactory entityInformationFactory);

    JdbcOperations getJdbcOperations();

}
