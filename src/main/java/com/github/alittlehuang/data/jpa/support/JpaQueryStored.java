package com.github.alittlehuang.data.jpa.support;

import com.github.alittlehuang.data.metamodel.EntityInformation;
import com.github.alittlehuang.data.jpa.util.JpaHelper;
import com.github.alittlehuang.data.query.page.Page;
import com.github.alittlehuang.data.query.page.Pageable;
import com.github.alittlehuang.data.query.specification.Selection;
import com.github.alittlehuang.data.query.specification.*;
import com.github.alittlehuang.data.query.support.AbstractQueryStored;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JpaQueryStored<T> extends AbstractQueryStored<T> {

    protected EntityManager entityManager;

    public JpaQueryStored(EntityManager entityManager, Class<T> type) {
        this.entityManager = entityManager;
        this.type = type;
    }

    @Override
    public List<T> getResultList() {
        StoredData<T> data = new StoredData<>(type).initAll();
        TypedQuery<T> typedQuery = entityManager.createQuery(data.query.select(data.root));
        setLimit(typedQuery, criteria.getOffset(), criteria.getMaxResults());
        setLock(typedQuery);
        return typedQuery.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> List<X> getObjectList() {
        List<? extends Selection<T>> list = criteria.getSelections();
        if (list == null || list.isEmpty()) {
            return (List<X>) getResultList();
        }
        StoredData<?> data = new StoredData<>(Object.class).initWhere().initGroupBy().initOrderBy();
        CriteriaQuery<?> query = data.query;
        List selections = list.stream()
                .map(it -> {

                    Expression expression = JpaHelper.toExpression(it, data.cb, data.root);
                    AggregateFunctions aggregate = it.getAggregateFunctions();

                    switch ( aggregate == null ? AggregateFunctions.NONE : aggregate ) {
                        case NONE:
                            return expression;
                        case AVG:
                            return data.cb.avg(expression);
                        case SUM:
                            return data.cb.sum(expression);
                        case MAX:
                            return data.cb.max(expression);
                        case MIN:
                            return data.cb.min(expression);
                        case COUNT:
                            return data.cb.count(expression);
                        default:
                            return expression;
                    }

                }).collect(Collectors.toList());
        TypedQuery typedQuery = entityManager
                .createQuery(query.multiselect(selections));
        setLimit(typedQuery, criteria.getOffset(), criteria.getMaxResults());
        setLock(typedQuery);
        return (List<X>) typedQuery.getResultList();
    }

    @Override
    public Page<T> getPage(long page, long size) {
        long count = count();
        Pageable pageable = new Pageable((int) page, (int) size);
        if (count == 0) {
            return Page.empty(pageable);
        }
        StoredData<T> data = new StoredData<>(type).initAll();
        TypedQuery<T> typedQuery = entityManager.createQuery(data.query.select(data.root));

        setLimit(typedQuery, pageable.getOffset(), size);

        setLock(typedQuery);
        List<T> resultList = typedQuery.getResultList();

        return new Page<>(resultList, pageable, count);
    }

    @Override
    public long count() {

        StoredData<Long> data = new StoredData<>(Long.class).initWhere().initGroupBy();
        CriteriaQuery<Long> countQuery = data.query;
        countQuery.select(data.cb.count(data.root));
        return entityManager.createQuery(countQuery).getSingleResult();

    }

    @Override
    public boolean exists() {
        StoredData<Object> data = new StoredData<>(Object.class).initWhere().initGroupBy();
        EntityInformation<T, ?> information = EntityInformation.getInstance(type);
        data.query.select(data.root.get(information.getIdAttribute().getFieldName()));
        return !entityManager.createQuery(data.query)
                .setMaxResults(1)
                .getResultList()
                .isEmpty();
    }

    private void setLock(TypedQuery<T> typedQuery) {
        LockModeType lockModeType = criteria.getLockModeType();
        if (lockModeType != null) {
            typedQuery.setLockMode(lockModeType);
        }
    }

    private void setLimit(TypedQuery<T> typedQuery, Long offset, Long maxResults) {
        if (maxResults != null && maxResults > 0) {
            typedQuery.setMaxResults(maxResults.intValue());
            if (offset != null && offset > 0) {
                typedQuery.setFirstResult(offset.intValue());
            }
        }
    }

    private class StoredData<R> {
        final CriteriaBuilder cb;
        final CriteriaQuery<R> query;
        final Root<T> root;
        final Predicate predicate;

        StoredData(Class<R> r) {
            cb = entityManager.getCriteriaBuilder();
            this.query = cb.createQuery(r);
            root = query.from(type);
            predicate = toPredicate();
        }

        private StoredData<R> initWhere() {
            if (predicate != null) {
                query.where(predicate);
            }
            return this;
        }

        private StoredData<R> initGroupBy() {
            if (!criteria.getGroupings().isEmpty()) {
                List<Expression<?>> paths = criteria.getGroupings().stream()
                        .map(it -> JpaHelper.getPath(root, it.getNames()))
                        .collect(Collectors.toList());
                query.groupBy(paths);
            }
            return this;
        }

        private StoredData<R> initOrderBy() {
            ArrayList<Order> orders = new ArrayList<>();
            List<? extends Orders<T>> ordersList = criteria.getOrders();
            if ( !ordersList.isEmpty() ) {
                for ( Orders<T> order : ordersList ) {
                    Expression expression = JpaHelper.toExpression(order, cb, root);
                    switch (order.getDirection()) {
                        case DESC:
                            orders.add(cb.desc(expression));
                            break;
                        case ASC:
                            orders.add(cb.asc(expression));
                            break;
                        default:
                            throw new RuntimeException();
                    }
                }
                query.orderBy(orders);
            }
            return this;
        }

        private StoredData<R> initFetch() {
            List<? extends FetchAttribute<T>> list = criteria.getFetchAttributes();
            for (FetchAttribute<T> attr : list) {
                Fetch fetch = null;
                for (String stringPath : attr.getNames()) {
                    if (fetch == null) {
                        fetch = root.fetch(stringPath, attr.getJoinType());
                    } else {
                        fetch = fetch.fetch(stringPath, attr.getJoinType());
                    }
                }
            }
            return this;
        }

        private StoredData<R> initAll() {
            return initWhere().initGroupBy().initFetch().initOrderBy();
        }

        private Predicate toPredicate() {
            WhereClause<T> where = criteria.getWhereClause();
            return new SpecificationImpl<>(where).toPredicate(root, query, cb);
        }
    }

}
