package com.github.alittlehuang.data.query.specification;

import org.springframework.data.domain.Sort;

public interface Orders<T> extends Expression<T> {

    Sort.Direction getDirection();

}
