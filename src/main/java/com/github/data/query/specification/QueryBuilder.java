package com.github.data.query.specification;

public interface QueryBuilder<T, THIS extends QueryBuilder<T, THIS>> extends
        WhereClauseBuilder<T, THIS> {

}
