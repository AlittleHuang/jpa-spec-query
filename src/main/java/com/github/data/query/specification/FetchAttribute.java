package com.github.data.query.specification;

import javax.persistence.criteria.JoinType;

public interface FetchAttribute<T> extends Attribute<T> {

    JoinType getJoinType();

}
