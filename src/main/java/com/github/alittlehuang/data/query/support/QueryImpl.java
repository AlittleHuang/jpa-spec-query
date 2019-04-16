package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.page.Page;
import com.github.alittlehuang.data.query.specification.Expression;
import com.github.alittlehuang.data.query.specification.Query;

/**
 * @author ALittleHuang
 */
public class QueryImpl<T> extends AbstractQuery<T, Page<T>, Query<T>> implements Query<T> {

    public QueryImpl(AbstractQueryStored<T, Page<T>> stored) {
        super(stored);
    }

}
