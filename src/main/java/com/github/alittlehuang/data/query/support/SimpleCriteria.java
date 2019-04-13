package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.specification.*;
import lombok.Setter;

import javax.persistence.LockModeType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ALittleHuang
 */
public class SimpleCriteria<T> implements Criteria<T> {

    protected final SimpleWhereClause<T> whereClause;
    protected final List<Selection<T>> selections = new ArrayList<>();
    protected final List<Expression<T>> groupings = new ArrayList<>();
    protected final List<SimpleOrders<T>> orders = new ArrayList<>();
    protected final List<FetchAttribute<T>> fetchAttributes = new ArrayList<>();
    private final Class<T> javaType;

    @Setter
    private LockModeType lockModeType;

    @Setter
    private Long offset;
    @Setter
    private Long maxResults;

    public SimpleCriteria(SimpleWhereClause<T> whereClause, Class<T> type) {
        this.whereClause = whereClause;
        this.javaType = type;
    }

    @Override
    public LockModeType getLockModeType() {
        return lockModeType;
    }

    @Override
    public SimpleWhereClause<T> getWhereClause() {
        return whereClause;
    }

    @Override
    public List<Selection<T>> getSelections() {
        return selections;
    }

    @Override
    public List<Expression<T>> getGroupings() {
        return groupings;
    }

    @Override
    public List<SimpleOrders<T>> getOrders() {
        return orders;
    }

    @Override
    public List<FetchAttribute<T>> getFetchAttributes() {
        return fetchAttributes;
    }

    @Override
    public Long getOffset() {
        return offset;
    }

    @Override
    public Long getMaxResults() {
        return maxResults;
    }

    @Override
    public Class<T> getJavaType() {
        return javaType;
    }
}
