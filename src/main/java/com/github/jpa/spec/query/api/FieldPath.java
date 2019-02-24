package com.github.jpa.spec.query.api;

import javax.persistence.criteria.Root;

public interface FieldPath<T> {

    String[] getStringPaths(Root<T> root);
//    Getters<T,?> getGetters();
    <X> javax.persistence.criteria.Path<X> getPaths(Root<T> root);
}
