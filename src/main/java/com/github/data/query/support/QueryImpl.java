package com.github.data.query.support;

import com.github.data.query.specification.Attribute;
import com.github.data.query.specification.Query;
import com.github.data.query.specification.QueryStored;
import lombok.experimental.Delegate;

public class QueryImpl<T> extends AbstractCriteriaBuilder<T, Query<T>> implements
        Query<T> {

    @Delegate
    protected QueryStored<T> stored;

    @Override
    protected QueryImpl<T> createSub(Attribute<T> paths) {
        return new QueryImpl<>(paths, getWhereClause(), getCriteria(), (AbstractQueryStored<T>) stored);
    }

    public QueryImpl(AbstractQueryStored<T> stored) {
        super();
        stored.criteria = getCriteria();
        this.stored = stored;
    }

    private QueryImpl(Attribute<T> path, WhereClauseItem root, SimpleCriteria<T> criteria, AbstractQueryStored<T> stored) {
        super(path, root, criteria);
        stored.criteria = criteria;
        this.stored = stored;
    }

}
