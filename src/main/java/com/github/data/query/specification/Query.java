package com.github.data.query.specification;

public interface Query<T> extends CriteriaBuilder<T, Query<T>>, QueryStored<T> {

}
