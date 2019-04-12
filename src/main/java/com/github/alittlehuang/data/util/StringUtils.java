/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.alittlehuang.data.util;

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
