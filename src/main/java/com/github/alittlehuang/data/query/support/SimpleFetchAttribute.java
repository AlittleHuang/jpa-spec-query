package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.specification.AttributePath;
import com.github.alittlehuang.data.query.specification.Expressions;
import com.github.alittlehuang.data.query.specification.FetchAttribute;
import lombok.Setter;
import lombok.experimental.Delegate;

import javax.persistence.criteria.JoinType;

public class SimpleFetchAttribute<T> implements FetchAttribute<T> {

    @lombok.Getter
    @Setter
    private JoinType joinType;
    @Delegate
    private AttributePath attribute;

    public SimpleFetchAttribute(String path, JoinType joinType) {
        attribute = new SimpleAttribute<>(path);
        this.joinType = joinType == null ? JoinType.LEFT : joinType;
    }

    public SimpleFetchAttribute(Expressions<T, ?> expression, JoinType joinType) {
        this.attribute = expression;
        this.joinType = joinType == null ? JoinType.LEFT : joinType;
    }
}
