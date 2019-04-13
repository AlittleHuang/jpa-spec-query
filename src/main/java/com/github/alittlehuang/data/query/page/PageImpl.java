/*
 * Copyright 2008-2018 the original author or authors.
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

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Basic {@code Page} implementation.
 *
 * @param <T> the type of which the page consists.
 * @author Oliver Gierke
 * @author Mark Paluch
 */
public class PageImpl<T> extends Chunk<T> implements Page<T> {

    private final long total;

    /**
     * Constructor of {@code Page}.
     *
     * @param content  the content of this page, must not be {@literal null}.
     * @param pageable the paging information, must not be {@literal null}.
     * @param total    the total amount of items available. The total might be adapted considering the length of the content
     *                 given, if it is going to be the content of the last page. This is in place to mitigate inconsistencies.
     */
    public PageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable);
        this.total = total;
    }

    public static <T> PageImpl<T> empty(Pageable pageable) {
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    public int getTotalPages() {
        return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
    }

    @Override
    public long getTotalElements() {
        return total;
    }

    @Override
    public boolean hasNext() {
        return getNumber() + 1 < getTotalPages();
    }

    public <U> PageImpl<U> map(Function<? super T, ? extends U> converter) {
        return new PageImpl<>(getConvertedContent(converter), getPageable(), total);
    }

    @Override
    public String toString() {

        String contentType = "UNKNOWN";
        List<T> content = getContent();

        if (content.size() > 0) {
            contentType = content.get(0).getClass().getName();
        }

        return String.format("Page %s of %d containing %s instances", getNumber() + 1, getTotalPages(), contentType);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PageImpl<?>)) {
            return false;
        }

        PageImpl<?> that = (PageImpl<?>) obj;

        return this.total == that.total && super.equals(obj);
    }

    @Override
    public int hashCode() {

        int result = 17;

        result += 31 * (int) (total ^ total >>> 32);
        result += 31 * super.hashCode();

        return result;
    }
}
