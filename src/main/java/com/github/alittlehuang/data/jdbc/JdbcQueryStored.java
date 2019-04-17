package com.github.alittlehuang.data.jdbc;

import com.github.alittlehuang.data.query.page.Page;
import com.github.alittlehuang.data.query.page.PageImpl;
import com.github.alittlehuang.data.query.page.Pageable;

import java.util.List;

public class JdbcQueryStored<T> extends AbstractJdbcQueryStored<T, Page<T>> {

    public JdbcQueryStored(JdbcQueryStoredConfig config, Class<T> entityType) {
        super(config, entityType);
    }

    @Override
    protected Page<T> toPage(Number page, Number size, List<T> content, Number totalElement) {
        return new PageImpl<>(content, new Pageable(page.longValue(), size.longValue()), totalElement.longValue());
    }
}