package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.specification.AttributePath;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author ALittleHuang
 */
@Getter
@EqualsAndHashCode
public class SimpleAttribute<T> implements AttributePath<T> {

    private String[] names;

    public SimpleAttribute(String path) {
        this.names = path.split("\\.");
    }

//    @Override
//    public String[] getNames() {
//        return names;
//    }

    @Override
    public String[] getNames(Class<? extends T> type) {
        return names;
    }
}
