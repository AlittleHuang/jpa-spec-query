package com.github.alittlehuang.data.query.specification;

/**
 * @author ALittleHuang
 */
public interface Orders<T> extends Expression<T> {

    Direction getDirection();

}
