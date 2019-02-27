package com.github.data.query.specification;

import org.springframework.data.domain.Sort;

public interface Orders<T> extends Attribute<T> {

    Sort.Direction getDirection();

}
