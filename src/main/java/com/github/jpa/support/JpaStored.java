package com.github.jpa.support;

import com.github.data.query.specification.FieldPath;
import com.github.data.query.specification.Orders;
import com.github.data.query.specification.WhereClause;
import com.github.data.query.support.AbstractStored;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class JpaStored<T> extends AbstractStored<T> {
    public static final int DEFAULT_PAGE_SIZE = 10;

    private EntityManager entityManager;
    private Class<T> type;

    public JpaStored(EntityManager entityManager, Class<T> type) {
        this.entityManager = entityManager;
        this.type = type;
    }

    @Override
    public List<T> getResultList() {
        StoredData<T> data = new StoredData<>(type).initAll();
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
        StoredData<?> data = new StoredData<>(Object.class).initWhere().initGroupBy().initOrderBy();
        CriteriaQuery<?> query = data.query;
        List selections = list.stream()
                .map(it -> {
                    Path path = it.getPaths(data.root);
                    Class<?> type = path.getJavaType();
                    return path.as(type);
                }).collect(Collectors.toList());
        TypedQuery typedQuery = entityManager
                .createQuery(query.multiselect(selections));
        setLimilt(typedQuery, criteria.getOffset(), criteria.getMaxResults());
        setLock(typedQuery);
        return (List<X>) typedQuery.getResultList();
    }

    @Override
    public Page<T> getPage(int page, int size) {
        long count = count();
        if (count == 0) {
            return Page.empty();
        }
        StoredData<T> data = new StoredData<>(type).initAll();
        TypedQuery<T> typedQuery = entityManager.createQuery(data.query.select(data.root));

        PageRequest pageable = PageRequest.of(page, size);
        setLimilt(typedQuery, (int) pageable.getOffset(), size);

        setLock(typedQuery);
        List<T> resultList = typedQuery.getResultList();

        return new PageImpl<>(resultList, pageable, count);
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
        JpaEntityInformation<T, ?> information = gettJpaEntityInformation(data);
        data.query.select(data.root.get(information.getIdAttribute()));
        return !entityManager.createQuery(data.query)
                .setMaxResults(1)
                .getResultList()
                .isEmpty();
    }

    private static final Map<Class, JpaEntityInformation> INFORMATION_MAP = new ConcurrentHashMap<>();

    private JpaEntityInformation<T, ?> gettJpaEntityInformation(StoredData<Object> data) {
        //noinspection unchecked
        return INFORMATION_MAP.computeIfAbsent(
                data.root.getJavaType(),
                type -> JpaEntityInformationSupport.getEntityInformation(type, entityManager)
        );
    }

    private void setLock(TypedQuery<T> typedQuery) {
        LockModeType lockModeType = criteria.getLockModeType();
        if (lockModeType != null) {
            typedQuery.setLockMode(lockModeType);
        }
    }

    private void setLimilt(TypedQuery<T> typedQuery, Integer offset, Integer maxResults) {
        if (maxResults != null && maxResults > 0) {
            typedQuery.setMaxResults(maxResults);
            if (offset != null && offset > 0) {
                typedQuery.setFirstResult(offset);
            }
        }
    }

    private class StoredData<R> {//中间值
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
                        .map(it -> (it.getPaths(root)))
                        .collect(Collectors.toList());
                query.groupBy(paths);
            }
            return this;
        }

        private StoredData<R> initOrderBy() {
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

        private StoredData<R> initFetch() {
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

        private StoredData<R> initAll() {
            return initWhere().initGroupBy().initFetch().initOrderBy();
        }

        Predicate toPredicate() {
            WhereClause where = criteria.getWhereClause();
            return new SpecificationImpl<T>(where).toPredicate(root, query, cb);
        }
    }
}
