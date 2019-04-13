package com.github.alittlehuang.data.util;

/**
 * @author ALittleHuang
 */
public abstract class StringUtils {

    public static boolean hasText(String str) {
        return ( str != null && !str.isEmpty() && containsText(str) );
    }

    public static boolean isBlank(String str) {
        return !hasText(str);
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for ( int i = 0; i < strLen; i++ ) {
            if ( !Character.isWhitespace(str.charAt(i)) ) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasLength(String str) {
        return str != null && !str.isEmpty();
    }

}
