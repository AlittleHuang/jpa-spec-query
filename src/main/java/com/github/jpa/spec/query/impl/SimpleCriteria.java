package com.github.jpa.spec.query.impl;

import com.github.jpa.spec.query.api.Criteria;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.LockModeType;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SimpleCriteria<T> implements Criteria<T> {

    private final WhereClauseItem whereClause;
    private final List<SimpleFieldPath<T>> selections = new ArrayList<>();
    private final List<SimpleFieldPath<T>> groupings = new ArrayList<>();
    private final List<SimpleOrders<T>> orders = new ArrayList<>();
    private final List<SimpleFieldPath<T>> fetchs = new ArrayList<>();

    @Setter
    private LockModeType lockModeType;

    @Setter
    private Integer offset;
    @Setter
    private Integer maxResults;

    public SimpleCriteria(WhereClauseItem whereClause) {
        this.whereClause = whereClause;
    }

}
