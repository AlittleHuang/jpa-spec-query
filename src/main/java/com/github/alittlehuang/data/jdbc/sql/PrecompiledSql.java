package com.github.alittlehuang.data.jdbc.sql;

import com.github.alittlehuang.data.log.Logger;
import com.github.alittlehuang.data.log.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @author ALittleHuang
 */
public class PrecompiledSql {
    private static Logger logger = LoggerFactory.getLogger(PrecompiledSql.class);

    protected String sql;
    protected List<?> args;

    public PrecompiledSql(String sql, List<?> args) {
        this.sql = sql;
        this.args = args;
    }

    public String getSql(){
        return sql;
    }

    public List<?> getArgs() {
        return args;
    }

    public <R> R execute(Connection connection, PreparedStatementFunction<R> function) {
        String sql = getSql();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            setParam(preparedStatement, sql);
            return function.apply(preparedStatement);
        } catch ( SQLException e ) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet getGeneratedKeys(Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParam(preparedStatement, sql);
            preparedStatement.executeUpdate();
            return preparedStatement.getGeneratedKeys();
        } catch ( SQLException e ) {
            throw new RuntimeException(e);
        }
    }

    private void setParam(PreparedStatement preparedStatement, String sql) throws SQLException {
        List<?> args = getArgs();
        setParam(preparedStatement, sql, args);
    }

    protected void setParam(PreparedStatement preparedStatement, String sql, List<?> args) throws SQLException {
        int i = 0;
        for ( Object arg : args ) {
            preparedStatement.setObject(++i, arg);
        }
        logSql(sql, args);
    }

    protected void logSql(String sql, List<?> args) {
        if ( logger.isDebugEnabled() ) {
            boolean hasArgs = args != null && !args.isEmpty();
            StringBuilder info = new StringBuilder(( hasArgs ? "prepared sql:\n\n" : "sql:\n\n" ) + sql + "\n");
            if ( hasArgs ) {
                info.append("\nargs: ").append(args.toString()).append('\n');
            }
            logger.debug(info.toString().replaceAll("\n", "\n  "));
        }
    }

    protected void logSql() {
        logSql(sql, getArgs());
    }

    @Override
    public String toString() {
        return "PrecompiledSql{\n" +
                "  sql='" + sql + '\'' +
                "\n  args=" + args +
                "\n}";
    }

    @FunctionalInterface
    public interface PreparedStatementFunction<R> {
        R apply(PreparedStatement preparedStatement) throws SQLException;
    }

}
