package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.page.Page;
import com.github.alittlehuang.data.query.specification.Criteria;
import com.github.alittlehuang.data.query.specification.QueryStored;

/**
 * @author ALittleHuang
 */
public abstract class AbstractQueryStored<T> implements QueryStored<T> {
    protected static final int DEFAULT_PAGE_SIZE = 10;

    protected Criteria<T> criteria;
    protected Class<T> type;

    void setCriteria(Criteria<T> criteria) {
        this.criteria = criteria;
    }

    protected Criteria<T> getCriteria() {
        return criteria;
    }


    @Override
    public Class<T> getJavaType() {
        return type;
    }

    @Override
    public Page<T> getPage() {
        Criteria<T> criteria = getCriteria();
        Long offset = criteria.getOffset();
        offset = offset == null ? 0 : offset;
        Long maxResults = criteria.getMaxResults();
        maxResults = maxResults == null ? DEFAULT_PAGE_SIZE : maxResults;
        return getPage(( offset / maxResults ), maxResults);
    }
}
