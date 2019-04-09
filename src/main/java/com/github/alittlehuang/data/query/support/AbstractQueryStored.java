package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.specification.QueryStored;
import com.github.alittlehuang.data.query.specification.Criteria;

public abstract class AbstractQueryStored<T> implements QueryStored<T> {

    protected Criteria<T> criteria;

    void setCriteria(Criteria<T> criteria) {
        this.criteria = criteria;
    }

    protected Criteria<T> getCriteria() {
        return criteria;
    }

}
