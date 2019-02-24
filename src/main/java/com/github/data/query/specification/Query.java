package com.github.data.query.specification;

public interface Query<T> extends QueryBuilder<T, Query<T>>, CriteriaBuilder<T, Query<T>>, Stored<T> {

}
