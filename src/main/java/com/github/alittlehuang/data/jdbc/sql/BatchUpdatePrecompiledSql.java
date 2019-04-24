package com.github.alittlehuang.data.jdbc.sql;

import com.github.alittlehuang.data.jdbc.ConnectionCallback;
import com.github.alittlehuang.data.jdbc.JdbcUtil;
import com.github.alittlehuang.data.log.Logger;
import com.github.alittlehuang.data.log.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ALittleHuang
 */
public class BatchUpdatePrecompiledSql
        extends AbstractPrecompiledSql
        implements ConnectionCallback<int[]> {
    private static Logger logger = LoggerFactory.getLogger(BatchUpdatePrecompiledSql.class);

    public BatchUpdatePrecompiledSql(String sql, List<List<?>> args) {
        super(sql, args);
    }

    @Override
    public List<List<?>> getArgs() {
        //noinspection unchecked
        return (List<List<?>>) super.getArgs();
    }


    @Override
    public int[] doInConnection(Connection con) throws SQLException {
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        List<List<?>> args = getArgs();
        for ( List<?> arg : args ) {
            JdbcUtil.setParam(preparedStatement, sql, arg);
            preparedStatement.addBatch();
        }
        return preparedStatement.executeBatch();
    }
}
