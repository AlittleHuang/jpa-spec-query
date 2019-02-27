package com.github.jpa.util;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

public class JpaHelper {

    public static <T> Path<?> getPath(Root<T> root, String[] attributeNames) {
        Path path = root;
        for (String attributeName : attributeNames) {
            path = path.get(attributeName);
        }
        return path;
    }

}
