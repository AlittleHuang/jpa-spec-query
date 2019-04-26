package com.github.alittlehuang.data.util;

import java.util.Arrays;

/**
 * @author ALittleHuang
 */
public class JointKey {

    private final Object[] values;
    private final int hashCode;

    public JointKey(Object... objects) {
        values = objects;
        hashCode = Arrays.deepHashCode(objects);
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        JointKey jointKey = (JointKey) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return hashCode() == jointKey.hashCode() && Arrays.equals(values, jointKey.values);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

}
