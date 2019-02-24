package com.github.jpa.spec.query.impl;

import com.github.jpa.spec.query.api.FieldPath;
import com.github.jpa.spec.query.api.Orders;
import com.github.jpa.spec.query.api.WhereClause;
import com.github.jpa.spec.repostory.SpecificationImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JpaStored<T> extends AbstractStored<T> {
    public static final int DEFAULT_PAGE_SIZE = 10;

    private EntityManager entityManager;
    private Class<T> type;

    @Override
    public List<T> getResultList() {
        StoredData data = new StoredData().initAll();
        TypedQuery<T> typedQuery = entityManager.createQuery(data.query.select(data.root));
        setLimilt(typedQuery, criteria.getOffset(), criteria.getMaxResults());
        setLock(typedQuery);
        return typedQuery.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> List<X> getObjectList() {
        List<? extends FieldPath<T>> list = criteria.getSelections();
        if (list == null || list.isEmpty()) {
            return (List<X>) getResultList();
        }
        StoredData data = new StoredData().initAll();
        CriteriaQuery<T> query = data.query;
        List<Selection<?>> selections = list.stream()
                .map(it -> {
                    Path path = it.getPaths(data.root);
                    Class<?> type = path.getJavaType();
                    return path.as(type);
                }).collect(Collectors.toList());
        return (List<X>) entityManager.createQuery(query.multiselect(selections)).getResultList();
    }

    @Override
    public Page<T> getPage() {

        StoredData data = new StoredData()
                .initWhere()
                .initGroupBy();

        //noinspection unchecked
        CriteriaQuery<Long> countQuery = (CriteriaQuery<Long>) data.query;
        countQuery.select(data.cb.count(data.root));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        data.initFetch().initOrderBy();

        TypedQuery<T> typedQuery = entityManager.createQuery(data.query.select(data.root));

        // init pageable
        Integer offset = criteria.getOffset();
        Integer maxResults = criteria.getMaxResults();
        offset = offset == null ? 0 : offset;
        maxResults = maxResults == null ? DEFAULT_PAGE_SIZE : maxResults;
        PageRequest pageable = PageRequest.of(offset / maxResults, maxResults);
        setLimilt(typedQuery, offset, maxResults);

        setLock(typedQuery);

        List<T> resultList = typedQuery.getResultList();

        return new PageImpl<>(resultList, pageable, count);
    }

    @Override
    public long count() {
        StoredData data = new StoredData().initWhere().initGroupBy();
        //noinspection unchecked
        CriteriaQuery<Long> countQuery = (CriteriaQuery<Long>) data.query;
        countQuery.select(data.cb.count(data.root));

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    public boolean exists() {
        return false;
    }

    private TypedQuery<T> setLock(TypedQuery<T> typedQuery) {
        LockModeType lockModeType = criteria.getLockModeType();
        if (lockModeType != null) {
            typedQuery.setLockMode(lockModeType);
        }
        return typedQuery;
    }

    private TypedQuery<T> setLimilt(TypedQuery<T> typedQuery, Integer offset, Integer maxResults) {
        if (maxResults != null && maxResults > 0) {
            typedQuery.setMaxResults(maxResults);
            if (offset != null && offset > 0) {
                typedQuery.setFirstResult(offset);
            }
        }
        return typedQuery;
    }

    private class StoredData {//中间值
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(type);
        Root<T> root = query.from(type);
        Predicate predicate = toPredicate();

        private StoredData initWhere() {
            if (predicate != null) {
                query.where(predicate);
            }
            return this;
        }

        private StoredData initGroupBy() {
            if (!criteria.getGroupings().isEmpty()) {
                List<Expression<?>> paths = criteria.getGroupings().stream()
                        .map(it -> (it.getPaths(root)))
                        .collect(Collectors.toList());
                query.groupBy(paths);
            }
            return this;
        }

        private StoredData initOrderBy() {
            ArrayList<Order> orders = new ArrayList<>();
            if (!criteria.getOrders().isEmpty()) {
                for (Orders<T> order : criteria.getOrders()) {
                    switch (order.getDirection()) {
                        case DESC:
                            orders.add(cb.desc(order.getPaths(root)));
                            break;
                        case ASC:
                            orders.add(cb.asc(order.getPaths(root)));
                            break;
                        default:
                            throw new RuntimeException();
                    }
                }
                query.orderBy(orders);
            }
            return this;
        }

        private StoredData initFetch() {
            List<? extends FieldPath<T>> fetchs = criteria.getFetchs();
            for (FieldPath<T> fidld : fetchs) {
                Fetch fetch = null;
                for (String stringPath : fidld.getStringPaths(root)) {
                    if (fetch == null) {
                        fetch = root.fetch(stringPath);
                    } else {
                        fetch = fetch.fetch(stringPath);
                    }
                }
            }
            return this;
        }

        private StoredData initAll(){
            return initWhere().initGroupBy().initFetch().initOrderBy();
        }

        Predicate toPredicate() {
            WhereClause where = criteria.getWhereClause();
            return new SpecificationImpl(where).toPredicate(root, query, cb);
        }
    }
}
