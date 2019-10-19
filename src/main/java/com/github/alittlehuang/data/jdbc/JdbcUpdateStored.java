package com.github.alittlehuang.data.jdbc;

import com.github.alittlehuang.data.jdbc.operations.JdbcOperations;
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
    private final JdbcOperations sqlActuator;
    private final Class<T> entityType;

    public JdbcUpdateStored(JdbcOperations sqlActuator, Class<T> entityType) {
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
        EntityInformation<T, Object> ef = EntityInformationImpl.getInstance(entityType);
        int[] result = sqlActuator.updateBatch(updateSlqBuilder.updateSql());
        if ( ef.hasVersion() ) {
            for ( int i : result ) {
                Assert.state(i == 1, "the entity has been updated in other transactions");
            }
            for ( T entity : entities ) {
                Attribute<T, ? extends Number> versionAttribute = ef.getVersionAttribute();
                Number value = versionAttribute.getValue(entity);
                if ( value.getClass() == Long.class ) {
                    value = value.longValue() + 1;
                } else {
                    value = value.intValue() + 1;
                }
                //noinspection unchecked
                ( (Attribute<T, Number>) versionAttribute ).setValue(entity, value);
            }
        }
        return result;
    }

    @Override
    public <X extends Iterable<? extends T>> X insert(X entities) {
        Assert.notNull(entities, "entity must not be null");

        Iterator<? extends T> iterator = entities.iterator();
        if ( !iterator.hasNext() ) {
            return entities;
        }

        BatchInsertPrecompiledSql precompiledSql = new UpdateSlqBuilder(entities, entityType).insertSql();

        return sqlActuator.insertBatch(precompiledSql, resultSet -> {
            while ( resultSet.next() ) {
                Attribute<T, Object> idAttribute = EntityInformationImpl.getInstance(entityType).getIdAttribute();
                Object object = JdbcUtil.getValue(resultSet, 1, idAttribute.getJavaType());
                T next = iterator.next();
                idAttribute.setValue(next, object);
            }
            return entities;
        });

    }

    private class UpdateSlqBuilder {
        StringBuilder sql;
        private List<List<?>> args;

        final Iterable<? extends T> entities;
        final Class<T> type;

        UpdateSlqBuilder(Iterable<? extends T> entities, Class<T> type) {
            this.entities = entities;
            this.type = type;
        }

        private BatchUpdatePrecompiledSql updateSql() {
            sql = new StringBuilder();
            EntityInformation<T, Object> ef = EntityInformationImpl.getInstance(type);
            boolean hasVersion = ef.hasVersion();
            Attribute<T, ? extends Number> versionAttribute = ef.getVersionAttribute();

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
                setParams(dataList, attribute, true);
            }
            sql.append(" WHERE ").append("`").append(idAttribute.getColumnName()).append("`").append("=?");
            if ( hasVersion ) {
                sql.append(" AND ").append(versionAttribute.getColumnName()).append("=?");
            }

            Iterator<? extends T> iterator = entities.iterator();
            for ( Data data : dataList ) {
                data.arg.add(data.idValue);
                if ( hasVersion ) {
                    data.arg.add(versionAttribute.getValue(iterator.next()));
                }
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
            setParams(dataList, attribute, false);
        }

        private void setParams(ArrayList<Data> dataList, Attribute<T, ?> attribute, boolean updateVersion) {
            for ( Data data : dataList ) {
                Object value = attribute.getValue(data.entity);
                if ( updateVersion && attribute.getVersion() != null ) {
                    value = ( (Number) value ).longValue() + 1;
                }
                data.arg.add(value);
            }
        }

        class Data {
            T entity;
            Object idValue;
            List<Object> arg = new ArrayList<>();
        }
    }


}
