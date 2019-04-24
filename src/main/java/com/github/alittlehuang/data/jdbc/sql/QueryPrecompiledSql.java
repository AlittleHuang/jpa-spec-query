package com.github.alittlehuang.data.jdbc.sql;

import com.github.alittlehuang.data.jdbc.JdbcUtil;
import com.github.alittlehuang.data.jdbc.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ALittleHuang
 */
public class QueryPrecompiledSql extends AbstractPrecompiledSql implements PreparedStatementCreator {

    public QueryPrecompiledSql(String sql, List<?> args) {
        super(sql, args);
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        String sql = getSql();
        List<?> args = getArgs();
        PreparedStatement stmt = con.prepareStatement(sql);
        JdbcUtil.setParam(stmt, sql, args);
        return stmt;
    }
}
