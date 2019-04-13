package com.github.alittlehuang.data.util;

import java.util.Arrays;

/**
 * @author ALittleHuang
 */
public class JointKey {

    private final Object[] values;

    public JointKey(Object... keys) {
        values = keys;
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
        return Arrays.equals(values, jointKey.values);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(values);
    }

}
