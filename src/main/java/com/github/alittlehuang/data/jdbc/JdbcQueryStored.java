package com.github.alittlehuang.data.jdbc;

import com.github.alittlehuang.data.jdbc.sql.PrecompiledSql;
import com.github.alittlehuang.data.jdbc.sql.PrecompiledSqlForEntity;
import com.github.alittlehuang.data.jdbc.sql.SelectedAttribute;
import com.github.alittlehuang.data.jdbc.sql.SqlBuilderFactory;
import com.github.alittlehuang.data.log.Logger;
import com.github.alittlehuang.data.log.LoggerFactory;
import com.github.alittlehuang.data.metamodel.Attribute;
import com.github.alittlehuang.data.metamodel.EntityInformation;
import com.github.alittlehuang.data.query.page.PageFactory;
import com.github.alittlehuang.data.query.page.Pageable;
import com.github.alittlehuang.data.query.specification.Selection;
import com.github.alittlehuang.data.query.support.AbstractQueryStored;
import com.github.alittlehuang.data.util.JointKey;
import lombok.Getter;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

public class JdbcQueryStored<T, P> extends AbstractQueryStored<T, P> {
    private static Logger logger = LoggerFactory.getLogger(JdbcQueryStored.class);

    @Getter
    private JdbcQueryStoredConfig config;
    private Class<T> entityType;

    public JdbcQueryStored(JdbcQueryStoredConfig config, Class<T> entityType, PageFactory<T, P> factory) {
        super(factory);
        this.config = config;
        this.entityType = entityType;
    }

    @Override
    public List<T> getResultList() {
        PrecompiledSqlForEntity<T> precompiledSql = getSqlBuilder().listEntityResult();
        try ( Connection connection = config.getDataSource().getConnection() ) {
            ResultSet resultSet = getResultSet(connection, precompiledSql);
            return toList(resultSet, precompiledSql.getSelections());
        } catch ( SQLException e ) {
            throw new RuntimeException(e);
        }
    }

    private SqlBuilderFactory.SqlBuilder<T> getSqlBuilder() {
        return config.getSqlBuilderFactory().createSqlBuild(getCriteria());
    }

    private Map<JointKey,Object> entityMap = new HashMap<>();

    private List<T> toList(ResultSet resultSet, List<SelectedAttribute> selectedAttributes) throws SQLException {
        List<T> results = new ArrayList<>();
        boolean firstRow = true;
        while ( resultSet.next() ) {
            T entity;
            try {
                entity = entityType.newInstance();
            } catch ( InstantiationException | IllegalAccessException e ) {
                throw new RuntimeException(e);
            }
            results.add(entity);
            int index = 0;
            HashMap<JointKey, Object> instanceMap = new HashMap<>();
            instanceMap.put(asKey(null), entity);
            for ( SelectedAttribute selectedAttribute : selectedAttributes ) {
                Object val = resultSet.getObject(++index);
                Attribute attribute = selectedAttribute.getAttribute();
                Class<?> targetType = attribute.getJavaType();
                if ( val != null ) {
                    if ( !targetType.isInstance(val) ) {
                        Class<?> valType = val.getClass();
                        Function function = config.getTypeConverter(valType, targetType);
                        if ( function != null ) {
                            //noinspection unchecked
                            val = function.apply(val);
                        } else {
                            Object value = ResultSetUtil.getValue(resultSet, index, targetType);
                            if ( value != null ) {
                                val = value;
                            }
                            // type mismatch
                            else {

                                if ( firstRow && logger.isWarnEnabled() ) {
                                    Class<?> entityType = attribute.getEntityType();
                                    EntityInformation<?, ?> information = EntityInformation.getInstance(entityType);
                                    Field field = attribute.getField();

                                    logger.warn("the type " + information.getTableName() + "." + attribute.getColumnName() +
                                            " in the database does not match " + field.getDeclaringClass().getTypeName() + "."
                                            + field.getName());
                                }

                            }
                        }
                    }
                    Object entityAttr = getInstance(instanceMap, selectedAttribute.getParent());
                    attribute.setValue(entityAttr, val);
                }
            }
            firstRow = false;
        }
        return results;
    }

    private Object getInstance(Map<JointKey, Object> map, SelectedAttribute selected) {

        JointKey key = asKey(selected);
        if ( map.containsKey(key) ) {
            return map.get(key);
        }

        try {
            Object parentInstance = getInstance(map, selected.getParent());
            Attribute attribute = selected.getAttribute();
            Object val = attribute.getJavaType().newInstance();
            attribute.setValue(parentInstance, val);
            map.put(key, val);
            return val;
        } catch ( InstantiationException | IllegalAccessException e ) {
            throw new RuntimeException(e);
        }

    }

    private JointKey asKey(SelectedAttribute selected) {
        return selected == null ? null : new JointKey(selected.getAttribute(), selected.getParentAttribute());
    }

    @Override
    public <X> List<X> getObjectList() {

        List<? extends Selection<T>> selections = criteria.getSelections();
        if ( selections == null || selections.isEmpty() ) {
            //noinspection unchecked
            return (List<X>) getResultList();
        }

        PrecompiledSql precompiledSql = getSqlBuilder().listObjectResult();
        int columnsCount = selections.size();
        List<Object> result = new ArrayList<>();
        try {
            ResultSet resultSet = getResultSet(config.getDataSource().getConnection(), precompiledSql);
            while ( resultSet.next() ) {
                if ( columnsCount == 1 ) {
                    result.add(resultSet.getObject(1));
                } else {
                    Object[] row = new Object[columnsCount];
                    for ( int i = 0; i < columnsCount; i++ ) {
                        row[i] = resultSet.getObject(i + 1);
                    }
                    result.add(row);
                }
            }
        } catch ( SQLException e ) {
            throw new RuntimeException(e);
        }
        //noinspection unchecked
        return (List<X>) result;
    }

    @Override
    public P getPage(long page, long size) {
        long count = count();
        Pageable pageable = new Pageable(page, size);
        List<T> content;
        if ( count == 0 ) {
            content = Collections.emptyList();
        } else {
            try ( Connection connection = config.getDataSource().getConnection() ) {

                PrecompiledSqlForEntity<T> precompiledSql = config.getSqlBuilderFactory()
                        .createSqlBuild(getCriteria(), pageable)
                        .listEntityResult();

                ResultSet resultSet = getResultSet(connection, precompiledSql);
                content = toList(resultSet, precompiledSql.getSelections());
            } catch ( SQLException e ) {
                throw new RuntimeException(e);
            }
        }
        return getPageFactory().get(page, size, content, count);
    }

    @Override
    public long count() {
        PrecompiledSql count = getSqlBuilder().count();

        try ( Connection connection = config.getDataSource().getConnection() ) {
            ResultSet resultSet = getResultSet(connection, count);
            if ( resultSet.next() ) {
                return resultSet.getLong(1);
            } else {
                return 0L;
            }
        } catch ( SQLException e ) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean exists() {
        PrecompiledSql precompiledSql = getSqlBuilder().exists();
        try ( Connection connection = config.getDataSource().getConnection() ) {
            ResultSet resultSet = getResultSet(connection, precompiledSql);
            return resultSet.next();
        } catch ( SQLException e ) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<T> getJavaType() {
        return entityType;
    }

    private ResultSet getResultSet(Connection connection, PrecompiledSql precompiledSql) {
        return precompiledSql.execute(connection, PreparedStatement::executeQuery);
    }

}