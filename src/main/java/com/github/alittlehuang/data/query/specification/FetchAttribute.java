package com.github.alittlehuang.data.query.specification;

import javax.persistence.criteria.JoinType;

/**
 * @author ALittleHuang
 */
public interface FetchAttribute<T> extends AttributePath {

    JoinType getJoinType();

}
