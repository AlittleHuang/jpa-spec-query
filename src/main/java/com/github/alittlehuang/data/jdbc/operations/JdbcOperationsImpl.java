package com.github.alittlehuang.data.jdbc.operations;

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

public class JdbcOperationsImpl implements JdbcOperations {
    private static final Logger logger = LoggerFactory.getLogger(JdbcOperationsImpl.class);
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

    public JdbcOperationsImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        if ( RELY_ON_SPRING_JDBC ) {
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }
    }

    public JdbcOperationsImpl() {
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
    public int[] updateBatch(JdbcOperationsCallback operations) {
        return execute(con -> {
            try ( PreparedStatement stmt = operations.createPreparedStatement(con) ) {
                operations.setValues(stmt);
                return stmt.executeBatch();
            }
        }, true);
    }

    @Override
    public int update(JdbcOperationsCallback operations) {
        return execute(con -> {
            try ( PreparedStatement stmt = operations.createPreparedStatement(con) ) {
                operations.setValues(stmt);
                return stmt.executeUpdate();
            }
        }, true);
    }

    @Override
    public <R> R query(JdbcOperationsCallback operations, ResultSetExtractor<R> rse) {
        return execute(con -> {
            try ( PreparedStatement stmt = operations.createPreparedStatement(con) ) {
                operations.setValues(stmt);
                try ( ResultSet rs = stmt.executeQuery() ) {
                    return rse.extractData(rs);
                }
            }
        }, false);
    }

    @Override
    public <R> R insert(JdbcOperationsCallback operations, ResultSetExtractor<R> rse) {
        return execute(con -> {
            try ( PreparedStatement stmt = operations.createPreparedStatement(con) ) {
                operations.setValues(stmt);
                stmt.executeUpdate();
                try ( ResultSet rs = stmt.getGeneratedKeys() ) {
                    return rse.extractData(rs);
                }
            }
        }, true);
    }

    @Override
    public <R> R insertBatch(JdbcOperationsCallback operations, ResultSetExtractor<R> rse) {
        return execute(con -> {
            try ( PreparedStatement stmt = operations.createPreparedStatement(con) ) {
                operations.setValues(stmt);
                stmt.executeBatch();
                try ( ResultSet rs = stmt.getGeneratedKeys() ) {
                    return rse.extractData(rs);
                }
            }
        }, true);
    }
}
