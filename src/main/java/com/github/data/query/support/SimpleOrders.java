package com.github.data.query.support;

import com.github.data.query.specification.Path;
import com.github.data.query.specification.Orders;
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
