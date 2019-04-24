package com.github.alittlehuang.data.jdbc;

import com.github.alittlehuang.data.log.Logger;
import com.github.alittlehuang.data.log.LoggerFactory;
import lombok.Getter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcSqlActuatorImpl implements JdbcSqlActuator {
    private static final Logger logger = LoggerFactory.getLogger(JdbcSqlActuatorImpl.class);
    protected static final boolean RELY_ON_SPRING_JDBC;

    @Getter
    private DataSource dataSource;
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

    public JdbcSqlActuatorImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        if ( RELY_ON_SPRING_JDBC ) {
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }
    }

    public JdbcSqlActuatorImpl() {
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

    private <R> R execute(ConnectionCallback<R> action, boolean commit) {
        if ( jdbcTemplate != null ) {
            return jdbcTemplate.execute(action::doInConnection);
        }
        try ( Connection connection = getConnection() ) {
            R result = action.doInConnection(connection);
            if ( commit && !connection.getAutoCommit() ) {
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
