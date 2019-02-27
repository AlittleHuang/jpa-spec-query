package com.github.data.query.support;

import com.github.data.query.specification.Attribute;
import com.github.data.query.specification.Getter;
import com.github.data.query.specification.Orders;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.springframework.data.domain.Sort;

public class SimpleOrders<T> implements Orders<T>, Attribute<T> {

    @lombok.Getter
    @Setter
    private Sort.Direction direction;
    @Delegate
    private Attribute<T> attribute;

    public SimpleOrders(Sort.Direction direction, String path) {
        attribute = new SimpleAttribute<>(path);
        this.direction = direction;
    }

    public SimpleOrders(Sort.Direction direction, Getter<T, ?> getter) {
        this.attribute = getter;
        this.direction = direction;
    }
}
