package com.github.jpa.spec;

import lombok.Getter;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.List;

@Getter
public class QueryImpl<T> {

    @Delegate
    private WhereClauseImpl whereClause;
    private final List<String[]> selections = new ArrayList<>();
    private final List<String[]> groupings = new ArrayList<>();
    private final List<String[]> orders = new ArrayList<>();
    private T type;

    private Integer offset;
    private Integer maxResults;

    public QueryImpl addSelect(String... paths) {
        for (String path : paths) {
            selections.add(path.split("\\."));
        }
        return this;
    }

    @SafeVarargs
    public final QueryImpl addSelect(Getters<T, ?>... paths) {

        return this;
    }
}
