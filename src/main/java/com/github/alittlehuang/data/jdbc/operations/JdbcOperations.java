package com.github.alittlehuang.data.jdbc.operations;

public interface JdbcOperations {

    int[] updateBatch(JdbcOperationsCallback operations);

    int update(JdbcOperationsCallback operations);

    <R> R query(JdbcOperationsCallback operations, ResultSetExtractor<R> rse);

    <R> R insert(JdbcOperationsCallback operations, ResultSetExtractor<R> rse);

    <R> R insertBatch(JdbcOperationsCallback operations, ResultSetExtractor<R> rse);

}
