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

public interface Page<T> {

    static <T> Page<T> empty(Pageable pageable) {
        return new Page<T>() {
            @Override
            public long getNumber() {
                return pageable.getPageNumber();
            }

            @Override
            public long getSize() {
                return pageable.getPageSize();
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public List<T> getContent() {
                return Collections.emptyList();
            }
        };
    }

    long getNumber();

    long getSize();

    long getTotalElements();

    List<T> getContent();

}
