package com.github.jpa.spec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.*;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CriteriaImpl<T> implements Criteria<T> {

    private static final Log logger = LogFactory.getLog(Criteria.class);

    private final Conditions<T> conditions;
    private Predicate.BooleanOperator operator = Predicate.BooleanOperator.AND;
    private Predicate predicate;

    List<Criteria<T>> criterias;

    public CriteriaImpl(Root<T> root, CriteriaQuery<?> query,
                        CriteriaBuilder criteriaBuilder) {
        this.conditions = new Conditions(root, query, criteriaBuilder, this);
    }

    public CriteriaImpl(EntityManager em, Class<T> entityType) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<?> query = criteriaBuilder.createQuery();
        Root<T> root = query.from(entityType);
        this.conditions = new Conditions<>(root, query, criteriaBuilder, this);
    }

    private CriteriaImpl(Conditions conditions) {
        this.conditions = conditions;
    }

    @Override
    public Criteria<T> operator(Predicate.BooleanOperator operator) {
        if (operator != null)
            this.operator = operator;
        return this;
    }


    private void add(Criteria<T> criteria) {
        if (criterias == null) {
            criterias = new ArrayList<>();
        }
        criterias.add(criteria);
    }

    /**
     * 返回一个新的Criteria,与原Criteria以AND条件链接
     */
    @Override
    public Criteria and() {
        CriteriaImpl<T> criteria = new CriteriaImpl<>(conditions);
        add(criteria);
        return criteria;
    }

    @Override
    public Criteria or() {
        CriteriaImpl<T> criteria = new CriteriaImpl<>(conditions);
        criteria.operator(Predicate.BooleanOperator.OR);
        add(criteria);
        return criteria;
    }

    private Criteria<T> or(Predicate restriction) {
        if (predicate != null)
            predicate = conditions.cb.or(predicate, restriction);
        else
            predicate = conditions.cb.or(restriction);
        return this;
    }

    private Criteria<T> orNot(Predicate restriction) {
        if (predicate != null)
            predicate = conditions.cb.or(predicate, restriction.not());
        else
            predicate = conditions.cb.or(restriction.not());
        return this;
    }

    private Criteria<T> and(Predicate restriction) {
        if (predicate != null)
            predicate = conditions.cb.and(predicate, restriction);
        else
            predicate = conditions.cb.and(restriction);
        return this;
    }

    private Criteria<T> andNot(Predicate restriction) {
        if (predicate != null)
            predicate = conditions.cb.and(predicate, restriction.not());
        else
            predicate = conditions.cb.and(restriction).not();
        return this;
    }

    @Override
    public Criteria<T> andEqual(String name, Object value) {
        return value == null ? this
                : and(conditions.cb.equal(getPath(name), value));
    }

    @Override
    public Criteria<T> andEqualAsPath(String path, String other) {
        return and(conditions.cb.equal(getPath(path), getPath(path)));
    }

    @Override
    public Criteria<T> orEqualAsPath(String path, String other) {
        return or(conditions.cb.equal(getPath(path), getPath(path)));
    }

    @Override
    public Criteria<T> andNotEqualAsPath(String path, String other) {
        return andNot(conditions.cb.equal(getPath(path), getPath(path)));
    }

    @Override
    public Criteria<T> orNotNotEqualAsPath(String path, String other) {
        return orNot(conditions.cb.equal(getPath(path), getPath(path)));
    }

    @Override
    public Criteria<T> orEqual(String name, Object value) {
        return value == null ? this : or(conditions.cb.equal(getPath(name), value));
    }

    @Override
    public Criteria<T> andNotEqual(String name, Object value) {
        return value == null ? this
                : andNot(conditions.cb.equal(getPath(name), value));
    }

    @Override
    public Criteria<T> orNotNotEqual(String name, Object value) {
        return value == null ? this
                : orNot(conditions.cb.equal(getPath(name), value));
    }


    public Criteria<T> andEqIgnoreEmpty(String name, Object value) {
        return isEmpty(value) ? this
                : and(conditions.cb.equal(getPath(name), value));
    }

    /**
     * 忽略空字符串
     */
    @Override
    public Criteria<T> orEqIgnoreEmpty(String name, Object value) {
        return isEmpty(value) ? this
                : or(conditions.cb.equal(getPath(name), value));
    }

    /**
     * 忽略空字符串
     */
    @Override
    public Criteria<T> andNotEqIgnoreEmpty(String name, Object value) {
        return isEmpty(value) ? this
                : andNot(conditions.cb.equal(getPath(name), value));
    }

    /**
     * 忽略空字符串
     */
    public Criteria<T> orNotNotEqIgnoreEmpty(String name, Object value) {
        return isEmpty(value) ? this
                : orNot(conditions.cb.equal(getPath(name), value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> andGe(
            String name,
            Y value,
            Class<Y> type) {
        return isEmpty(value) ? this
                : and(conditions.cb
                .greaterThanOrEqualTo(getPath(name).as(type), value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> orGe(
            String name,
            Y value,
            Class<Y> type) {
        return isEmpty(value) ? this
                : or(conditions.cb
                .greaterThanOrEqualTo(getPath(name).as(type), value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> andGt(
            String name,
            Y value,
            Class<Y> type) {
        return isEmpty(value) ? this : and(
                conditions.cb.greaterThan(getPath(name).as(type), value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> orGt(
            String name,
            Y value,
            Class<Y> type) {
        return isEmpty(value) ? this : or(conditions.cb.greaterThan(getPath(name).as(type), value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> andLe(
            String name,
            Y value,
            Class<Y> type) {
        return isEmpty(value) ? this : and(conditions.cb.lessThanOrEqualTo(getPath(name).as(type), value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> orLe(
            String name,
            Y value,
            Class<Y> type) {
        return isEmpty(value) ? this : or(conditions.cb.lessThanOrEqualTo(getPath(name).as(type), value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> andLt(
            String name,
            Y value,
            Class<Y> type) {
        return isEmpty(value) ? this : and(conditions.cb.lessThan(getPath(name).as(type), value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> orLt(
            String name,
            Y value,
            Class<Y> type) {
        return isEmpty(value) ? this : or(conditions.cb.lessThan(getPath(name).as(type), value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> andBetween(
            String name,
            Y value0,
            Y value1,
            Class<Y> type) {
        if (value0 == null || value1 == null) return null;
        return and(conditions.cb.between(getPath(name).as(type), value0, value1));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> orBetween(
            String name,
            Y value0,
            Y value1,
            Class<Y> type) {
        if (value0 == null || value1 == null) return null;
        return or(conditions.cb.between(getPath(name).as(type), value0, value1));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> andNotBetween(
            String name,
            Y value0,
            Y value1,
            Class<Y> type) {
        if (value0 == null || value1 == null) return null;
        return and(conditions.cb.between(getPath(name).as(type), value0, value1).not());
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> orNotBetween(
            String name,
            Y value0,
            Y value1,
            Class<Y> type) {
        if (value0 == null || value1 == null) return null;
        return or(conditions.cb.between(getPath(name).as(type), value0, value1).not());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> andBetween(
            String name,
            Y value0,
            Y value1) {
        return and(conditions.cb.between((Path<? extends Y>) getPath(name), value0, value1));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> orBetween(
            String name,
            Y value0,
            Y value1) {
        if (value0 == null || value1 == null) return null;
        return or(conditions.cb.between((Path<? extends Y>) getPath(name), value0, value1));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> andNotBetween(
            String name,
            Y value0,
            Y value1) {
        if (value0 == null || value1 == null) return null;
        return and(conditions.cb.between((Expression<? extends Y>) getPath(name), value0, value1).not());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> orNotBetween(
            String name,
            Y value0,
            Y value1) {
        if (value0 == null || value1 == null) return null;
        return or(conditions.cb.between((Expression<? extends Y>) getPath(name), value0, value1).not());
    }

    @Override
    public Criteria<T> andIsNull(String name) {
        return and(conditions.cb.isNull(getPath(name)));
    }

    @Override
    public Criteria<T> andIsNotNull(String name) {
        return and(conditions.cb.isNotNull(getPath(name)));
    }

    @Override
    public Criteria<T> andLike(String name, String value) {
        return isEmpty(value) ? this : and(conditions.cb.like(getPath(name).as(String.class), value));
    }

    @Override
    public Criteria<T> andNotLike(String name, String value) {
        return isEmpty(value) ? this : and(conditions.cb.like(getPath(name).as(String.class), value).not());
    }

    @Override
    public Criteria<T> orLike(String name, String value) {
        return isEmpty(value) ? this : or(conditions.cb.like(getPath(name).as(String.class), value));
    }

    @Override
    public Criteria<T> orNotLike(String name, String value) {
        return isEmpty(value) ? this : or(conditions.cb.like(getPath(name).as(String.class), value).not());
    }

    @Override
    public <X> Criteria<T> andIn(String name, Collection<X> value) {
        if (value.isEmpty()) return this;
        CriteriaBuilder.In<Object> in = conditions.cb.in(getPath(name));
        for (X x : value) {
            in.value(x);
        }
        return and(in);
    }

    @Override
    public <X> Criteria<T> andIn(String name, X... value) {
        CriteriaBuilder.In<Object> in = conditions.cb.in(getPath(name));
        for (X x : value) {
            in.value(x);
        }
        return and(in);
    }

    @Override
    public <X> Criteria<T> andNotIn(String name, Collection<X> value) {
        //if (value.isEmpty()) return this;
        CriteriaBuilder.In<Object> in = conditions.cb.in(getPath(name));
        for (X x : value) {
            in.value(x);
        }
        return and(in.not());
    }

    @Override
    public <X> Criteria<T> andNotIn(String name, X[] value) {
        CriteriaBuilder.In<Object> in = conditions.cb.in(getPath(name));
        for (X x : value) {
            in.value(x);
        }
        return and(in.not());
    }

    @Override
    public <X> Criteria<T> orIn(String name, Collection<X> value) {
        if (value.isEmpty()) return this;
        CriteriaBuilder.In<Object> in = conditions.cb.in(getPath(name));
        for (X x : value) {
            in.value(x);
        }
        return or(in);
    }

    @Override
    public <X> Criteria<T> orIn(String name, X[] value) {
        CriteriaBuilder.In<Object> in = conditions.cb.in(getPath(name));
        for (X x : value) {
            in.value(x);
        }
        return or(in);
    }

    @Override
    public <X> Criteria<T> orNotIn(String name, Collection<X> value) {
        if (value.isEmpty()) return this;
        CriteriaBuilder.In<Object> in = conditions.cb.in(getPath(name));
        for (X x : value) {
            in.value(x);
        }
        return or(in.not());
    }

    @Override
    public <X> Criteria<T> orNotIn(String name, X[] value) {
        CriteriaBuilder.In<Object> in = conditions.cb.in(getPath(name));
        for (X x : value) {
            in.value(x);
        }
        return or(in.not());
    }

    @Override
    public Predicate toPredicate() {
        if (criterias == null)
            return predicate;
        if (predicate != null)
            return conditions.cb.and(predicate, toPredicate(criterias));
        return toPredicate(criterias);
    }

    private boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    @Override
    public Criteria<T> limit(int offset, int maxResults) {
        conditions.limit(offset, maxResults);
        return this;
    }

    @Override
    public Criteria<T> limit(int maxResults) {
        conditions.limit(maxResults);
        return this;
    }

    @Override
    public Criteria<T> limitMaxIndex(int maxIndex) {
        conditions.limitMaxIndex(maxIndex);
        return this;
    }

    @Override
    public Criteria<T> setPageable(int page, int size) {
        conditions.setPageable(page, size);
        return this;
    }

    @Override
    public Criteria<T> setPageable(Pageable pageable) {
        conditions.setPageable(pageable);
        return this;
    }

    @Override
    public Criteria<T> addOrderByDesc(String... attributeNames) {
        conditions.addOrderByDesc(attributeNames);
        return this;
    }

    @Override
    public Criteria<T> addOrderByAsc(String... attributeNames) {
        conditions.addOrderByAsc(attributeNames);
        return this;
    }

    @Override
    public void setPageResultEmpty() {
        conditions.setPageResultEmpty();
    }

    @Override
    public Criteria<T> groupBy(String... attributes) {
        conditions.groupBy(attributes);
        return this;
    }

    @Override
    public Criteria<T> criteria() {
        conditions.criteria();
        return this;
    }

    @Override
    public Criteria<T> addSelect(String property) {
        conditions.addSelect(property);
        return this;
    }

    @Override
    public Criteria<T> addSelectMin(String property) {
        conditions.addSelectMin(property);
        return this;
    }

    @Override
    public Criteria<T> addSelectMax(String property) {
        conditions.addSelectMax(property);
        return this;
    }

    @Override
    public Criteria<T> addSelectSum(String property) {
        conditions.addSelectSum(property);
        return this;
    }

    @Override
    public Predicate toPredicate(List<Criteria<T>> criteriaList) {
        return conditions.toPredicate(criteriaList);
    }

    @Override
    public Page<T> getPage(Pageable pageable) {
        return conditions.getPage(pageable);
    }

    @Override
    public Pageable getPageable() {
        return conditions.getPageable();
    }

    @Override
    public long count() {
        return conditions.count();
    }

    @Override
    public List<?> getObjList() {
        return conditions.getObjList();
    }

    @Override
    public List<T> getList() {
        return conditions.getList();
    }

    @Override
    public Predicate.BooleanOperator getOperator() {
        return operator;
    }

    private Path<?> getPath(String name) {
        return Criteria.getPath(conditions.root, name);
    }

    static class Conditions<T> {

        private final List<Selection<?>> selections = new ArrayList<>();

        private final List<Expression<?>> grouping = new ArrayList<>();

        private final List<Order> orders = new ArrayList<>();

        private final Criteria<T> criteriaRoot;

        private Integer offset;

        private Integer maxResults;

        private Integer maxIndex;

        private EntityManager entityManager;

        private Pageable pageable;

        private CriteriaBuilder cb;

        private Root<T> root;

        private CriteriaQuery<?> query;


        private CriteriaQuery<?> getCriteriaQuery() {
            return query;
        }


        private CriteriaBuilder getCb() {
            return cb;
        }


        private Root<T> getRoot() {
            return root;
        }


        private Page<T> getPage(Pageable pageable) {
            if (emptyPage) {
                return Page.empty();
            }
            Assert.notNull(pageable, "pageable must not be null");
            long count = count();
            CriteriaQuery<T> entityQuery = initQuery();
            List<Order> pOrders = new ArrayList<>();
            Sort sort = pageable.getSort();
            if (sort != null) {
                for (Sort.Order order : sort) {
                    String property = order.getProperty();
                    switch (order.getDirection()) {
                        case ASC:
                            pOrders.add(cb.asc(getPath(property)));
                            break;
                        case DESC:
                            pOrders.add(cb.desc(getPath(property)));
                            break;
                    }
                }
            }
            List<Order> orders = entityQuery.getOrderList();
            pOrders.addAll(orders);
            entityQuery.orderBy(pOrders);
            TypedQuery<T> tQuery = getRootQuery();
            tQuery.setFirstResult((int) pageable.getOffset());
            int pageSize = pageable.getPageSize();
            if (maxIndex != null) {
                int maxsize = maxIndex - (int) pageable.getOffset();
                if (maxsize < pageSize) {
                    pageSize = maxsize < 0 ? 0 : maxsize;
                }
            }
            tQuery.setMaxResults(pageSize);
            List<T> resultList = tQuery.getResultList();
            Page<T> page = new PageImpl<>(resultList, pageable, count);
            entityQuery.orderBy(orders);
            return page;
        }


        private long count() {
            CriteriaQuery<?> query = initQuery();
            @SuppressWarnings("unchecked")
            CriteriaQuery<Long> countQuery = (CriteriaQuery<Long>) query;
            countQuery.select(cb.count(root));
            return entityManager.createQuery(countQuery).getSingleResult();
        }


        private List<?> getObjList() {
            return entityManager.createQuery(initQuery()).getResultList();
        }


        private List<T> getList() {
            return getRootQuery().getResultList();
        }


        private Pageable getPageable() {
            return pageable;
        }


        private Conditions(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, Criteria<T> criteriaRoot) {
            this.cb = criteriaBuilder;
            this.root = root;
            this.query = query;
            this.criteriaRoot = criteriaRoot;
        }

        private boolean emptyPage;

        /**
         * 设置分页条件
         *
         * @param offset     开始位置
         * @param maxResults 每页数量
         */

        private void limit(int offset, int maxResults) {
            this.offset = offset;
            this.maxResults = maxResults;
        }

        /**
         * 设置最多查询数量
         *
         * @param maxResults 最多查询数量
         */

        private void limit(int maxResults) {
            this.maxResults = maxResults;
        }


        private void limitMaxIndex(int maxIndex) {
            this.maxIndex = maxIndex;
        }

        /**
         * 设置分页条件
         *
         * @param page 页码
         * @param size 每页最大记录
         */

        private void setPageable(int page, int size) {
            pageable = PageRequest.of(page - 1, size);
        }

        /**
         * 设置分页条件
         *
         * @param pageable 分页条件
         */

        private void setPageable(Pageable pageable) {
            this.pageable = pageable;
        }


        private CriteriaBuilder cb() {
            return cb;
        }

        private Root<T> root() {
            return root;
        }


        private Criteria<T> criteria() {
            return criteriaRoot.and();
        }


        private Criteria<T> or() {
            return criteria().operator(Predicate.BooleanOperator.OR);
        }


        private Criteria<T> and() {
            return criteria();
        }


        private void addOrderByDesc(String... attributeNames) {
            for (String attributeName : attributeNames) {
                orders.add(cb.desc(getPath(attributeName)));
            }
        }


        private void addOrderByAsc(String... attributeNames) {
            for (String attributeName : attributeNames) {
                orders.add(cb.asc(getPath(attributeName)));
            }
        }

        private Predicate toPredicate(List<Criteria<T>> criteriaList) {
            CriteriaImpl<T> criteria = new CriteriaImpl<>(this);
            for (Criteria<T> c : criteriaList) {
                Predicate predicate = c.toPredicate();
                if (predicate == null)
                    continue;
                switch (c.getOperator()) {
                    case AND:
                        criteria.and(predicate);
                        break;
                    case OR:
                        criteria.or(predicate);
                        break;
                    default:
                }
            }
            return criteria.toPredicate();
        }


        private void setPageResultEmpty() {
            emptyPage = true;
        }

        private void addSelect(String property) {
            Path<?> path = getPath(property);
            Class<?> type = path.getJavaType();
            selections.add(path.as(type));
        }

        private void addSelectMin(String property) {
            Path<?> path = getPath(property);
            @SuppressWarnings("unchecked")
            Class<? extends Number> type = (Class<? extends Number>) path.getJavaType();
            cb.min(path.as(type));
        }

        /**
         *
         */

        private void addSelectMax(String property) {
            Path<?> path = getPath(property);
            @SuppressWarnings("unchecked")
            Class<? extends Number> type = (Class<? extends Number>) path.getJavaType();
            selections.add(cb.max(path.as(type)));
        }

        /**
         * selel
         */

        private void addSelectSum(String property) {
            Path<?> path = getPath(property);
            @SuppressWarnings("unchecked")
            Class<? extends Number> type = (Class<? extends Number>) path.getJavaType();
            selections.add(cb.sum(path.as(type)));
        }

        /**
         *
         */

        private void groupBy(String... attributes) {
            for (String attr : attributes) {
                grouping.add(getPath(attr));
            }

        }
        /*  end */

        /* 以下private方法 */

        @SuppressWarnings("unchecked")
        private void initQuery(CriteriaQuery<?> query, Predicate predicate) {
            if (query == null)
                return;
            if (!orders.isEmpty())
                query.orderBy(orders);
            if (!grouping.isEmpty())
                query.groupBy(grouping);
            if (selections.isEmpty()) {
                ((CriteriaQuery<T>) query).select(root);
            } else
                query.multiselect(selections);
            if (predicate != null)
                query.where(predicate);
        }

        private CriteriaQuery<T> initQuery() {
            initQuery(query, criteriaRoot.toPredicate());
            return (CriteriaQuery<T>) query;
        }

        private TypedQuery<T> getRootQuery() {
            TypedQuery<T> typedQuery = entityManager.createQuery(initQuery().select(root));
            if (maxResults != null && maxResults > 0) {
                typedQuery.setMaxResults(maxResults);
                if (offset != null && offset > 0) {
                    typedQuery.setFirstResult(offset);
                }
            }
            return typedQuery;
        }

        private Path<?> getPath(String name) {
            return Criteria.getPath(root, name);
        }
    }
}
