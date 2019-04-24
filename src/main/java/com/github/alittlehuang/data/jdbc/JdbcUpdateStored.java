package com.github.alittlehuang.data.jdbc;

import com.github.alittlehuang.data.jdbc.sql.BatchInsertPrecompiledSql;
import com.github.alittlehuang.data.jdbc.sql.BatchUpdatePrecompiledSql;
import com.github.alittlehuang.data.metamodel.Attribute;
import com.github.alittlehuang.data.metamodel.EntityInformation;
import com.github.alittlehuang.data.metamodel.support.EntityInformationImpl;
import com.github.alittlehuang.data.update.UpdateStored;
import com.github.alittlehuang.data.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings( "Duplicates" )
public class JdbcUpdateStored<T> implements UpdateStored<T> {

    private static final int[] EMPTY_INT_ARRAY = new int[0];
    private final JdbcSqlActuator sqlActuator;
    private final Class<T> entityType;

    public JdbcUpdateStored(JdbcSqlActuator sqlActuator, Class<T> entityType) {
        this.sqlActuator = sqlActuator;
        this.entityType = entityType;
    }

    @Override
    public int update(T entity) {
        return update(Collections.singletonList(entity))[0];
    }

    @Override
    public T insert(T entity) {
        insert(Collections.singletonList(entity));
        return entity;
    }

    @Override
    public int[] update(Iterable<T> entities) {
        Assert.notNull(entities, "entity must not be null");
        Iterator<T> iterator = entities.iterator();
        if ( !iterator.hasNext() ) {
            return EMPTY_INT_ARRAY;
        }
        UpdateSlqBuilder updateSlqBuilder = new UpdateSlqBuilder(entities, entityType);
        return sqlActuator.update(updateSlqBuilder.updateSql());
    }

    @Override
    public <X extends Iterable<T>> X insert(X entities) {
        Assert.notNull(entities, "entity must not be null");

        Iterator<T> iterator = entities.iterator();
        if ( !iterator.hasNext() ) {
            return entities;
        }

        BatchInsertPrecompiledSql precompiledSql = new UpdateSlqBuilder(entities, entityType).insertSql();

        return sqlActuator.insert(precompiledSql, resultSet -> {
            while ( resultSet.next() ) {
                Attribute<T, Object> idAttribute = EntityInformationImpl.getInstance(entityType).getIdAttribute();
                Object object = JdbcUtil.getValue(resultSet, 1, idAttribute.getJavaType());
                idAttribute.setValue(iterator.next(), object);
            }
            return entities;
        });

    }

    private class UpdateSlqBuilder {
        StringBuilder sql;
        private List<List<?>> args;

        final Iterable<T> entities;
        final Class<T> type;

        UpdateSlqBuilder(Iterable<T> entities, Class<T> type) {
            this.entities = entities;
            this.type = type;
        }

        private BatchUpdatePrecompiledSql updateSql() {
            sql = new StringBuilder();
            EntityInformation<T, Object> ef = EntityInformationImpl.getInstance(type);

            Attribute<T, ?> idAttribute = ef.getIdAttribute();
            ArrayList<Data> dataList = new ArrayList<>();
            for ( T entity : entities ) {
                Object idValue = idAttribute.getValue(entity);
                Assert.notNull(idValue, "id attribute value must not be null");
                Data data = new Data();
                data.entity = entity;
                data.idValue = idValue;
                dataList.add(data);
            }
            List<? extends Attribute<T, ?>> list = ef.getBasicUpdatableAttributes();
            Assert.notEmpty(list, "basic updatable attributes must not be empty");

            sql.append("UPDATE `").append(ef.getTableName()).append("` SET ");
            boolean first = true;
            for ( Attribute<T, ?> attribute : list ) {
                if ( first ) {
                    first = false;
                } else {
                    sql.append(",");
                }
                sql.append("`").append(attribute.getColumnName()).append("`").append("=?");
                setParams(dataList, attribute);
            }
            sql.append(" WHERE ").append("`").append(idAttribute.getColumnName()).append("`").append("=?");
            for ( Data data : dataList ) {
                data.arg.add(data.idValue);
            }
            args = dataList.stream().map(it -> it.arg).collect(Collectors.toList());
            return new BatchUpdatePrecompiledSql(sql.toString(), args);
        }

        private BatchInsertPrecompiledSql insertSql() {
            sql = new StringBuilder();
            EntityInformation<T, Object> ef = EntityInformationImpl.getInstance(type);
            List<? extends Attribute<T, ?>> list = ef.getBasicInsertableAttributes();
            Assert.notEmpty(list, "basic insertable attributes must not be empty");

            ArrayList<Data> dataList = new ArrayList<>();
            for ( T entity : entities ) {
                Data data = new Data();
                data.entity = entity;
                checkVersion(ef, entity);
                dataList.add(data);
            }

            sql.append("INSERT INTO `").append(ef.getTableName()).append("` (");
            boolean first = true;
            for ( Attribute attribute : list ) {
                if ( first ) {
                    first = false;
                } else {
                    sql.append(",");
                }
                sql.append("`").append(attribute.getColumnName()).append("`");
            }
            sql.append(") VALUES (");
            first = true;
            for ( Attribute<T, ?> attribute : list ) {
                if ( first ) {
                    first = false;
                } else {
                    sql.append(",");
                }
                sql.append("?");
                setParams(dataList, attribute);
            }
            sql.append(")");
            args = dataList.stream().map(it -> it.arg).collect(Collectors.toList());
            return new BatchInsertPrecompiledSql(sql.toString(), args);
        }

        private void checkVersion(EntityInformation<T, Object> ef, T entity) {
            Attribute<T, ? extends Number> versionAttribute = ef.getVersionAttribute();
            if ( versionAttribute != null && versionAttribute.getValue(entity) == null ) {
                //noinspection unchecked
                Attribute<T, Number> attribute = (Attribute<T, Number>) versionAttribute;
                if ( versionAttribute.getJavaType() == Integer.class ) {
                    attribute.setValue(entity, 0);
                } else {
                    attribute.setValue(entity, 0L);
                }
            }
        }

        private void setParams(ArrayList<Data> dataList, Attribute<T, ?> attribute) {
            for ( Data data : dataList ) {
                data.arg.add(attribute.getValue(data.entity));
            }
        }

        class Data {
            T entity;
            Object idValue;
            List<Object> arg = new ArrayList<>();
        }
    }


}
