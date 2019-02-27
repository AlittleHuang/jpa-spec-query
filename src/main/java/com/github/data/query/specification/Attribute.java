package com.github.data.query.specification;

public interface Attribute<T> {
    String[] getNames(Class<T> root);
}
