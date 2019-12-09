package com.github.alittlehuang.data.query.support;

import com.github.alittlehuang.data.query.specification.*;
import com.github.alittlehuang.data.query.support.model.*;

import javax.persistence.LockModeType;
import javax.persistence.criteria.JoinType;

/**
 * @author ALittleHuang
 */
public abstract class AbstractCriteriaBuilder<T, THIS extends CriteriaBuilder<T, THIS>>
        extends AbstractWhereClauseBuilder<T, THIS>
        implements CriteriaBuilder<T, THIS> {

    private final CriteriaModel<T> criteria;

    protected AbstractCriteriaBuilder(Class<T> type) {
        super(type);
        this.criteria = new CriteriaModel<>(getWhereClause(), type);
    }

    public AbstractCriteriaBuilder(Expression<T> path, WhereClause<T> root, Criteria<T> criteria) {
        super(path, root, criteria.getJavaType());
        this.criteria = CriteriaModel.convert(criteria);
    }

    @Override
    public THIS addSelect(String... paths) {
        for (String path : paths) {
            String[] names = path.split("\\.");
            SelectionModel<T> selectionModel = new SelectionModel<>();
            selectionModel.setNames(names);
            criteria.getSelections().add(selectionModel);
        }
        return self();
    }

    @Override
    public THIS addSelect(Expressions<T, ?> expression) {
        criteria.getSelections().add(expression.toSelectionModel(getJavaType()));
        return self();
    }

    @Override
    public THIS addSelect(Expressions<T, Number> expression, AggregateFunctions aggregate) {

        SelectionModel<T> model = expression.toSelectionModel(getJavaType());
        model.setAggregateFunctions(aggregate);
        criteria.getSelections().add(model);
        return self();
    }

    @Override
    public THIS addGroupings(String... paths) {
        for (String path : paths) {
            ExpressionModel<T> model = new ExpressionModel<>();
            model.setNames(path.split("\\."));
            criteria.getGroupings().add(model);
        }
        return self();
    }

    @Override
    public THIS addGroupings(Expressions<T, ?> expression) {
        criteria.getGroupings().add(expression.toExpressionModel(getJavaType()));
        return self();
    }

    @Override
    public THIS addOrdersAsc(String... paths) {
        for (String path : paths) {
            OrdersModel<T> order = new OrdersModel<>();
            order.setNames(path.split("\\."));
            criteria.getOrders().add(order);
        }
        return self();
    }

    @Override
    public THIS addOrdersAsc(Expressions<T, ?> expression) {
        criteria.getOrders().add(expression.toOrdersModel(getJavaType()));
        return self();
    }

    @Override
    public THIS addOrdersDesc(String... paths) {
        for (String path : paths) {
            OrdersModel<T> order = new OrdersModel<>();
            order.setNames(path.split("\\."));
            order.setDirection(Direction.DESC);
            criteria.getOrders().add(order);
        }
        return self();
    }

    @Override
    public THIS addOrdersDesc(Expressions<T, ?> expression) {
        OrdersModel<T> order = expression.toOrdersModel(getJavaType());
        order.setDirection(Direction.DESC);
        criteria.getOrders().add(order);
        return self();
    }

    @Override
    public THIS fetch(String... paths) {
        for (String path : paths) {
            fetch(path, JoinType.LEFT);
        }
        return self();
    }

    @Override
    public THIS fetch(String paths, JoinType joinType) {
        FetchAttributeModel<T> fetchAttribute = new FetchAttributeModel<>();
        fetchAttribute.setJoinType(joinType);
        fetchAttribute.setNames(paths.split("\\."));
        criteria.getFetchAttributes().add(fetchAttribute);
        return self();
    }

    @Override
    public THIS fetch(Expressions<T, ?> expression, JoinType joinType) {
        FetchAttributeModel<T> attributeModel = expression.toFetchAttributeModel(getJavaType());
        attributeModel.setJoinType(joinType);
        criteria.getFetchAttributes().add(attributeModel);
        return self();
    }

    @Override
    public THIS fetch(Expressions<T, ?> expression) {
        return fetch(expression, JoinType.LEFT);
    }

    @Override
    public THIS setLockModeType(LockModeType lockModeType) {
        criteria.setLockModeType(lockModeType);
        return self();
    }

    @Override
    public CriteriaModel<T> getCriteria() {
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
