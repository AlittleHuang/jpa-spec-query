package com.github.alittlehuang.data.jpa.support;

import com.github.alittlehuang.data.query.page.Page;
import com.github.alittlehuang.data.query.page.PageImpl;
import com.github.alittlehuang.data.query.page.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

public class JpaQueryStored<T> extends AbstractJpaQueryStored<T, Page<T>> {

    public JpaQueryStored(EntityManager entityManager, Class<T> type) {
        super(entityManager, type);
    }

    @Override
    protected Page<T> toPage(Number page, Number size, List<T> content, Number totalElement) {
        return new PageImpl<>(content, new Pageable(page.longValue(), size.longValue()), totalElement.longValue());
    }
}
