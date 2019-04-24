package com.github.alittlehuang.data.jdbc;

import java.sql.ResultSet;

public interface JdbcSqlActuator {

    <R> R update(ConnectionCallback<R> action);

    <R> R query(PreparedStatementCreator psc, ResultSetExtractor<R> rse);

    <R> R insert(ConnectionCallback<ResultSet> psc, ResultSetExtractor<R> rse);

}
