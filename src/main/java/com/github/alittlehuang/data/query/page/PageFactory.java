package com.github.alittlehuang.data.query.page;

import java.util.List;

public interface PageFactory<T, P> {
    P get(Number page, Number size, List<T> content, Number totalElement);

    @SuppressWarnings("unchecked")
    PageFactory DEFAULT = (p, s, c, t) -> new PageImpl<>(c, new Pageable(p.longValue(), s.longValue()), t.longValue());

    static <T> PageFactory<T, Page<T>> getDefault() {
        //noinspection unchecked
        return (PageFactory<T, Page<T>>) DEFAULT;
    }
}
