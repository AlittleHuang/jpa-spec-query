package com.github.jpa.spec.query.api;

import com.github.jpa.spec.Getters;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

public interface FieldPath<T> {

    String[] getStringPaths(Root<T> root);
//    Getters<T,?> getGetters();
    <X> Path<X> getPaths(Root<T> root);
}
