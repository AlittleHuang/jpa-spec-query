package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.specification.Attribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class SimpleAttribute<T> implements Attribute<T> {

    private String[] names;

    public SimpleAttribute(String path) {
        this.names = path.split("\\.");
    }

    @Override
    public String[] getNames() {
        return names;
    }
}
