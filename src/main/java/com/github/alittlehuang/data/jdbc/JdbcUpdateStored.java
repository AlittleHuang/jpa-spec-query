package com.github.alittlehuang.data.jdbc;

import com.github.alittlehuang.data.jdbc.sql.PrecompiledSqlBatch;
import com.github.alittlehuang.data.metamodel.Attribute;
import com.github.alittlehuang.data.metamodel.EntityInformation;
import com.github.alittlehuang.data.update.UpdateStored;
import com.github.alittlehuang.data.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class JdbcUpdateStored<T> implements UpdateStored<T> {

    public static final int[] EMPTY_INTS = new int[0];
    private final DataSource dataSource;
    private final Class<T> entityType;

    public JdbcUpdateStored(DataSource dataSource, Class<T> entityType) {
        this.dataSource = dataSource;
        this.entityType = entityType;
    }

    public DataSource getDataSource() {
        return dataSource;
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
            return EMPTY_INTS;
        }
        UpdateSlqBuilder updateSlqBuilder = new UpdateSlqBuilder(entities, entityType);

        try ( Connection connection = dataSource.getConnection() ) {
            return updateSlqBuilder.updateSql().updateBatch(connection);
        } catch ( SQLException e ) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <X extends Iterable<T>> X insert(X entities) {
        Assert.notNull(entities, "entity must not be null");

        Iterator<T> iterator = entities.iterator();
        if ( !iterator.hasNext() ) {
            return entities;
        }
        UpdateSlqBuilder updateSlqBuilder = new UpdateSlqBuilder(entities, entityType);

        try ( Connection connection = dataSource.getConnection() ) {

            ResultSet resultSet = new UpdateSlqBuilder(entities, entityType).insertSql().getGeneratedKeys(connection);
            while ( resultSet.next() ) {
                Attribute idAttribute = EntityInformation.getInstance(entityType).getIdAttribute();
                Object object = ResultSetUtil.getValue(resultSet, 1, idAttribute.getJavaType());
                idAttribute.setValue(iterator.next(), object);
            }

        } catch ( SQLException e ) {

            throw new RuntimeException(e);
        }
        return entities;
    }

    static class UpdateSlqBuilder {
        StringBuilder sql;
        private List<List<?>> args;

        final Iterable<?> entities;
        final Class<?> type;

        public UpdateSlqBuilder(Iterable<?> entities, Class<?> type) {
            this.entities = entities;
            this.type = type;
        }

        private PrecompiledSqlBatch updateSql() {
            sql = new StringBuilder();
            EntityInformation<?, Object> ef = EntityInformation.getInstance(type);

            Attribute idAttribute = ef.getIdAttribute();
            ArrayList<Data> dataList = new ArrayList<>();
            for ( Object entity : entities ) {
                Object idValue = idAttribute.getValue(entity);
                Assert.notNull(idValue, "id attribute value must not be null");
                Data data = new Data();
                data.entity = entity;
                data.idValue = idValue;
                dataList.add(data);
            }
            List<Attribute> list = ef.getBasicUpdatableAttributes();
            Assert.notEmpty(list, "basic updatable attributes must not be empty");

            sql.append("UPDATE `").append(ef.getTableName()).append("` SET ");
            boolean first = true;
            for ( Attribute attribute : list ) {
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
            return new PrecompiledSqlBatch(sql.toString(), args);
        }

        private PrecompiledSqlBatch insertSql() {
            sql = new StringBuilder();
            EntityInformation<?, Object> ef = EntityInformation.getInstance(type);

            List<Attribute> list = ef.getBasicInsertableAttributes();
            Assert.notEmpty(list, "basic insertable attributes must not be empty");

            ArrayList<Data> dataList = new ArrayList<>();
            for ( Object entity : entities ) {
                Data data = new Data();
                data.entity = entity;
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
            for ( Attribute attribute : list ) {
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
            return new PrecompiledSqlBatch(sql.toString(), args);
        }

        private void setParams(ArrayList<Data> dataList, Attribute attribute) {
            for ( Data data : dataList ) {
                data.arg.add(attribute.getValue(data.entity));
            }
        }

        class Data {
            Object entity;
            Object idValue;
            List<Object> arg = new ArrayList<>();
        }
    }


}
