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

    private String sql;
    private List<Object> args;

    public PrecompiledSql(String sql, List<Object> args) {
        this.sql = sql;
        this.args = args;
    }

    public String getSql(){
        return sql;
    }

    public List<Object> getArgs(){
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
        int i = 0;
        List<Object> args = getArgs();
        for ( Object arg : args ) {
            preparedStatement.setObject(++i, arg);
        }

        logSql(sql, args);
    }

    private void logSql(String sql, List<Object> args) {
        if ( logger.isDebugEnabled() ) {
            boolean hasArgs = args != null && !args.isEmpty();
            String info = ( hasArgs ? "prepared sql:\n\n" : "sql:\n\n" ) + sql + "\n";
            logger.debug(info.replaceAll("\n", "\n  "));
            if ( hasArgs ) {
                logger.debug("args: " + args.toString());
            }
        }
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
