package com.github.alittlehuang.data.query.specification;

public interface Query<T> extends CriteriaBuilder<T, Query<T>>, QueryStored<T> {

}
