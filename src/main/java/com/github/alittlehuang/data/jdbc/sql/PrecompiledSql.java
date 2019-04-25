package com.github.alittlehuang.data.jdbc.sql;

import com.github.alittlehuang.data.jdbc.JdbcUtil;
import com.github.alittlehuang.data.jdbc.operations.JdbcOperationsCallback;
import com.github.alittlehuang.data.log.Logger;
import com.github.alittlehuang.data.log.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ALittleHuang
 */
public class PrecompiledSql<T> implements JdbcOperationsCallback {
    private static Logger logger = LoggerFactory.getLogger(PrecompiledSql.class);

    protected String sql;
    protected List<?> args;

    public PrecompiledSql(String sql, List<Object> args) {
        this.sql = sql;
        this.args = args;
    }

    public String getSql() {
        return sql;
    }

    public List<?> getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return "QueryPrecompiledSql{\n" +
                "  sql='" + sql + '\'' +
                "\n  args=" + args +
                "\n}";
    }

    public void logSql() {
        JdbcUtil.logSql(getSql(), getArgs());
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        return con.prepareStatement(getSql());
    }

    @Override
    public void setValues(PreparedStatement ps) throws SQLException {
        JdbcUtil.setParam(ps, getArgs());
    }

}
