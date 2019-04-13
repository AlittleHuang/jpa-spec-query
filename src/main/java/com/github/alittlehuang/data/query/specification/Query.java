package com.github.alittlehuang.data.query.specification;

import com.github.alittlehuang.data.query.page.Page;

/**
 * @author ALittleHuang
 */
public interface Query<T> extends BaseQuery<T, Page<T>, Query<T>> {

}
