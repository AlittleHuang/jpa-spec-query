package com.github.data.query.support;

import com.github.data.query.specification.Attribute;
import com.github.data.query.specification.CriteriaBuilder;
import com.github.data.query.specification.Getter;
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

    public AbstractCriteriaBuilder(Attribute path, WhereClauseItem<T> root, SimpleCriteria<T> criteria) {
        super(path, root);
        this.criteria = criteria;
    }

    @Override
    public THIS addSelect(String... paths) {
        for (String path : paths) {
            criteria.selections.add(new SimpleAttribute<>(path));
        }
        return self();
    }

    @Override
    public THIS addSelect(Getter<T, ?> paths) {
        criteria.selections.add(paths);
        return self();
    }

    @Override
    public THIS addGroupings(String... paths) {
        for (String path : paths) {
            criteria.groupings.add(new SimpleAttribute<>(path));
        }
        return self();
    }

    @Override
    public THIS addGroupings(Getter<T, ?> paths) {
        criteria.selections.add(paths);
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
    public THIS addOrdersAsc(Getter<T, ?> paths) {
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
    public THIS addOrdersDesc(Getter<T, ?> paths) {
        criteria.orders.add(new SimpleOrders<>(Sort.Direction.DESC, paths));
        return self();
    }

    @Override
    public THIS fetch(String... paths) {
        for (String path : paths) {
            criteria.fetchs.add(new SimpleAttribute<>(path));
        }
        return self();
    }

    @Override
    public THIS fetch(Getter<T, ?> paths) {
        criteria.fetchs.add(paths);
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
