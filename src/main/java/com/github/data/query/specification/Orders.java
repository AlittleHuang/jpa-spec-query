package com.github.data.query.specification;

import org.springframework.data.domain.Sort;

public interface Orders<T> extends FieldPath<T> {

    Sort.Direction getDirection();

}
