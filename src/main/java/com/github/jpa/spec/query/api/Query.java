package com.github.jpa.spec.query.api;

public interface Query<T> extends QueryBuilder<T, Query<T>>, CriteriaBuilder<T, Query<T>>, Stored<T> {


}
