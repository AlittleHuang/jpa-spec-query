package com.github.jpa.spec.query.api;

public interface QueryBuilder<T, THIS extends QueryBuilder<T, THIS>> extends
        WhereClauseBuilder<T, THIS> {

}
