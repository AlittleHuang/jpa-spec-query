package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.specification.AttributePath;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class SimpleAttribute<T> implements AttributePath {

    private String[] names;

    public SimpleAttribute(String path) {
        this.names = path.split("\\.");
    }

    @Override
    public String[] getNames() {
        return names;
    }
}
