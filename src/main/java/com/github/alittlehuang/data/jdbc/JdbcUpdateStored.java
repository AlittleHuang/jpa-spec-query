package com.github.alittlehuang.data.jdbc;

import com.github.alittlehuang.data.jdbc.sql.PrecompiledSql;
import com.github.alittlehuang.data.metamodel.Attribute;
import com.github.alittlehuang.data.metamodel.EntityInformation;
import com.github.alittlehuang.data.update.UpdateStored;
import com.github.alittlehuang.data.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JdbcUpdateStored<T> implements UpdateStored<T> {

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
        Assert.notNull(entity, "entity must not be null");
        UpdateSlqBuilder updateSlqBuilder = new UpdateSlqBuilder(entity, entityType);
        try ( Connection connection = dataSource.getConnection() ) {
            return updateSlqBuilder.updateSql().execute(connection, PreparedStatement::executeUpdate);
        } catch ( SQLException e ) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T insert(T entity) {
        Assert.notNull(entity, "entity must not be null");
        UpdateSlqBuilder updateSlqBuilder = new UpdateSlqBuilder(entity, entityType);
        try ( Connection connection = dataSource.getConnection() ) {
            ResultSet resultSet = new UpdateSlqBuilder(entity, entityType).insertSql().getGeneratedKeys(connection);
            if ( resultSet.next() ) {
                Attribute idAttribute = EntityInformation.getInstance(entityType).getIdAttribute();
                Object object = ResultSetUtil.getValue(resultSet, 1, idAttribute.getJavaType());
                idAttribute.setValue(entity, object);
            }

        } catch ( SQLException e ) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    private int update(Object entity, Class<?> type) {
        return 0;
    }

    static class UpdateSlqBuilder {
        StringBuilder sql;
        private List<Object> args;

        final Object entity;
        final Class<?> type;

        public UpdateSlqBuilder(Object entity, Class<?> type) {
            this.entity = entity;
            this.type = type;
        }

        private PrecompiledSql updateSql() {
            sql = new StringBuilder();
            args = new ArrayList<>();
            EntityInformation<?, Object> ef = EntityInformation.getInstance(type);

            Attribute idAttribute = ef.getIdAttribute();
            Object idValue = idAttribute.getValue(entity);
            Assert.notNull(idValue, "id attribute value must not be null");
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
                args.add(attribute.getValue(entity));
            }
            sql.append(" WHERE ").append("`").append(idAttribute.getColumnName()).append("`").append("=?");
            args.add(idValue);
            return new PrecompiledSql(sql.toString(), args);
        }

        private PrecompiledSql insertSql() {
            sql = new StringBuilder();
            args = new ArrayList<>();
            EntityInformation<?, Object> ef = EntityInformation.getInstance(type);

            List<Attribute> list = ef.getBasicInsertableAttributes();
            Assert.notEmpty(list, "basic insertable attributes must not be empty");

            Attribute idAttribute = ef.getIdAttribute();
            Object value = idAttribute.getValue(entity);
            if ( value == null ) {
                list = list.stream().filter(it -> it != idAttribute).collect(Collectors.toList());
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
                args.add(attribute.getValue(entity));
            }
            sql.append(")");
            return new PrecompiledSql(sql.toString(), args);
        }

    }


}
