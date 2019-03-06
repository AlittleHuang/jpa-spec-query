package com.github.data.query.support;

import com.github.data.query.specification.Attribute;
import com.github.data.query.specification.Criteria;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.LockModeType;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SimpleCriteria<T> implements Criteria<T> {

    protected final WhereClauseItem<T> whereClause;
    protected final List<Attribute<T>> selections = new ArrayList<>();
    protected final List<Attribute<T>> groupings = new ArrayList<>();
    protected final List<SimpleOrders<T>> orders = new ArrayList<>();
    protected final List<Attribute<T>> fetchs = new ArrayList<>();

    @Setter
    private LockModeType lockMode;

    @Setter
    private Long offset;
    @Setter
    private Long maxResults;

    public SimpleCriteria(WhereClauseItem<T> whereClause) {
        this.whereClause = whereClause;
    }

    @Override
    public LockModeType getLockModeType() {
        return null;
    }
}
