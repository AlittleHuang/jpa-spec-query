package com.github.data.query.support;

import com.github.data.query.specification.Criteria;
import com.github.data.query.specification.Stored;

public abstract class AbstractStored<T> implements Stored<T> {

    protected Criteria<T> criteria;

    void setCriteria(Criteria<T> criteria) {
        this.criteria = criteria;
    }

    protected Criteria<T> getCriteria() {
        return criteria;
    }

}
