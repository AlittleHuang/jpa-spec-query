package com.github.alittlehuang.data.jdbc;

import com.github.alittlehuang.data.jdbc.operations.JdbcOperations;
import com.github.alittlehuang.data.jdbc.operations.JdbcOperationsInSpring;
import com.github.alittlehuang.data.jdbc.sql.EntityInformationFactory;
import com.github.alittlehuang.data.jdbc.sql.QuerySqlBuilderFactory;
import com.github.alittlehuang.data.jdbc.sql.mysql57.Mysql57QuerySqlBuilderFactory;
import com.github.alittlehuang.data.log.Logger;
import com.github.alittlehuang.data.log.LoggerFactory;
import com.github.alittlehuang.data.metamodel.support.EntityInformationImpl;
import com.github.alittlehuang.data.util.JointKey;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author ALittleHuang
 */
public class JdbcQueryStoredConfigImpl implements JdbcQueryStoredConfig {

    private static final Logger logger = LoggerFactory.getLogger(JdbcQueryStoredConfigImpl.class);
    private QuerySqlBuilderFactory querySqlBuilderFactory;
    private Map<JointKey, Function> typeConverterSet = new ConcurrentHashMap<>();
    private EntityInformationFactory entityInformationFactory;
    private JdbcOperations jdbcOperations;

    public JdbcQueryStoredConfigImpl() {
    }

    public JdbcQueryStoredConfigImpl(DataSource dataSource) {
        jdbcOperations = new JdbcOperationsInSpring(dataSource);
    }

    @Override
    public <X, Y> Function<X, Y> getTypeConverter(Class<X> srcType, Class<Y> targetType) {
        //noinspection unchecked
        return typeConverterSet.get(new JointKey(srcType, targetType));
    }

    @Override
    public <T, U> void registerTypeConverter(Class<T> srcType, Class<U> targetType, Function<T, U> converter) {
        typeConverterSet.put(new JointKey(srcType, targetType), converter);
    }

    @Override
    public QuerySqlBuilderFactory getQuerySqlBuilderFactory() {
        if ( querySqlBuilderFactory == null) {
            querySqlBuilderFactory = new Mysql57QuerySqlBuilderFactory(this);
        }
        return querySqlBuilderFactory;
    }

    @Override
    public void setQuerySqlBuilderFactory(QuerySqlBuilderFactory querySqlBuilderFactory) {
        this.querySqlBuilderFactory = querySqlBuilderFactory;
    }

    @Override
    public EntityInformationFactory getEntityInformationFactory() {
        if (entityInformationFactory == null) {
            entityInformationFactory = EntityInformationImpl::getInstance;
        }
        return entityInformationFactory;
    }

    @Override
    public void setEntityInformationFactory(EntityInformationFactory entityInformationFactory) {
        this.entityInformationFactory = entityInformationFactory;
    }

    @Override
    public JdbcOperations getJdbcOperations() {
        return jdbcOperations;
    }
}
