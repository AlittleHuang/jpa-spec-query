package com.github.alittlehuang.data.query.specification;

/**
 * @author ALittleHuang
 */
public interface BaseQuery<T, PAGE, THIS extends BaseQuery<T, PAGE, THIS>>
        extends CriteriaBuilder<T, THIS>, BaseQueryStored<T, PAGE> {

}
