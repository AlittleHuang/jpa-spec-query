package com.github.alittlehuang.data.jpa.util;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 * @author ALittleHuang
 */
public class JpaHelper {

    public static <T> Path<?> getPath(Root<T> root, String[] attributeNames) {
        Path path = root;
        for ( String attributeName : attributeNames ) {
            path = path.get(attributeName);
        }
        return path;
    }


    public static <T> boolean hasAttributeNames(Root<T> root, String attributeNames) {
        return hasAttributeNames(root, attributeNames.split("\\."));
    }

    public static <T> boolean hasAttributeNames(Root<T> root, String[] attributeNames) {
        try {
            getPath(root, attributeNames);
        } catch ( IllegalArgumentException e ) {
            return false;
        }
        return true;
    }

}
