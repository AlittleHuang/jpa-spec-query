package com.github.jpa.util;

import javax.persistence.criteria.Path;

public class JpaHelper {

    public static <T> Path<?> toPath(Path<T> root, String[] attributeNames) {
        Path path = root;
        for (String attributeName : attributeNames) {
            path = path.get(attributeName);
        }
        return path;
    }

}
