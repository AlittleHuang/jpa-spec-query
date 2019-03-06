package com.github.data.query.support;

import com.github.data.query.specification.Attribute;
import com.github.data.query.specification.FetchAttribute;
import com.github.data.query.specification.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;

import javax.persistence.criteria.JoinType;

public class SimpleFatchAttribute<T> implements FetchAttribute<T> {

    @lombok.Getter
    @Setter
    private JoinType joinType;
    @Delegate
    private Attribute<T> attribute;

    public SimpleFatchAttribute(String path, JoinType joinType) {
        attribute = new SimpleAttribute<>(path);
        this.joinType = joinType == null ? JoinType.LEFT : joinType;
    }

    public SimpleFatchAttribute(Getter<T, ?> getter, JoinType joinType) {
        this.attribute = getter;
        this.joinType = joinType == null ? JoinType.LEFT : joinType;
    }
}
