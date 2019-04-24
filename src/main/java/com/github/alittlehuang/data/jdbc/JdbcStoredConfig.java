package com.github.alittlehuang.data.jdbc;

import com.github.alittlehuang.data.jdbc.sql.EntityInformationFactory;
import com.github.alittlehuang.data.jdbc.sql.SqlBuilderFactory;
import com.github.alittlehuang.data.jdbc.sql.mysql57.Mysql57SqlBuilderFactory;
import com.github.alittlehuang.data.log.Logger;
import com.github.alittlehuang.data.log.LoggerFactory;
import com.github.alittlehuang.data.metamodel.support.EntityInformationImpl;
import com.github.alittlehuang.data.util.JointKey;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author ALittleHuang
 */
public class JdbcStoredConfig implements JdbcSqlActuator {//sql执行器

    private static final Logger logger = LoggerFactory.getLogger(JdbcStoredConfig.class);
    private static final boolean RELY_ON_SPRING_JDBC;

    private DataSource dataSource;
    private SqlBuilderFactory sqlBuilderFactory;
    private Map<JointKey, Function> typeConverterSet = new ConcurrentHashMap<>();
    private EntityInformationFactory entityInformationFactory;
    private JdbcTemplate jdbcTemplate;

    static {
        boolean result = false;
        try {
            Class.forName("org.springframework.jdbc.datasource.DataSourceUtils");
            result = true;
        } catch ( ClassNotFoundException e ) {
            logger.info(e.getMessage());
        }
        RELY_ON_SPRING_JDBC = result;
    }

    public JdbcStoredConfig() {

    }

    public JdbcStoredConfig(DataSource dataSource) {
        this.dataSource = dataSource;
        if ( RELY_ON_SPRING_JDBC ) {
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }
    }

    public <X, Y> Function<X, Y> getTypeConverter(Class<X> srcType, Class<Y> targetType) {
        //noinspection unchecked
        return typeConverterSet.get(new JointKey(srcType, targetType));
    }

    public <T, U> void registerTypeConverter(Class<T> srcType, Class<U> targetType, Function<T, U> converter) {
        typeConverterSet.put(new JointKey(srcType, targetType), converter);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
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

    private Connection getConnection() {
        DataSource dataSource = getDataSource();
        if ( RELY_ON_SPRING_JDBC ) {
            return DataSourceUtils.getConnection(dataSource);
        }
        try {
            return dataSource.getConnection();
        } catch ( SQLException e ) {
            throw new RuntimeException(e);
        }
    }

    public <R> R execute(ConnectionCallback<R> action) {
        return execute(action, false);
    }

    public <R> R execute(ConnectionCallback<R> action, boolean commit) {
        if ( jdbcTemplate != null ) {
            return jdbcTemplate.execute(action::doInConnection);
        }
        try ( Connection connection = getConnection() ) {
            R result = action.doInConnection(connection);
            if ( commit ) {
                connection.commit();
            }
            return result;
        } catch ( SQLException e ) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <R> R update(ConnectionCallback<R> action) {
        return execute(action, true);
    }

    @Override
    public <R> R query(PreparedStatementCreator psc, ResultSetExtractor<R> rse) {
        return execute(con -> {
            try (
                    PreparedStatement statement = psc.createPreparedStatement(con);
                    ResultSet resultSet = statement.executeQuery()
            ) {
                R result = rse.extractData(resultSet);
                statement.close();
                return result;
            }
        });
    }

    @Override
    public <R> R insert(ConnectionCallback<ResultSet> psc, ResultSetExtractor<R> rse) {
        return execute(con -> {
            ResultSet resultSet = psc.doInConnection(con);
            return rse.extractData(resultSet);
        }, true);
    }

}
