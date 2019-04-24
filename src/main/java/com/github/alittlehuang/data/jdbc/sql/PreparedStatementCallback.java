package com.github.alittlehuang.data.jdbc.sql;

import org.springframework.dao.DataAccessException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementCallback<T> {
    T doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException;
}
