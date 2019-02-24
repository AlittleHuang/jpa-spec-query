package com.github.jpa.spec.query.api;

public interface QueryBuilder<T, THIS extends Query<T>> extends
        WhereClauseBuilder<T, QueryBuilder<T, THIS>> {

}
