package com.github.jpa.spec.query.impl;

import com.github.jpa.spec.query.api.Path;
import com.github.jpa.spec.query.api.FieldPath;
import com.github.jpa.spec.util.JpaHelper;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.criteria.Root;

@Getter
@EqualsAndHashCode
public class SimpleFieldPath<T> implements FieldPath<T> {

    private String[] paths;
    private Path<T, ?> getters;

    public SimpleFieldPath(String path) {
        this.paths = path.split("\\.");
    }

    public SimpleFieldPath(Path<T, ?> getter) {
        this.getters = getter;
    }

    @Override
    public String[] getStringPaths(Root<T> root) {
        if (paths != null) {
            return paths;
        }
        getPaths(root);
        return paths;
    }

    @SuppressWarnings("unchecked")
    public <X> javax.persistence.criteria.Path getPaths(Root<T> root) {
        javax.persistence.criteria.Path result = root;
        if (paths != null) {
            for (String path : paths) {
                result = result.get(path);
            }
        } else {
            paths = new String[getters.list().size()];
            int i = 0;
            for (Path getters : getters.list()) {
                String attributeName = JpaHelper.getPropertyNameFromGetter(result.getJavaType(), getters);
                paths[i++] = attributeName;
                result = result.get(attributeName);
            }
        }
        return result;
    }

//    @Override
//    public String[] getPaths(Class<T> type) {
//        if (paths != null) {
//            return paths;
//        }
//        if (getters != null) {
//
//        }
//        return new String[0];
//    }
}
