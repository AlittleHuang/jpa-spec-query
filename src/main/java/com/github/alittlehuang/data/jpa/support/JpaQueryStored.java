package com.github.alittlehuang.data.jpa.support;

import com.github.alittlehuang.data.jpa.util.JpaHelper;
import com.github.alittlehuang.data.metamodel.EntityInformation;
import com.github.alittlehuang.data.query.page.Page;
import com.github.alittlehuang.data.query.page.PageImpl;
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
import java.util.*;
import java.util.stream.Collectors;

public class JpaQueryStored<T> extends AbstractQueryStored<T> {

    private EntityManager entityManager;

    public JpaQueryStored(EntityManager entityManager, Class<T> type) {
        this.entityManager = entityManager;
        this.type = type;
    }

    @Override
    public List<T> getResultList() {
        return new StoredData<>(type).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> List<X> getObjectList() {
        List<? extends Selection<T>> list = criteria.getSelections();
        if (list == null || list.isEmpty()) {
            return (List<X>) getResultList();
        }
        return new StoredData<>(Object.class).getObjectList();
    }

    @Override
    public Page<T> getPage(long page, long size) {
        long count = count();
        Pageable pageable = new Pageable((int) page, (int) size);
        if (count == 0) {
            return Page.empty(pageable);
        }
        return new StoredData<>(type).getPage(pageable, count);
    }

    @Override
    public long count() {
        return new StoredData<>(Long.class).count();
    }

    @Override
    public boolean exists() {
        return new StoredData<>(Object.class).exists();
    }

    private class StoredData<R> {
        final CriteriaBuilder cb;
        final CriteriaQuery<R> query;
        final Root root;
        final Predicate predicate;
        final Map<ParameterExpression, Object> param = new HashMap<>();

        StoredData(Class<R> r) {
            cb = entityManager.getCriteriaBuilder();
            this.query = cb.createQuery(r);
            root = query.from(type);
            predicate = toPredicate();
        }

        public List<R> getResultList() {
            initWhere().initGroupBy().initFetch();
            //noinspection unchecked
            TypedQuery<R> typedQuery = entityManager.createQuery(query.select(root));
            setLimit(typedQuery, criteria.getOffset(), criteria.getMaxResults());
            setLock(typedQuery);
            setParameter(typedQuery);
            return typedQuery.getResultList();
        }

        @SuppressWarnings( "unchecked" )
        public <X> List<X> getObjectList() {
            initWhere().initGroupBy().initOrderBy();
            List<? extends Selection<T>> list = criteria.getSelections();
            List selections = list.stream()
                    .map(it -> {

                        Expression expression = toExpression(it, cb, root);
                        AggregateFunctions aggregate = it.getAggregateFunctions();

                        switch ( aggregate == null ? AggregateFunctions.NONE : aggregate ) {
                            case NONE:
                                return expression;
                            case AVG:
                                return cb.avg(expression);
                            case SUM:
                                return cb.sum(expression);
                            case MAX:
                                return cb.max(expression);
                            case MIN:
                                return cb.min(expression);
                            case COUNT:
                                return cb.count(expression);
                            default:
                                return expression;
                        }

                    }).collect(Collectors.toList());
            TypedQuery typedQuery = entityManager
                    .createQuery(query.multiselect(selections));
            setLimit(typedQuery, criteria.getOffset(), criteria.getMaxResults());
            setLock(typedQuery);
            setParameter(typedQuery);
            return (List<X>) typedQuery.getResultList();
        }

        public Page<R> getPage(Pageable pageable, long count) {
            initFetch().initWhere().initOrderBy();
            //noinspection unchecked
            TypedQuery<R> typedQuery = entityManager.createQuery(query.select(root));
            setLimit(typedQuery, pageable.getOffset(), pageable.getPageSize());
            setLock(typedQuery);
            setParameter(typedQuery);
            List<R> resultList = typedQuery.getResultList();
            return new PageImpl<>(resultList, pageable, count);
        }

        public long count() {

            initWhere().initGroupBy();
            //noinspection unchecked
            CriteriaQuery<Long> countQuery = (CriteriaQuery<Long>) query;
            countQuery.select(cb.count(root));
            TypedQuery<Long> query = entityManager.createQuery(countQuery);
            setParameter(query);
            return query.getSingleResult();

        }

        public boolean exists() {
            initWhere().initGroupBy();
            EntityInformation<T, ?> information = EntityInformation.getInstance(type);
            //noinspection unchecked
            query.select(root.get(information.getIdAttribute().getFieldName()));
            TypedQuery<R> query = entityManager.createQuery(this.query);
            setParameter(query);
            return !query.setMaxResults(1)
                    .getResultList()
                    .isEmpty();
        }

        private void setLock(TypedQuery<R> typedQuery) {
            LockModeType lockModeType = criteria.getLockModeType();
            if ( lockModeType != null ) {
                typedQuery.setLockMode(lockModeType);
            }
        }

        private void setLimit(TypedQuery<R> typedQuery, Long offset, Long maxResults) {
            if ( maxResults != null && maxResults > 0 ) {
                typedQuery.setMaxResults(maxResults.intValue());
                if ( offset != null && offset > 0 ) {
                    typedQuery.setFirstResult(offset.intValue());
                }
            }
        }

        private StoredData<R> initWhere() {
            if (predicate != null) {
                query.where(predicate);
            }
            return this;
        }

        private StoredData<R> initGroupBy() {
            if (!criteria.getGroupings().isEmpty()) {
                //noinspection unchecked
                List paths = criteria.getGroupings().stream()
                        .map(it -> toExpression(it, cb, (Root<T>) root))
                        .collect(Collectors.toList());
                //noinspection unchecked
                query.groupBy((List<Expression<?>>) paths);
            }
            return this;
        }

        private StoredData<R> initOrderBy() {
            ArrayList<Order> orders = new ArrayList<>();
            List<? extends Orders<T>> ordersList = criteria.getOrders();
            if ( !ordersList.isEmpty() ) {
                for ( Orders<T> order : ordersList ) {
                    //noinspection unchecked
                    Expression expression = toExpression(order, cb, root);
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

        void setParameter(TypedQuery<?> query) {
            for ( Map.Entry<ParameterExpression, Object> e : param.entrySet() ) {
                //noinspection unchecked
                query.setParameter(e.getKey(), e.getValue());
            }
        }

        private Predicate toPredicate() {
            WhereClause<T> where = criteria.getWhereClause();
            //noinspection unchecked
            return new SpecificationImpl(where).toPredicate(root, query, cb);
        }

        public <X> javax.persistence.criteria.Expression toExpression(com.github.alittlehuang.data.query.specification.Expression<X> expression, CriteriaBuilder cb, Root<X> root) {
            com.github.alittlehuang.data.query.specification.Expression<X> subexpression = expression.getSubexpression();
            javax.persistence.criteria.Expression exp = ( subexpression != null )
                    ? toExpression(subexpression, cb, root)
                    : toPath(root, expression);

            com.github.alittlehuang.data.query.specification.Expression.Function type = expression.getFunction();
            if ( type == null ) type = com.github.alittlehuang.data.query.specification.Expression.Function.NONE;

            Object[] args = expression.getArgs();
            args = args == null ? com.github.alittlehuang.data.query.specification.Expression.EMPTY_ARGS : args;

            javax.persistence.criteria.Expression result = exp;

            switch ( type ) {

                case NONE:
                    break;
                case ABS:
                    //noinspection unchecked
                    result = cb.abs(exp);
                    break;


                case SUM:
                    if ( isFirstArgNotAttrExpression(args) ) {
                        //noinspection unchecked
                        result = cb.sum(exp, (Number) args[0]);
                    } else
                        //noinspection unchecked
                        result = cb.sum(exp, getExpression(cb, root, args));
                    break;


                case PROD:
                    if ( isFirstArgNotAttrExpression(args) ) {
                        //noinspection unchecked
                        result = cb.prod(exp, (Number) args[0]);
                    } else
                        //noinspection unchecked
                        result = cb.prod(exp, getExpression(cb, root, args));
                    break;


                case DIFF:
                    if ( isFirstArgNotAttrExpression(args) ) {
                        //noinspection unchecked
                        result = cb.diff(exp, (Number) args[0]);
                    } else
                        //noinspection unchecked
                        result = cb.diff(exp, getExpression(cb, root, args));
                    break;


                case QUOT:
                    if ( isFirstArgNotAttrExpression(args) ) {
                        //noinspection unchecked
                        result = cb.quot(exp, (Number) args[0]);
                    } else
                        //noinspection unchecked
                        result = cb.quot(exp, getExpression(cb, root, args));
                    break;


                case MOD:
                    if ( isFirstArgNotAttrExpression(args) ) {
                        //noinspection unchecked
                        result = cb.mod(exp, (Integer) args[0]);
                    } else
                        //noinspection unchecked
                        result = cb.mod(exp, getExpression(cb, root, args));
                    break;


                case SQRT:
                    //noinspection unchecked
                    result = cb.sqrt(exp);
                    break;


                case CONCAT:
                    if ( isFirstArgNotAttrExpression(args) ) {
                        //noinspection unchecked
                        result = cb.concat(exp, (String) args[0]);
                    } else
                        //noinspection unchecked
                        result = cb.concat(exp, getExpression(cb, root, args));
                    break;


                case SUBSTRING:
                    if ( args.length == 2 ) {
                        //noinspection unchecked
                        result = cb.substring(exp, (Integer) args[0], (Integer) args[1]);
                    } else if ( args.length == 1 ) {
                        //noinspection unchecked
                        result = cb.substring(exp, (Integer) args[0]);
                    }
                    break;


                case TRIM:
                    if ( args == null || args.length == 0 ) {
                        //noinspection unchecked
                        result = cb.trim(exp);
                    } else if ( args.length == 1 ) {
                        //noinspection unchecked
                        result = cb.trim((CriteriaBuilder.Trimspec) args[0], exp);
                    } else if ( args.length == 2 ) {
                        //noinspection unchecked
                        result = cb.trim((CriteriaBuilder.Trimspec) args[0], (char) args[1], exp);
                    }
                    break;


                case LOWER:
                    //noinspection unchecked
                    result = cb.lower(exp);
                    break;


                case UPPER:
                    //noinspection unchecked
                    result = cb.upper(exp);
                    break;


                case LENGTH:
                    //noinspection unchecked
                    result = cb.length(exp);
                    break;


                case LOCATE:
                    if ( args[0] instanceof AttributePath ) {
                        //noinspection unchecked
                        result = cb.locate(exp, getExpression(cb, root, args));
                    } else {
                        //noinspection unchecked
                        result = cb.locate(exp, (String) args[0]);
                    }
                    break;


                case COALESCE:
                    if ( args[0] instanceof AttributePath ) {
                        result = cb.coalesce(exp, getExpression(cb, root, args));
                    } else {
                        result = cb.coalesce(exp, args[0]);
                    }
                    break;


                case NULLIF:
                    if ( isFirstArgNotAttrExpression(args) ) {
                        //noinspection unchecked
                        result = cb.nullif(exp, args[0]);
                    } else {
                        result = cb.nullif((javax.persistence.criteria.Expression<?>) exp, getExpression(cb, root, args));
                    }
                    break;
                case CUSTOMIZE: {
                    javax.persistence.criteria.Expression[] expressions = new javax.persistence.criteria.Expression[args.length + 1];
                    int index = 0;
                    expressions[index++] = exp;
                    for ( Object arg : args ) {
                        ParameterExpression<?> parameter = cb.parameter(arg.getClass());
                        expressions[index++] = parameter;
                        param.put(parameter, arg);
                    }
                    result = cb.function(expression.getFunctionName(), Object.class, expressions);
                }
            }
            return result;
        }

        private javax.persistence.criteria.Expression getExpression(CriteriaBuilder cb, Root<?> root, Object[] args) {
            //noinspection unchecked
            return toExpression((com.github.alittlehuang.data.query.specification.Expression) args[0], cb, root);
        }

        private boolean isFirstArgNotAttrExpression(Object[] args) {
            return args == null || args.length == 0 || args[0] == null || !( args[0] instanceof com.github.alittlehuang.data.query.specification.Expression );
        }

        private <X> Path<?> toPath(Root<X> root, AttributePath attribute) {
            return JpaHelper.getPath(root, attribute.getNames());
        }

        public class SpecificationImpl/* implements Specification<T>*/ {
            private final WhereClause<T> whereClause;

            public SpecificationImpl(WhereClause<T> whereClause) {
                this.whereClause = whereClause;
            }

            //@Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return new PredicateBuilder(root, query, criteriaBuilder, whereClause).toPredicate();
            }

            private class PredicateBuilder {
                private final Root<T> root;
                private final CriteriaQuery<?> query;
                private final CriteriaBuilder cb;
                private final WhereClause<T> item;
                private Predicate predicate;

                private PredicateBuilder(Root<T> root, CriteriaQuery query, CriteriaBuilder criteriaBuilder,
                                         WhereClause<T> item) {
                    this.root = root;
                    this.query = query;
                    this.cb = criteriaBuilder;
                    this.item = item;
                }

                private Predicate toPredicate() {
                    if ( !item.isCompound() ) {
                        build();
                    } else {
                        recursiveBuild();
                    }
                    return predicate == null
                            ? null
                            : ( item.isNegate() ? predicate.not() : predicate );
                }

                private void recursiveBuild() {
                    List<? extends WhereClause<T>> subItems = item.getCompoundItems();
                    if ( subItems != null ) {
                        for ( WhereClause<T> item : subItems ) {
                            Predicate predicateItem = new PredicateBuilder(root, query, cb, item).toPredicate();
                            if ( this.predicate == null ) {
                                this.predicate = predicateItem;
                            } else {
                                switch ( item.getBooleanOperator() ) {
                                    case AND:
                                        this.predicate = cb.and(this.predicate, predicateItem);
                                        break;
                                    case OR:
                                        this.predicate = cb.or(this.predicate, predicateItem);
                                        break;
                                }
                            }
                        }
                    }
                }

                private void build() {
                    com.github.alittlehuang.data.query.specification.Expression<T> expressions = item.getExpression();

                    Expression expression = toExpression(expressions, cb, root);

                    Object value = item.getParameter();

                    if ( value instanceof com.github.alittlehuang.data.query.specification.Expression ) {
                        //noinspection unchecked
                        com.github.alittlehuang.data.query.specification.Expression<T> attr = (com.github.alittlehuang.data.query.specification.Expression<T>) value;
                        toPredicateItem(expression, toExpression(attr, cb, root));
                    } else {
                        toPredicateItem(expression, value);
                    }
                }

                @SuppressWarnings( "unchecked" )
                private void toPredicateItem(Expression path, Object value) {

                    if ( path.getJavaType() == Number.class ) {
                        path = path.as(value.getClass());
                    }
                    switch ( item.getConditionalOperator() ) {
                        case EQUAL:
                            predicate = cb.equal(path, value);
                            break;
                        case GREATER_THAN:
                            predicate = cb.greaterThan(path, (Comparable) value);
                            break;
                        case LESS_THAN:
                            predicate = cb.lessThan(path, (Comparable) value);
                            break;
                        case GREATER_THAN_OR_EQUAL_TO:
                            predicate = cb.greaterThanOrEqualTo(path, (Comparable) value);
                            break;
                        case LESS_THAN_OR_EQUAL_TO:
                            predicate = cb.lessThanOrEqualTo(path, (Comparable) value);
                            break;
                        case BETWEEN: {
                            Iterator<?> values = ( (Iterable<?>) value ).iterator();
                            Object x = values.next();
                            Object y = values.next();
                            if ( x instanceof Expression && y instanceof Expression ) {
                                predicate = cb.between(path, (Expression) x, (Expression) y);
                            } else {
                                predicate = cb.between(path, (Comparable) x, (Comparable) y);
                            }
                            break;
                        }
                        case IN: {
                            Iterable<?> values = (Iterable<Comparable>) value;
                            Iterator<?> iterator = values.iterator();

                            if ( !iterator.hasNext() ) {
                                predicate = cb.equal(path, path).not();//will get empty result
                                break;
                            }
                            CriteriaBuilder.In<Object> in = cb.in(path);
                            for ( Object valueIn : values ) {
                                in.value(valueIn);
                            }
                            predicate = in;
                            break;
                        }
                        case LIKE:
                            predicate = cb.like(path, (String) value);
                            break;
                        case IS_NULL:
                            predicate = cb.isNull(path);
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                }

                @SuppressWarnings( "unchecked" )
                private void toPredicateItem(Expression expression, Expression other) {
                    switch ( item.getConditionalOperator() ) {
                        case EQUAL:
                            predicate = cb.equal(expression, other);
                            break;
                        case GREATER_THAN:
                            predicate = cb.greaterThan(expression, other);
                            break;
                        case LESS_THAN:
                            predicate = cb.lessThan(expression, other);
                            break;
                        case GREATER_THAN_OR_EQUAL_TO:
                            predicate = cb.greaterThanOrEqualTo(expression, other);
                            break;
                        case LESS_THAN_OR_EQUAL_TO:
                            predicate = cb.lessThanOrEqualTo(expression, other);
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                }

            }

        }
    }

}
