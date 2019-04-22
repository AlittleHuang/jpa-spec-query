package com.github.alittlehuang.data.jdbc.sql;

import com.github.alittlehuang.data.log.Logger;
import com.github.alittlehuang.data.log.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @author ALittleHuang
 */
public class PrecompiledSqlBatch extends PrecompiledSql {
    private static Logger logger = LoggerFactory.getLogger(PrecompiledSqlBatch.class);

    public PrecompiledSqlBatch(String sql, List<List<?>> args) {
        super(sql, args);
    }

    @Override
    public List<List<?>> getArgs() {
        //noinspection unchecked
        return (List<List<?>>) super.getArgs();
    }


    @Override
    public ResultSet getGeneratedKeys(Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            List<List<?>> args = getArgs();
            for ( List<?> arg : args ) {
                setParam(preparedStatement, sql, arg);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            preparedStatement.clearBatch();
            return resultSet;
        } catch ( SQLException e ) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <R> R execute(Connection connection, PreparedStatementFunction<R> function) {
        throw new UnsupportedOperationException();
    }

    public int[] updateBatch(Connection connection) {
        String sql = getSql();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            List<List<?>> args = getArgs();
            for ( List<?> arg : args ) {
                setParam(preparedStatement, sql, arg);
                preparedStatement.addBatch();
            }
            return preparedStatement.executeBatch();
        } catch ( SQLException e ) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void logSql() {
        List<List<?>> args = getArgs();
        for ( List<?> arg : args ) {
            logSql(sql, arg);
        }
    }
}
