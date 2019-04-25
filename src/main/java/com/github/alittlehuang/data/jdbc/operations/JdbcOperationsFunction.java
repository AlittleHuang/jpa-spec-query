package com.github.alittlehuang.data.jdbc.operations;

import java.sql.SQLException;

@FunctionalInterface
public interface JdbcOperationsFunction<T, R> {

    R apply(T t) throws SQLException;

}
