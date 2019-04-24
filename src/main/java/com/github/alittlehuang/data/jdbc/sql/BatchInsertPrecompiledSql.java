package com.github.alittlehuang.data.jdbc.sql;

import com.github.alittlehuang.data.jdbc.ConnectionCallback;
import com.github.alittlehuang.data.jdbc.JdbcUtil;
import com.github.alittlehuang.data.log.Logger;
import com.github.alittlehuang.data.log.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @author ALittleHuang
 */
public class BatchInsertPrecompiledSql extends AbstractPrecompiledSql
        implements ConnectionCallback<ResultSet> {
    private static Logger logger = LoggerFactory.getLogger(BatchInsertPrecompiledSql.class);

    public BatchInsertPrecompiledSql(String sql, List<List<?>> args) {
        super(sql, args);
    }

    @Override
    public List<List<?>> getArgs() {
        //noinspection unchecked
        return (List<List<?>>) super.getArgs();
    }

    @Override
    public ResultSet doInConnection(Connection con) throws SQLException {
        PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        List<List<?>> args = getArgs();
        for ( List<?> arg : args ) {
            JdbcUtil.setParam(preparedStatement, sql, arg);
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        preparedStatement.clearBatch();
        return resultSet;
    }
}
