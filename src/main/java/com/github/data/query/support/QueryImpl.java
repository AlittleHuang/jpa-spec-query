package com.github.data.query.support;

import com.github.data.query.specification.Stored;
import lombok.experimental.Delegate;

public class QueryImpl<T> extends AbstractCriteriaBuilder<T, com.github.data.query.specification.Query<T>> implements
        com.github.data.query.specification.Query<T> {

    @Delegate
    protected Stored<T> stored;

    @Override
    protected QueryImpl<T> createSub(SimpleFieldPath<T> paths) {
        return new QueryImpl<T>(paths, getWhereClause(), getCriteria(), (AbstractStored<T>) stored);
    }

    public QueryImpl(AbstractStored<T> stored) {
        super();
        stored.criteria = getCriteria();
        this.stored = stored;
    }

    private QueryImpl(SimpleFieldPath<T> path, WhereClauseItem root, SimpleCriteria<T> criteria, AbstractStored<T> stored) {
        super(path, root, criteria);
        stored.criteria = criteria;
        this.stored = stored;
    }

}
