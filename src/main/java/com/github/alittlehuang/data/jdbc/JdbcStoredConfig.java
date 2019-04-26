package com.github.alittlehuang.data.jdbc;

import com.github.alittlehuang.data.jdbc.operations.JdbcOperationsInSpring;
import com.github.alittlehuang.data.jdbc.sql.EntityInformationFactory;
import com.github.alittlehuang.data.jdbc.sql.SqlBuilderFactory;
import com.github.alittlehuang.data.jdbc.sql.mysql57.Mysql57SqlBuilderFactory;
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
public class JdbcStoredConfig extends JdbcOperationsInSpring {

    private static final Logger logger = LoggerFactory.getLogger(JdbcStoredConfig.class);
    private SqlBuilderFactory sqlBuilderFactory;
    private Map<JointKey, Function> typeConverterSet = new ConcurrentHashMap<>();
    private EntityInformationFactory entityInformationFactory;

    public JdbcStoredConfig() {
    }

    public JdbcStoredConfig(DataSource dataSource) {
        super(dataSource);
    }

    public <X, Y> Function<X, Y> getTypeConverter(Class<X> srcType, Class<Y> targetType) {
        //noinspection unchecked
        return typeConverterSet.get(new JointKey(srcType, targetType));
    }

    public <T, U> void registerTypeConverter(Class<T> srcType, Class<U> targetType, Function<T, U> converter) {
        typeConverterSet.put(new JointKey(srcType, targetType), converter);
    }


    public SqlBuilderFactory getSqlBuilderFactory() {
        if (sqlBuilderFactory == null) {
            sqlBuilderFactory = new Mysql57SqlBuilderFactory(this);
        }
        return sqlBuilderFactory;
    }

    public void setSqlBuilderFactory(SqlBuilderFactory sqlBuilderFactory) {
        this.sqlBuilderFactory = sqlBuilderFactory;
    }

    public EntityInformationFactory getEntityInformationFactory() {
        if (entityInformationFactory == null) {
            entityInformationFactory = EntityInformationImpl::getInstance;
        }
        return entityInformationFactory;
    }

    public void setEntityInformationFactory(EntityInformationFactory entityInformationFactory) {
        this.entityInformationFactory = entityInformationFactory;
    }


}
