package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.specification.BaseQuery;
import com.github.alittlehuang.data.query.specification.BaseQueryStored;
import com.github.alittlehuang.data.query.specification.Expression;

import java.util.List;

/**
 * @author ALittleHuang
 */
public class AbstractQuery<T, P, THIS extends BaseQuery<T, P, THIS>>
        extends AbstractCriteriaBuilder<T, THIS>
        implements BaseQuery<T, P, THIS> {

    protected AbstractQueryStored<T, P> stored;

    protected BaseQueryStored<T, P> getStored() {
        return stored;
    }

    public AbstractQuery(AbstractQueryStored<T, P> stored) {
        super(stored.getJavaType());
        stored.criteria = getCriteria();
        this.stored = stored;
    }

    @Override
    protected THIS createSubItem(Expression<T> expression) {
        //noinspection unchecked
        return (THIS) new AbstractQuery<T, P, THIS>(expression, getWhereClause(), getCriteria(), stored);
    }

    protected AbstractQuery(Expression<T> expression, SimpleWhereClause<T> root, SimpleCriteria<T> criteria,
                            AbstractQueryStored<T, P> stored) {
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
    public P getPage(long page, long size) {
        return getStored().getPage(page, size);
    }

    @Override
    public P getPage() {
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
