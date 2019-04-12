/*
 * Copyright 2013-2018 the original author or authors.
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
package com.github.alittlehuang.data.query.page;

import java.io.Serializable;

/**
 * Abstract Java Bean implementation of {@code Pageable}.
 *
 * @author Thomas Darimont
 * @author Oliver Gierke
 * @author Alex Bondarev
 */
public class Pageable implements Serializable {

    private final int page;
    private final int size;

    /**
     * Creates a new {@link Pageable}. Pages are zero indexed, thus providing 0 for {@code page} will return
     * the first page.
     *
     * @param page must not be less than zero.
     * @param size must not be less than one.
     */
    public Pageable(int page, int size) {

        if ( page < 0 ) {
            throw new IllegalArgumentException("Page index must not be less than zero!");
        }

        if ( size < 1 ) {
            throw new IllegalArgumentException("Page size must not be less than one!");
        }

        this.page = page;
        this.size = size;
    }

    public int getPageSize() {
        return size;
    }

    public int getPageNumber() {
        return page;
    }

    public long getOffset() {
        return (long) page * (long) size;
    }

    public boolean hasPrevious() {
        return page > 0;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;

        result = prime * result + page;
        result = prime * result + size;

        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if ( this == obj ) {
            return true;
        }

        if ( obj == null || getClass() != obj.getClass() ) {
            return false;
        }

        Pageable other = (Pageable) obj;
        return this.page == other.page && this.size == other.size;
    }
}
