package com.github.alittlehuang.data.query.specification;

import com.github.alittlehuang.data.query.page.Page;

import java.util.function.Function;

/**
 * @author ALittleHuang
 */
public interface QueryStored<T> extends BaseQueryStored<T, Page<T>> {

    default <P> P getPage(long page, long size, Function<Page<T>, P> function) {
        return function.apply(getPage(page, size));
    }

    default <P> P getPage(Function<Page<T>, P> function) {
        return function.apply(getPage());
    }

}
