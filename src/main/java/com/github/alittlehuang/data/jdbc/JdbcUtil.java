package com.github.alittlehuang.data.jdbc;

import com.github.alittlehuang.data.log.Logger;
import com.github.alittlehuang.data.log.LoggerFactory;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class JdbcUtil {

    private static Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

    public static <X> X getValue(ResultSet resultSet, int index, Class<X> targetType) throws SQLException {
        Object val = null;
        if ( targetType == Byte.class ) {
            val = resultSet.getByte(index);
        } else if ( targetType == Short.class ) {
            val = resultSet.getShort(index);
        } else if ( targetType == Integer.class ) {
            val = resultSet.getInt(index);
        } else if ( targetType == Float.class ) {
            val = resultSet.getFloat(index);
        } else if ( targetType == Long.class ) {
            val = resultSet.getLong(index);
        } else if ( targetType == Double.class ) {
            val = resultSet.getDouble(index);
        } else if ( targetType == BigDecimal.class ) {
            val = resultSet.getBigDecimal(index);
        } else if ( targetType == Boolean.class ) {
            val = resultSet.getBoolean(index);
        } else if ( targetType == Date.class ) {
            val = resultSet.getDate(index);
        } else if ( targetType == String.class ) {
            val = resultSet.getString(index);
        } else if ( targetType == Time.class ) {
            val = resultSet.getTime(index);
        }
        //noinspection unchecked
        return (X) val;
    }


    /**
     * Close the given JDBC ResultSet and ignore any thrown exception.
     * This is useful for typical finally blocks in manual JDBC code.
     *
     * @param rs the JDBC ResultSet to close (may be {@code null})
     */
    public static void closeResultSet(@Nullable ResultSet rs) {
        if ( rs != null ) {
            try {
                rs.close();
            } catch ( SQLException ex ) {
                logger.trace("Could not close JDBC ResultSet", ex);
            } catch ( Throwable ex ) {
                // We don't trust the JDBC driver: It might throw RuntimeException or Error.
                logger.trace("Unexpected exception on closing JDBC ResultSet", ex);
            }
        }
    }

    public static void closeStatement(@Nullable Statement stmt) {
        if ( stmt != null ) {
            try {
                stmt.close();
            } catch ( SQLException ex ) {
                logger.trace("Could not close JDBC Statement", ex);
            } catch ( Throwable ex ) {
                // We don't trust the JDBC driver: It might throw RuntimeException or Error.
                logger.trace("Unexpected exception on closing JDBC Statement", ex);
            }
        }
    }

    public static void closeConnection(@Nullable Connection con) {
        if (con != null) {
            try {
                con.close();
            }
            catch (SQLException ex) {
                logger.debug("Could not close JDBC Connection", ex);
            }
            catch (Throwable ex) {
                // We don't trust the JDBC driver: It might throw RuntimeException or Error.
                logger.debug("Unexpected exception on closing JDBC Connection", ex);
            }
        }
    }

    public static void setParam(PreparedStatement preparedStatement, String sql, List<?> args) throws SQLException {
        int i = 0;
        for ( Object arg : args ) {
            preparedStatement.setObject(++i, arg);
        }
        logSql(sql, args);
    }

    private static void logSql(String sql, List<?> args) {
        if ( logger.isDebugEnabled() ) {
            boolean hasArgs = args != null && !args.isEmpty();
            StringBuilder info = new StringBuilder(( hasArgs ? "prepared sql:\n\n" : "sql:\n\n" ) + sql + "\n");
            if ( hasArgs ) {
                info.append("\nargs: ").append(args.toString()).append('\n');
            }
            logger.debug(info.toString().replaceAll("\n", "\n  "));
        }
    }

}
