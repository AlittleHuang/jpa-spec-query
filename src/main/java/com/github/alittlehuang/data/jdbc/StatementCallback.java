package com.github.alittlehuang.data.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.lang.Nullable;

import java.sql.SQLException;
import java.sql.Statement;

@FunctionalInterface
public interface StatementCallback<T> {

    @Nullable
    T doInStatement(Statement stmt) throws SQLException, DataAccessException;

}
