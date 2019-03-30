package com.github.data.query.specification;

import org.springframework.data.domain.Sort;

public interface Orders<T> extends Expressions<T> {

    Sort.Direction getDirection();

}
