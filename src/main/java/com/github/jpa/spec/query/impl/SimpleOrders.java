package com.github.jpa.spec.query.impl;

import com.github.jpa.spec.query.api.Path;
import com.github.jpa.spec.query.api.Orders;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

public class SimpleOrders<T> extends SimpleFieldPath<T> implements Orders<T> {

    @Getter
    @Setter
    Sort.Direction direction;

    public SimpleOrders(Sort.Direction direction, String path) {
        super(path);
        this.direction = direction;
    }

    public SimpleOrders(Sort.Direction direction, Path<T, ?> getter) {
        super(getter);
        this.direction = direction;
    }
}
