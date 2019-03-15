package com.github.data.query.support;

import com.github.data.query.specification.Attribute;
import com.github.data.query.specification.FetchAttribute;
import com.github.data.query.specification.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;

import javax.persistence.criteria.JoinType;

public class SimpleFetchAttribute<T> implements FetchAttribute<T> {

    @lombok.Getter
    @Setter
    private JoinType joinType;
    @Delegate
    private Attribute<T> attribute;

    public SimpleFetchAttribute(String path, JoinType joinType) {
        attribute = new SimpleAttribute<>(path);
        this.joinType = joinType == null ? JoinType.LEFT : joinType;
    }

    public SimpleFetchAttribute(Getter<T, ?> getter, JoinType joinType) {
        this.attribute = getter;
        this.joinType = joinType == null ? JoinType.LEFT : joinType;
    }
}
