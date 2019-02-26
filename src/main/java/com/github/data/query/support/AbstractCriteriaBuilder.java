package com.github.data.query.support;

import com.github.data.query.specification.CriteriaBuilder;
import com.github.data.query.specification.Path;
import org.springframework.data.domain.Sort;

import javax.persistence.LockModeType;

public abstract class AbstractCriteriaBuilder<T, THIS extends CriteriaBuilder<T, THIS>>
        extends AbstractWhereClauseBuilder<T, THIS>
        implements CriteriaBuilder<T, THIS> {

    private final SimpleCriteria<T> criteria;

    protected AbstractCriteriaBuilder() {
        super();
        this.criteria = new SimpleCriteria<>(getWhereClause());
    }

    public AbstractCriteriaBuilder(SimpleFieldPath path, WhereClauseItem root, SimpleCriteria<T> criteria) {
        super(path, root);
        this.criteria = criteria;
    }

    @Override
    public THIS addSelect(String... paths) {
        for (String path : paths) {
            criteria.selections.add(new SimpleFieldPath<>(path));
        }
        return self();
    }

    @Override
    public THIS addSelect(Path<T, ?> paths) {
        criteria.selections.add(new SimpleFieldPath<>(paths));
        return self();
    }

    @Override
    public THIS addGroupings(String... paths) {
        for (String path : paths) {
            criteria.groupings.add(new SimpleFieldPath<>(path));
        }
        return self();
    }

    @Override
    public THIS addGroupings(Path<T, ?> paths) {
        criteria.selections.add(new SimpleFieldPath<>(paths));
        return self();
    }

    @Override
    public THIS addOrdersAsc(String... paths) {
        for (String path : paths) {
            criteria.orders.add(new SimpleOrders<>(Sort.Direction.ASC, path));
        }
        return self();
    }

    @Override
    public THIS addOrdersAsc(Path<T, ?> paths) {
        criteria.orders.add(new SimpleOrders<>(Sort.Direction.ASC, paths));
        return self();
    }

    @Override
    public THIS addOrdersDesc(String... paths) {
        for (String path : paths) {
            criteria.orders.add(new SimpleOrders<>(Sort.Direction.DESC, path));
        }
        return self();
    }

    @Override
    public THIS addOrdersDesc(Path<T, ?> paths) {
        criteria.orders.add(new SimpleOrders<>(Sort.Direction.DESC, paths));
        return self();
    }

    @Override
    public THIS addFetchs(String... paths) {
        for (String path : paths) {
            criteria.fetchs.add(new SimpleFieldPath<>(path));
        }
        return self();
    }

    @Override
    public THIS addFetchs(Path<T, ?> paths) {
        criteria.fetchs.add(new SimpleFieldPath<>(paths));
        return self();
    }

    @Override
    public THIS setLockModeType(LockModeType lockModeType) {
        return self();
    }

    @Override
    public SimpleCriteria<T> getCriteria() {
        return criteria;
    }
}
