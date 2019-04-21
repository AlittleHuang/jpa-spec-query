package com.github.alittlehuang.data.jdbc;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;

public abstract class ResultSetUtil {

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

}
