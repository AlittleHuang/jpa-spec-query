package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.page.PageFactory;
import com.github.alittlehuang.data.query.specification.BaseQueryStored;
import com.github.alittlehuang.data.query.specification.Criteria;

/**
 * @author ALittleHuang
 */
public abstract class AbstractQueryStored<T, P> implements BaseQueryStored<T, P> {
    protected static final int DEFAULT_PAGE_SIZE = 10;

    protected Criteria<T> criteria;
    protected Class<T> type;
    private final PageFactory<T, P> pageFactory;

    public AbstractQueryStored(PageFactory<T, P> pageFactory) {
        this.pageFactory = pageFactory;
    }

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
    public P getPage() {
        Criteria<T> criteria = getCriteria();
        Long offset = criteria.getOffset();
        offset = offset == null ? 0 : offset;
        Long maxResults = criteria.getMaxResults();
        maxResults = maxResults == null ? DEFAULT_PAGE_SIZE : maxResults;
        return getPage(( offset / maxResults ), maxResults);
    }

    protected PageFactory<T, P> getPageFactory() {
        return pageFactory;
    }
}
