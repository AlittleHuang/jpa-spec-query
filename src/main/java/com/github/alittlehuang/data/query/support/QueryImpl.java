package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.page.Page;
import com.github.alittlehuang.data.query.specification.Expression;
import com.github.alittlehuang.data.query.specification.Query;
import com.github.alittlehuang.data.query.support.model.CriteriaModel;
import com.github.alittlehuang.data.query.support.model.WhereClauseModel;

/**
 * @author ALittleHuang
 */
public class QueryImpl<T> extends AbstractQuery<T, Page<T>, Query<T>> implements Query<T> {

    public QueryImpl(AbstractQueryStored<T, Page<T>> stored) {
        super(stored);
    }

    protected QueryImpl(Expression<T> expression,
                        WhereClauseModel<T> root,
                        CriteriaModel<T> criteria,
                        AbstractQueryStored<T, Page<T>> stored) {
        super(expression, root, criteria, stored);
    }

    @Override
    protected QueryImpl<T> createSubItem(Expression<T> expression) {
        return new QueryImpl<>(expression, getWhereClause(), getCriteria(), stored);
    }

}
