package com.github.alittlehuang.data.query.specification;

import javax.persistence.criteria.JoinType;

public interface FetchAttribute<T> extends Attribute<T> {

    JoinType getJoinType();

}
