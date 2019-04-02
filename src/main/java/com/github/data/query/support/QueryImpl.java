package com.github.data.query.support;

import com.github.data.query.specification.AttrExpression;
import com.github.data.query.specification.Query;
import com.github.data.query.specification.QueryStored;
import org.springframework.data.domain.Page;

import java.util.List;

public class QueryImpl<T> extends AbstractCriteriaBuilder<T, Query<T>> implements Query<T> {

    protected AbstractQueryStored<T> stored;

    protected QueryStored<T> getStored() {
        return stored;
    }

    @Override
    protected QueryImpl<T> createSubItem(AttrExpression<T> expression) {
        return new QueryImpl<>(expression, getWhereClause(), getCriteria(), stored);
    }

    public QueryImpl(AbstractQueryStored<T> stored) {
        super();
        stored.criteria = getCriteria();
        this.stored = stored;
    }

    protected QueryImpl(AttrExpression<T> expression, SimpleWhereClause<T> root, SimpleCriteria<T> criteria,
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
}
