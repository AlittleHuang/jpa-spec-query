package com.github.alittlehuang.data.query.specification;

/**
 * @author ALittleHuang
 */
public interface Query<T> extends CriteriaBuilder<T, Query<T>>, QueryStored<T> {

}
