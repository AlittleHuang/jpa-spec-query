package com.github.jpa.spec;

import lombok.Setter;
import lombok.experimental.Delegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CriteriaImpl<T> implements Criteria<T> {

    private static final Log logger = LogFactory.getLog(CriteriaImpl.class);

    @Delegate
    private final Conditions conditions;
    private Predicate.BooleanOperator operator = Predicate.BooleanOperator.AND;
    private Predicate predicate;

    private List<Criteria<T>> ands;
    private List<Criteria<T>> ors;

    CriteriaImpl(Root<T> root,
                        CriteriaQuery<?> query,
                        CriteriaBuilder criteriaBuilder) {
        this.conditions = new Conditions(root, query, criteriaBuilder, null, this);
    }

    public CriteriaImpl(EntityManager em, Class<T> entityType) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<?> query = criteriaBuilder.createQuery();
        Root<T> root = query.from(entityType);
        this.conditions = new Conditions(root, query, criteriaBuilder, em, this);
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

    private void add2And(Criteria<T> criteria) {
        if (ands == null) {
            ands = new ArrayList<>();
        }
        ands.add(criteria);
    }

    private void add2Or(Criteria<T> criteria) {
        if (ors == null) {
            ors = new ArrayList<>();
        }
        ors.add(criteria);
    }

    /**
     * 返回一个新的Criteria,与原Criteria以AND条件链接
     */
    @Override
    public Criteria<T> and() {
        CriteriaImpl<T> criteria = new CriteriaImpl<>(conditions);
        add2And(criteria);
        return criteria;
    }

    /**
     * 返回一个新的Criteria,与原Criteria以OR条件链接
     */
    @Override
    public Criteria<T> or() {
        CriteriaImpl<T> criteria = new CriteriaImpl<>(conditions);
        criteria.operator(Predicate.BooleanOperator.OR);
        add2Or(criteria);
        return criteria;
    }

    private Criteria<T> or(Predicate append) {
        predicate = appendOr(predicate, append);
        return this;
    }

    private Predicate appendOr(Predicate predicate, Predicate append) {
        Predicate result = predicate;
        if (result != null)
            result = conditions.cb.or(result, append);
        else
            result = conditions.cb.or(append);
        return result;
    }

    private Criteria<T> orNot(Predicate restriction) {
        if (predicate != null)
            predicate = conditions.cb.or(predicate, restriction.not());
        else
            predicate = conditions.cb.or(restriction.not());
        return this;
    }

    private Criteria<T> and(Predicate restriction) {
        predicate = appendAnd(predicate, restriction);
        return this;
    }

    private Predicate appendAnd(Predicate predicate, Predicate append) {
        Predicate result = predicate;
        if (result != null)
            result = conditions.cb.and(result, append);
        else
            result = conditions.cb.and(append);
        return result;
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
        return and(conditions.cb.equal(getPath(path), getPath(other)));
    }

    @Override
    public Criteria<T> orEqualAsPath(String path, String other) {
        return or(conditions.cb.equal(getPath(path), getPath(other)));
    }

    @Override
    public Criteria<T> andNotEqualAsPath(String path, String other) {
        return andNot(conditions.cb.equal(getPath(path), getPath(other)));
    }

    @Override
    public Criteria<T> orNotEqualAsPath(String path, String other) {
        return orNot(conditions.cb.equal(getPath(path), getPath(other)));
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
    public Criteria<T> orNotEqual(String name, Object value) {
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
    public Criteria<T> orNotEqIgnoreEmpty(String name, Object value) {
        return isEmpty(value) ? this
                : orNot(conditions.cb.equal(getPath(name), value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> andGe(
            String name,
            Y value,
            Class<Y> type) {
        return isEmpty(value) ? this
                : and(conditions.cb.greaterThanOrEqualTo(getPath(name).as(type), value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> andGe(String name, Y value) {
        Path path = getPath(name);
        //noinspection unchecked
        return isEmpty(value) ? this : and(conditions.cb.greaterThanOrEqualTo(path, value));
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
    public <Y extends Comparable<? super Y>> Criteria<T> orGe(String name, Y value) {
        Path path = getPath(name);
        //noinspection unchecked
        return isEmpty(value) ? this : or(conditions.cb.greaterThanOrEqualTo(path, value));
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
    public <Y extends Comparable<? super Y>> Criteria<T> andGt(String name, Y value) {
        Path path = getPath(name);
        //noinspection unchecked
        return isEmpty(value) ? this : and(conditions.cb.greaterThan(path, value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> orGt(
            String name,
            Y value,
            Class<Y> type) {
        return isEmpty(value) ? this : or(conditions.cb.greaterThan(getPath(name).as(type), value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> orGt(String name, Y value) {
        Path path = getPath(name);
        //noinspection unchecked
        return isEmpty(value) ? this : or(conditions.cb.greaterThan(path, value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> andLe(
            String name,
            Y value,
            Class<Y> type) {
        return isEmpty(value) ? this : and(conditions.cb.lessThanOrEqualTo(getPath(name).as(type), value));
    }


    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> andLe(String name, Y value) {
        Path path = getPath(name);
        //noinspection unchecked
        return isEmpty(value) ? this : and(conditions.cb.lessThanOrEqualTo(path, value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> orLe(
            String name,
            Y value,
            Class<Y> type) {
        return isEmpty(value) ? this : or(conditions.cb.lessThanOrEqualTo(getPath(name).as(type), value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> orLe(String name, Y value) {
        Path path = getPath(name);
        //noinspection unchecked
        return isEmpty(value) ? this : or(conditions.cb.lessThanOrEqualTo(path, value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> andLt(
            String name,
            Y value,
            Class<Y> type) {
        return isEmpty(value) ? this : and(conditions.cb.lessThan(getPath(name).as(type), value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> andLt(String name, Y value) {
        Path path = getPath(name);
        //noinspection unchecked
        return isEmpty(value) ? this : and(conditions.cb.lessThan(path, value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> orLt(
            String name,
            Y value,
            Class<Y> type) {
        return isEmpty(value) ? this : or(conditions.cb.lessThan(getPath(name).as(type), value));
    }

    @Override
    public <Y extends Comparable<? super Y>> Criteria<T> orLt(String name, Y value) {
        Path path = getPath(name);
        //noinspection unchecked
        return isEmpty(value) ? this : or(conditions.cb.lessThan(path, value));
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
    public Criteria<T> orIsNotNull(String name) {
        return or(conditions.cb.isNotNull(getPath(name)));
    }

    @Override
    public Criteria<T> orIsNull(String name) {
        return or(conditions.cb.isNull(getPath(name)));
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
        if (value.isEmpty()) {
            return andNotEqualAsPath(name, name);
        }
        if (value.size() == 1) {
            return andEqual(name, value.iterator().next());
        }
        CriteriaBuilder.In<Object> in = conditions.cb.in(getPath(name));
        for (X x : value) {
            in.value(x);
        }
        return and(in);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> Criteria<T> andIn(String name, X... value) {
        return andIn(name, Arrays.asList(value));
    }

    @Override
    public <X> Criteria<T> andNotIn(String name, Collection<X> value) {
        if (value.isEmpty()) return this;
        if (value.size() == 1) {
            return andNotEqual(name, value.iterator().next());
        }
        CriteriaBuilder.In<Object> in = conditions.cb.in(getPath(name));
        for (X x : value) {
            in.value(x);
        }
        return and(in.not());
    }

    @SafeVarargs
    @Override
    public final <X> Criteria<T> andNotIn(String name, X... value) {
        return andNotIn(name, Arrays.asList(value));
    }

    @Override
    public <X> Criteria<T> orIn(String name, Collection<X> value) {
        if (value.isEmpty()) {
            return orNotEqualAsPath(name, name);
        }
        if (value.size() == 1) {
            return orEqual(name, value.iterator().next());
        }
        CriteriaBuilder.In<Object> in = conditions.cb.in(getPath(name));
        for (X x : value) {
            in.value(x);
        }
        return or(in);
    }

    @SafeVarargs
    @Override
    public final <X> Criteria<T> orIn(String name, X... value) {
        return orIn(name, Arrays.asList(value));
    }

    @Override
    public <X> Criteria<T> orNotIn(String name, Collection<X> value) {
        if (value.isEmpty()) {
            return this;
        }
        if (value.size() == 1) {
            return orNotEqual(name, value.iterator().next());
        }
        CriteriaBuilder.In<Object> in = conditions.cb.in(getPath(name));
        for (X x : value) {
            in.value(x);
        }
        return or(in.not());
    }

    @SafeVarargs
    @Override
    public final <X> Criteria<T> orNotIn(String name, X... value) {
        return orNotIn(name, Arrays.asList(value));
    }

    @Override
    public Predicate toPredicate() {
        Predicate result = this.predicate;
        if (ands != null && !ands.isEmpty()) {
            result = appendAnd(result, toPredicate(ands));
        }
        if (ors != null && !ors.isEmpty()) {
            result = appendOr(result, toPredicate(ors));
        }
        if (result == null) {
            result = conditions.cb.and();
        }
        return result;
    }

    @Override
    public Criteria<T> setLockMode(LockModeType lockMode) {
        this.conditions.lockModeType = lockMode;
        return this;
    }

    @Override
    public Criteria<T> fetch(Getters<T, ?> getters) {
        String attributeName = getAttributeNameByGetter(getters);
        conditions.root.fetch(attributeName);
        return this;
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
    public Class<T> getType() {
        //noinspection unchecked
        return (Class<T>) conditions.root.getJavaType();
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
    public Predicate.BooleanOperator getOperator() {
        return operator;
    }

    private Path<?> getPath(String name) {
        return Criteria.getPath(conditions.root, name);
    }

    @Override
    public String getAttributeNameByGetter(Getters<T, ?> getters) {
        List<Getters> list = getters.list();
        //noinspection unchecked
        String result = getPropertyNameFromGetter(getType(), list.get(0));
        int size = list.size();
        if (size <= 1) {
            return result;
        }
        StringBuilder sb = new StringBuilder(result);
        String last = result;
        Path path = conditions.root;
        for (int i = 1; i < size; i++) {
            path = path.get(last);
            //noinspection unchecked
            last = getPropertyNameFromGetter(path.getJavaType(), list.get(i));
            sb.append('.').append(last);
        }
        return sb.toString();
    }

    private class Conditions {

        private final List<Selection<?>> selections = new ArrayList<>();

        private final List<Expression<?>> groupings = new ArrayList<>();

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

        @Setter
        private LockModeType lockModeType;
        private boolean emptyPage;

        private JpaEntityInformation entityInformation;

        private Conditions(Root<T> root,
                           CriteriaQuery<?> query,
                           CriteriaBuilder criteriaBuilder,
                           EntityManager entityManager,
                           Criteria<T> criteriaRoot) {
            this.cb = criteriaBuilder;
            this.root = root;
            this.query = query;
            this.criteriaRoot = criteriaRoot;
            this.entityManager = entityManager;

        }

        public Page<T> getPage(Pageable pageable) {
            if (emptyPage) {
                return Page.empty();
            }
            Assert.notNull(pageable, "pageable must not be null");
            long count = count();
            CriteriaQuery<T> entityQuery = initQuery();
            List<Order> orderList = new ArrayList<>();
            Sort sort = pageable.isPaged() ? pageable.getSort() : Sort.unsorted();

            if (sort != null) {
                for (Sort.Order order : sort) {
                    String property = order.getProperty();
                    switch (order.getDirection()) {
                        case ASC:
                            orderList.add(cb.asc(getPath(property)));
                            break;
                        case DESC:
                            orderList.add(cb.desc(getPath(property)));
                            break;
                    }
                }
            }
            List<Order> orders = entityQuery.getOrderList();
            orderList.addAll(orders);
            entityQuery.orderBy(orderList);
            TypedQuery<T> tQuery = entityManager.createQuery(entityQuery.select(root));
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

        public long count() {
            CriteriaQuery<?> query = initQuery();
            @SuppressWarnings("unchecked")
            CriteriaQuery<Long> countQuery = (CriteriaQuery<Long>) query;
            countQuery.select(cb.count(root));
            return entityManager.createQuery(countQuery).getSingleResult();
        }

        public boolean exists() {
            CriteriaQuery<?> query = initQuery();
            //noinspection unchecked
            query.select(root.get(getEntityInformation().getIdAttribute()));
            return !entityManager.createQuery(query)
                    .setLockMode(lockModeType == null ? LockModeType.NONE : lockModeType)
                    .setMaxResults(1)
                    .getResultList()
                    .isEmpty();
        }

        public List<?> getObjList() {
            return entityManager.createQuery(initQuery().multiselect(selections)).getResultList();
        }

        public List<T> getList() {
            return getRootQuery().getResultList();
        }

        public Pageable getPageable() {
            return pageable;
        }

        /**
         * 设置分页条件
         *
         * @param pageable 分页条件
         */
        private void setPageable(Pageable pageable) {
            this.pageable = pageable;
        }

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

        private void addSelectMax(String property) {
            Path<?> path = getPath(property);
            @SuppressWarnings("unchecked")
            Class<? extends Number> type = (Class<? extends Number>) path.getJavaType();
            selections.add(cb.max(path.as(type)));
        }


        private void addSelectSum(String property) {
            Path<?> path = getPath(property);
            @SuppressWarnings("unchecked")
            Class<? extends Number> type = (Class<? extends Number>) path.getJavaType();
            selections.add(cb.sum(path.as(type)));
        }

        private void groupBy(String... attributes) {
            for (String attr : attributes) {
                groupings.add(getPath(attr));
            }

        }

        /* 以下private方法 */

        private void initQuery(CriteriaQuery<?> query, Predicate predicate) {
            if (query == null)
                return;
            if (!orders.isEmpty())
                query.orderBy(orders);
            if (!groupings.isEmpty())
                query.groupBy(groupings);
            if (predicate != null)
                query.where(predicate);
        }

        @SuppressWarnings("unchecked")
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
            if (lockModeType != null) {
                typedQuery.setLockMode(lockModeType);
            }
            return typedQuery;
        }

        private JpaEntityInformation getEntityInformation() {
            if (entityInformation == null) {
                entityInformation = JpaEntityInformationSupport.getEntityInformation(root.getJavaType(), entityManager);
            }
            return entityInformation;
        }

        private Path<?> getPath(String name) {
            return Criteria.getPath(root, name);
        }
    }


    private final static Map<Class, String> map = new HashMap<>();

    private static <T> String getPropertyNameFromGetter(Class<T> type, Getters<T, ?> getters) {

        Class key = getters.getClass();

        String name = map.get(key);
        if (name != null) {
            return name;
        } else {
            synchronized (map) {

                name = map.get(key);
                if (name != null) {
                    return name;
                }

                name = Proxy.getPropertyName(type, getters);
                map.put(key, name);

                return name;

            }
        }

    }

    private static class Proxy implements MethodInterceptor {

        private static Map<Class<?>, Object> instanceMap = new ConcurrentHashMap<>();
        private static Proxy proxy = new Proxy();

        private static <T, U> String getGetterName(Class<T> type, Getters<T, U> getters) {
            T target = proxy.getProxyInstance(type);
            try {
                getters.apply(target);
            } catch (Exception e) {
                return e.getMessage();
            }
            throw new RuntimeException();
        }

        private static <T> String getPropertyName(Class<T> type, Getters<T, ?> getters) {
            String getterName = getGetterName(type, getters);
            boolean check = getterName != null && getterName.length() > 3 && getterName.startsWith("get");
            Assert.state(check, "the function is not getters");
            StringBuilder builder = new StringBuilder(getterName.substring(3));
            if (builder.length() == 1) {
                return builder.toString().toLowerCase();
            }
            if (builder.charAt(1) >= 'A' && builder.charAt(1) <= 'Z') {
                return builder.toString();
            }
            builder.setCharAt(0, Character.toLowerCase(builder.charAt(0)));
            return builder.toString();
        }

        private <T> T getProxyInstance(Class<T> type) {
            //noinspection unchecked
            return (T) instanceMap.computeIfAbsent(type, it -> {
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(type);
                enhancer.setCallback(this);
                return enhancer.create();
            });
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            throw new Exception(method.getName());
        }

    }
}
