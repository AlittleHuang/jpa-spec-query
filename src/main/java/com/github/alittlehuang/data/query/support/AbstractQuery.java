package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.page.Page;
import com.github.alittlehuang.data.query.specification.Expression;
import com.github.alittlehuang.data.query.specification.Query;
import com.github.alittlehuang.data.query.specification.QueryStored;

import java.util.List;

/**
 * @author ALittleHuang
 */
public abstract class AbstractQuery<T> extends AbstractCriteriaBuilder<T, Query<T>> implements Query<T> {

    protected AbstractQueryStored<T> stored;

    protected QueryStored<T> getStored() {
        return stored;
    }

    public AbstractQuery(AbstractQueryStored<T> stored) {
        super(stored.getJavaType());
        stored.criteria = getCriteria();
        this.stored = stored;
    }

    protected AbstractQuery(Expression<T> expression, SimpleWhereClause<T> root, SimpleCriteria<T> criteria,
                            AbstractQueryStored<T> stored) {
        super(expression, root, criteria);
        stored.criteria = criteria;
        this.stored = stored;
    }

    @Override
    public List<T> getResultList() {
        return getStored().getResultList();
    }

    @Override
    public T getSingleResult() {
        return getStored().getSingleResult();
    }

    @Override
    public <X> List<X> getObjectList() {
        return getStored().getObjectList();
    }

    @Override
    public <X> X getSingleObject() {
        return getStored().getSingleObject();
    }

    @Override
    public Page<T> getPage(long page, long size) {
        return getStored().getPage(page, size);
    }

    @Override
    public Page<T> getPage() {
        return getStored().getPage();
    }

    @Override
    public long count() {
        return getStored().count();
    }

    @Override
    public boolean exists() {
        return getStored().exists();
    }

    @Override
    public Class<T> getJavaType() {
        return getStored().getJavaType();
    }
}
