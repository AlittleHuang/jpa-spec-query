package com.github.data.query.support;

import com.github.data.query.specification.Expression;

public class QueryImpl<T> extends AbstractQuery<T> {

    public QueryImpl(AbstractQueryStored<T> stored) {
        super(stored);
    }

    protected QueryImpl(Expression<T> expression,
                        SimpleWhereClause<T> root,
                        SimpleCriteria<T> criteria,
                        AbstractQueryStored<T> stored) {
        super(expression, root, criteria, stored);
    }

    @Override
    protected QueryImpl<T> createSubItem(Expression<T> expression) {
        return new QueryImpl<>(expression, getWhereClause(), getCriteria(), stored);
    }

}
