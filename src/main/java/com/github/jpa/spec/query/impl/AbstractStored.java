package com.github.jpa.spec.query.impl;

import com.github.jpa.spec.query.api.Criteria;
import com.github.jpa.spec.query.api.Stored;

public abstract class AbstractStored<T> implements Stored<T> {
    protected Criteria<T> criteria;

    void setCriteria(Criteria<T> criteria) {
        this.criteria = criteria;
    }

    protected Criteria<T> getCriteria() {
        return criteria;
    }

}
