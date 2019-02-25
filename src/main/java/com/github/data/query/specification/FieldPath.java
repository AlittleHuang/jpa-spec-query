package com.github.data.query.specification;

import javax.persistence.criteria.Root;

public interface FieldPath<T> {

    String[] getStringPaths(Root<T> root);
    <X> javax.persistence.criteria.Path<X> getPaths(Root<T> root);

}
