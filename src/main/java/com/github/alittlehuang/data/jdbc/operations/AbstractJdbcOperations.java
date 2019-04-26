package com.github.alittlehuang.data.jdbc.operations;

import com.github.alittlehuang.data.log.Logger;
import com.github.alittlehuang.data.log.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class AbstractJdbcOperations implements JdbcOperations {
    private static final Logger logger = LoggerFactory.getLogger(AbstractJdbcOperations.class);

    protected abstract <R> R execute(ConnectionCallback<R> action, boolean commit);

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

    private static boolean isRelyOnSpringJdbc() {
        boolean relyOnSpringJdbc = false;
        try {
            Class.forName("org.springframework.jdbc.core.JdbcTemplate");
            relyOnSpringJdbc = true;
        } catch ( ClassNotFoundException e ) {
            logger.info(e.getMessage());
        }
        return relyOnSpringJdbc;
    }
}
