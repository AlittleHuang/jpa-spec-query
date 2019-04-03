package com.github.data.query.support;

import com.github.data.query.specification.AggregateFunctions;
import com.github.data.query.specification.AttrExpression;
import com.github.data.query.specification.CriteriaBuilder;
import com.github.data.query.specification.Selection;
import org.springframework.data.domain.Sort;

import javax.persistence.LockModeType;
import javax.persistence.criteria.JoinType;

public abstract class AbstractCriteriaBuilder<T, THIS extends CriteriaBuilder<T, THIS>>
        extends AbstractWhereClauseBuilder<T, THIS>
        implements CriteriaBuilder<T, THIS> {

    private final SimpleCriteria<T> criteria;

    protected AbstractCriteriaBuilder() {
        super();
        this.criteria = new SimpleCriteria<>(getWhereClause());
    }

    public AbstractCriteriaBuilder(AttrExpression<T> path, SimpleWhereClause<T> root, SimpleCriteria<T> criteria) {
        super(path, root);
        this.criteria = criteria;
    }

    @Override
    public THIS addSelect(String... paths) {
        for (String path : paths) {
            Selection<T> selection = cls -> paths;
            criteria.selections.add(selection);
        }
        return self();
    }

    @Override
    public THIS addSelect(Expressions<T, ?> expression) {
        criteria.selections.add(expression);
        return self();
    }

    @Override
    public THIS addSelect(Expressions<T, ?> expression, AggregateFunctions aggregate) {

        Selection<T> selection = new Selection<T>() {

            @Override
            public String[] getNames(Class<? extends T> cls) {
                return expression.getNames(cls);
            }

            @Override
            public AggregateFunctions getAggregateFunctions() {
                return aggregate;
            }

        };

        criteria.selections.add(selection);
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
    public THIS addGroupings(Expressions<T, ?> expression) {
        criteria.groupings.add(expression);
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
    public THIS addOrdersAsc(Expressions<T, ?> expression) {
        criteria.orders.add(new SimpleOrders<>(Sort.Direction.ASC, expression));
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
    public THIS addOrdersDesc(Expressions<T, ?> expression) {
        criteria.orders.add(new SimpleOrders<>(Sort.Direction.DESC, expression));
        return self();
    }

    @Override
    public THIS fetch(String... paths) {
        for (String path : paths) {
            criteria.fetchAttributes.add(new SimpleFetchAttribute<>(path, JoinType.LEFT));
        }
        return self();
    }

    @Override
    public THIS fetch(String paths, JoinType joinType) {
        criteria.fetchAttributes.add(new SimpleFetchAttribute<>(paths, joinType));
        return self();
    }

    @Override
    public THIS fetch(Expressions<T, ?> expression, JoinType joinType) {
        criteria.fetchAttributes.add(new SimpleFetchAttribute<>(expression, joinType));
        return self();
    }

    @Override
    public THIS fetch(Expressions<T, ?> expression) {
        criteria.fetchAttributes.add(new SimpleFetchAttribute<>(expression, JoinType.LEFT));
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

    @Override
    public THIS setOffset(long offset) {
        criteria.setOffset(offset);
        return self();
    }

    @Override
    public THIS setMaxResult(long maxResult) {
        criteria.setMaxResults(maxResult);
        return self();
    }

    @Override
    public THIS setPageable(long page, long size) {
        criteria.setMaxResults(size);
        criteria.setOffset(page * size);
        return self();
    }
}
