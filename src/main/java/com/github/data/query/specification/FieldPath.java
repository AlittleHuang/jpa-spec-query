package com.github.data.query.specification;

import javax.persistence.criteria.Root;

public interface FieldPath<T> {

    String[] getStringPaths(Root<T> root);
//    Getters<T,?> getGetters();
    <X> javax.persistence.criteria.Path<X> getPaths(Root<T> root);
}
