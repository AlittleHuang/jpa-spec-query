package com.github.jpa.spec.query.impl;

import com.github.jpa.spec.Getters;
import com.github.jpa.spec.query.api.Criteria;
import com.github.jpa.spec.query.api.WhereClause;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import javax.persistence.LockModeType;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SimpleCriteria<T> implements Criteria<T> {

    WhereClauseItem<T> whereClause;
    private final List<SimpleFieldPath<T>> selections = new ArrayList<>();
    private final List<SimpleFieldPath<T>> groupings = new ArrayList<>();
    private final List<SimpleOrders<T>> orders = new ArrayList<>();
    private final List<SimpleFieldPath<T>> fetchs = new ArrayList<>();

    private LockModeType lockModeType;

    @Setter
    private Integer offset;
    @Setter
    private Integer maxResults;

    public SimpleCriteria(WhereClauseItem<T> whereClause) {
        this.whereClause = whereClause;
    }

}
