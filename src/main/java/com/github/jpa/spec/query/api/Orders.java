package com.github.jpa.spec.query.api;

import org.springframework.data.domain.Sort;

public interface Orders<T> extends FieldPath<T> {

    Sort.Direction getDirection();

}
