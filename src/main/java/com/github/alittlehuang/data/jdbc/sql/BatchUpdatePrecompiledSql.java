package com.github.alittlehuang.data.jdbc.sql;

import com.github.alittlehuang.data.jdbc.JdbcUtil;
import com.github.alittlehuang.data.log.Logger;
import com.github.alittlehuang.data.log.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ALittleHuang
 */
public class BatchUpdatePrecompiledSql extends PrecompiledSql {
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
    public void logSql() {
        List<List<?>> args = getArgs();
        String sql = getSql();
        for ( List<?> arg : args ) {
            JdbcUtil.logSql(sql, arg);
        }
    }

    @Override
    public void setValues(PreparedStatement ps) throws SQLException {
        List<List<?>> args = getArgs();
        for ( List<?> arg : args ) {
            JdbcUtil.setParam(ps, arg);
            ps.addBatch();
        }
    }
}
