package com.github.data.query.support;

import com.github.data.query.specification.Path;
import com.github.data.query.specification.ConditionalOperator;
import com.github.data.query.specification.Query;
import com.github.data.query.specification.Stored;
import lombok.experimental.Delegate;
import org.springframework.data.domain.Sort;

import javax.persistence.LockModeType;
import javax.persistence.criteria.Predicate.BooleanOperator;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.github.data.query.specification.ConditionalOperator.*;
import static javax.persistence.criteria.Predicate.BooleanOperator.AND;
import static javax.persistence.criteria.Predicate.BooleanOperator.OR;

public class QueryImpl<T> implements Query<T> {

    private static final boolean NOT = true;

    private final WhereClauseItem where;
    private final SimpleCriteria<T> criteria;
    private final AbstractStored<T> stored;

    @Delegate
    private Stored<T> getStored() {
        return stored;
    }

    private QueryImpl<T> add(QueryImpl<T> sub) {
        assert where.compoundItems != null;
        where.compoundItems.add(sub.where);
        return this;
    }

    public QueryImpl(AbstractStored<T> stored) {
        where = new WhereClauseItem(null);
        criteria = new SimpleCriteria<>(where);
        stored.criteria = criteria;
        this.stored = stored;
    }

    private QueryImpl<T> add(String paths,
                             Object value,
                             BooleanOperator operator,
                             ConditionalOperator conditionalOperator) {
        return add(paths, value, operator, false, conditionalOperator);
    }

    private QueryImpl<T> add(String paths,
                             Object value,
                             BooleanOperator operator,
                             boolean isNot,
                             ConditionalOperator conditionalOperator) {
        QueryImpl<T> sub = new QueryImpl<>(new SimpleFieldPath<>(paths), value, criteria, stored);
        sub.where.booleanOperator = operator;
        sub.where.conditionalOperator = conditionalOperator;
        this.where.negate = isNot;
        return add(sub);
    }

    private QueryImpl(SimpleFieldPath path, Object value, SimpleCriteria<T> criteria, AbstractStored<T> stored) {
        where = new WhereClauseItem(path);
        this.where.value = value;
        this.criteria = criteria;
        this.stored = stored;
    }

    private QueryImpl<T> add(Path<T, ?> paths,
                             Object value,
                             BooleanOperator operator,
                             ConditionalOperator conditionalOperator) {
        return add(paths, value, operator, false, conditionalOperator);
    }

    private QueryImpl<T> add(Path<T, ?> paths,
                             Object value,
                             BooleanOperator operator,
                             boolean negate,
                             ConditionalOperator conditionalOperator) {
        QueryImpl<T> item = new QueryImpl<>(new SimpleFieldPath<>(paths), value, criteria, stored);
        item.where.booleanOperator = operator;
        item.where.conditionalOperator = conditionalOperator;
        item.where.negate = negate;
        return add(item);
    }

    public QueryImpl<T> and() {
        QueryImpl<T> sub = new QueryImpl<>(null, null, criteria, stored);
        assert this.where.compoundItems != null;
        this.where.compoundItems.add(sub.where);
        return sub;
    }

    public QueryImpl<T> or() {
        QueryImpl<T> item = new QueryImpl<>(null, null, criteria, stored);
        item.where.booleanOperator = OR;
        assert this.where.compoundItems != null;
        this.where.compoundItems.add(item.where);
        return item;
    }

    @Override
    public QueryImpl<T> andEqualAsPath(String path, String other) {
        return add(path, new SimpleFieldPath<>(other), AND, EQUAL);
    }

    @Override
    public QueryImpl<T> orEqualAsPath(String path, String other) {
        return add(path, new SimpleFieldPath<>(other), OR, EQUAL);
    }

    @Override
    public QueryImpl<T> andNotEqualAsPath(String path, String other) {
        return add(path, new SimpleFieldPath<>(other), AND, NOT, EQUAL);
    }

    @Override
    public QueryImpl<T> orNotEqualAsPath(String path, String other) {
        return add(path, new SimpleFieldPath<>(other), OR, NOT, EQUAL);
    }

    @Override
    public QueryImpl<T> andEqual(String name, Object value) {
        return add(name, value, AND, EQUAL);
    }

    @Override
    public QueryImpl<T> orEqual(String name, Object value) {
        return add(name, value, OR, EQUAL);
    }

    @Override
    public QueryImpl<T> andNotEqual(String name, Object value) {
        return add(name, value, AND, NOT, EQUAL);
    }

    @Override
    public QueryImpl<T> orNotEqual(String name, Object value) {
        return add(name, value, OR, NOT, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> QueryImpl<T> andBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), AND, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> QueryImpl<T> orBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), OR, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> QueryImpl<T> andNotBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), AND, NOT, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> QueryImpl<T> orNotBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), OR, NOT, EQUAL);
    }

    @Override
    public QueryImpl<T> andIsNull(String name) {
        return add(name, null, AND, IS_NULL);
    }

    @Override
    public QueryImpl<T> andIsNotNull(String name) {
        return add(name, null, AND, NOT, IS_NULL);
    }

    @Override
    public QueryImpl<T> orIsNotNull(String name) {
        return add(name, null, OR, NOT, IS_NULL);
    }

    @Override
    public QueryImpl<T> orIsNull(String name) {
        return add(name, null, OR, IS_NULL);
    }

    @Override
    public QueryImpl<T> andIsNull(Path<T,?> name) {
        return add(name, null, AND, IS_NULL);
    }

    @Override
    public QueryImpl<T> andIsNotNull(Path<T,?> name) {
        return add(name, null, AND, NOT, IS_NULL);
    }

    @Override
    public QueryImpl<T> orIsNotNull(Path<T,?> name) {
        return add(name, null, OR, NOT, IS_NULL);
    }

    @Override
    public QueryImpl<T> orIsNull(Path<T,?> name) {
        return add(name, null, OR, IS_NULL);
    }

    @Override
    public QueryImpl<T> andLike(String name, String value) {
        return add(name, value, AND, LIKE);
    }

    @Override
    public QueryImpl<T> andNotLike(String name, String value) {
        return add(name, value, AND, NOT, LIKE);
    }

    @Override
    public QueryImpl<T> orLike(String name, String value) {
        return add(name, value, OR, LIKE);
    }

    @Override
    public QueryImpl<T> orNotLike(String name, String value) {
        return add(name, value, OR, NOT, LIKE);
    }

    @Override
    public <X> QueryImpl<T> andIn(String name, Collection<X> value) {
        return add(name, value, AND, IN);
    }

    @SafeVarargs
    @Override
    public final <X> QueryImpl<T> andIn(String name, X... value) {
        return andIn(name, Arrays.asList(value));
    }

    @Override
    public <X> QueryImpl<T> andNotIn(String name, Collection<X> value) {
        return add(name, value, AND, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <X> QueryImpl<T> andNotIn(String name, X... value) {
        return andNotIn(name, Arrays.asList(value));
    }

    @Override
    public <X> QueryImpl<T> orIn(String name, Collection<X> value) {
        return add(name, value, OR, IN);
    }

    @SafeVarargs
    @Override
    public final <X> QueryImpl<T> orIn(String name, X... value) {
        return orIn(name, Arrays.asList(value));
    }

    @Override
    public <X> QueryImpl<T> orNotIn(String name, Collection<X> value) {
        return add(name, value, AND, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <X> QueryImpl<T> orNotIn(String name, X... value) {
        return orNotIn(name, Arrays.asList(value));
    }

    @Override
    public <U, G extends Path<T, ? super U>> QueryImpl<T> andEqualAsPath(G path, G other) {
        return add(path, new SimpleFieldPath<>(other), AND, EQUAL);
    }

    @Override
    public <U, G extends Path<T, ? super U>> QueryImpl<T> orEqualAsPath(G path, G other) {
        return add(path, new SimpleFieldPath<>(other), OR, EQUAL);
    }

    @Override
    public <U, G extends Path<T, ? super U>> QueryImpl<T> andNotEqualAsPath(G path, G other) {
        return add(path, new SimpleFieldPath<>(other), AND, NOT, EQUAL);
    }

    @Override
    public <U, G extends Path<T, ? super U>> QueryImpl<T> orNotEqualAsPath(G path, G other) {
        return add(path, new SimpleFieldPath<>(other), OR, NOT, EQUAL);
    }

    @Override
    public <U, F extends Path<T, ? super U>> QueryImpl<T> andEqual(F getter, U value) {
        return add(getter, value, AND, EQUAL);
    }

    @Override
    public <U, F extends Path<T, ? super U>> QueryImpl<T> orEqual(F getter, U value) {
        return add(getter, value, OR, EQUAL);
    }

    @Override
    public <U, F extends Path<T, ? super U>> QueryImpl<T> andNotEqual(F getter, U value) {
        return add(getter, value, AND, NOT, EQUAL);
    }

    @Override
    public <U, F extends Path<T, ? super U>> QueryImpl<T> orNotEqual(F getter, U value) {
        return add(getter, value, OR, NOT, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> QueryImpl<T> andGe(String name, Y value) {
        return add(name, value, AND, GE);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> QueryImpl<T> andGe(F getter, U value) {
        return add(getter, value, AND, GE);
    }

    @Override
    public <Y extends Comparable<? super Y>> QueryImpl<T> orGe(String name, Y value) {
        return add(name, value, OR, GE);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> QueryImpl<T> orGe(F getter, U value) {
        return add(getter, value, OR, GE);
    }

    @Override
    public <Y extends Comparable<? super Y>> QueryImpl<T> andLe(String name, Y value) {
        return add(name, value, AND, LT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> QueryImpl<T> andLe(F getter, U value) {
        return add(getter, value, AND, LE);
    }

    @Override
    public <Y extends Comparable<? super Y>> QueryImpl<T> orLe(String name, Y value) {
        return add(name, value, OR, LT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> QueryImpl<T> orLe(F getter, U value) {
        return add(getter, value, OR, LE);
    }

    @Override
    public <Y extends Comparable<? super Y>> QueryImpl<T> andGt(String name, Y value) {
        return add(name, value, AND, GT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> QueryImpl<T> andGt(F getter, U value) {
        return add(getter, value, AND, GT);
    }

    @Override
    public <Y extends Comparable<? super Y>> QueryImpl<T> orGt(String name, Y value) {
        return add(name, value, OR, GT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> QueryImpl<T> orGt(F getter, U value) {
        return add(getter, value, OR, GT);
    }

    @Override
    public <Y extends Comparable<? super Y>> QueryImpl<T> andLt(String name, Y value) {
        return add(name, value, AND, LT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> QueryImpl<T> andLt(F getter, U value) {
        return add(getter, value, AND, LT);
    }

    @Override
    public <Y extends Comparable<? super Y>> QueryImpl<T> orLt(String name, Y value) {
        return add(name, value, OR, LT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> QueryImpl<T> orLt(F getter, U value) {
        return add(getter, value, OR, LT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> QueryImpl<T> andBetween(F getter, U value, U otherValue) {
        return add(getter, Arrays.asList(value, otherValue), AND, BETWEEN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> QueryImpl<T> orBetween(F getter, U value, U otherValue) {
        return add(getter, Arrays.asList(value, otherValue), OR, BETWEEN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> QueryImpl<T> andNotBetween(F getter, U value, U otherValue) {
        return add(getter, Arrays.asList(value, otherValue), AND, NOT, BETWEEN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> QueryImpl<T> orNotBetween(F getter, U value, U otherValue) {
        return add(getter, Arrays.asList(value, otherValue), OR, NOT, BETWEEN);
    }

    @Override
    public QueryImpl<T> andIsNull(String name, Boolean isNull) {
        if (isNull)
            return add(name, null, AND, IS_NULL);
        else {
            return add(name, null, AND, NOT, IS_NULL);
        }
    }

    @Override
    public QueryImpl<T> orIsNull(String name, Boolean isNull) {
        if (isNull)
            return add(name, null, OR, IS_NULL);
        else {
            return add(name, null, OR, NOT, IS_NULL);
        }
    }

    @Override
    public QueryImpl<T> andLike(Path<T, String> getters, String value) {
        return add(getters, value, AND, LIKE);
    }

    @Override
    public QueryImpl<T> andNotLike(Path<T, String> getters, String value) {
        return add(getters, value, AND, NOT, LIKE);
    }

    @Override
    public QueryImpl<T> orLike(Path<T, String> getters, String value) {
        return add(getters, value, OR, LIKE);
    }

    @Override
    public QueryImpl<T> orNotLike(Path<T, String> getters, String value) {
        return add(getters, value, OR, NOT, LIKE);
    }

    @Override
    public <U, F extends Path<T, ? super U>> QueryImpl<T> andIn(Path<T, U> getters, Collection<U> value) {
        return add(getters, value, AND, IN);
    }

    @SafeVarargs
    @Override
    public final <U, F extends Path<T, ? super U>> QueryImpl<T> andIn(Path<T, U> getters, U... value) {
        return andIn(getters, Arrays.asList(value));
    }

    @Override
    public <U, F extends Path<T, ? super U>> QueryImpl<T> andNotIn(Path<T, U> getters, Collection<U> value) {
        return add(getters, value, AND, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <U, F extends Path<T, ? super U>> QueryImpl<T> andNotIn(Path<T, U> getters, U... value) {
        return andNotIn(getters, Arrays.asList(value));
    }

    @Override
    public <U, F extends Path<T, ? super U>> QueryImpl<T> orIn(Path<T, U> getters, Collection<U> value) {
        return add(getters, value, OR, IN);
    }

    @SafeVarargs
    @Override
    public final <U, F extends Path<T, ? super U>> QueryImpl<T> orIn(Path<T, U> getters, U... value) {
        return orIn(getters, Arrays.asList(value));
    }

    @Override
    public <U, F extends Path<T, ? super U>> QueryImpl<T> orNotIn(Path<T, U> getters, Collection<U> value) {
        return add(getters, value, OR, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <U, F extends Path<T, ? super U>> QueryImpl<T> orNotIn(Path<T, U> getters, U... value) {
        return orNotIn(getters, Arrays.asList(value));
    }

    public Query<T> addSelect(String... paths) {
        List<SimpleFieldPath<T>> selections = criteria.getSelections();
        for (String path : paths) {
            selections.add(new SimpleFieldPath<>(path));
        }
        return this;
    }

    public Query<T> addSelect(Path<T, ?> path) {
        criteria.getSelections().add(new SimpleFieldPath<>(path));
        return this;
    }

    public Query<T> addGroupings(String... paths) {
        List<SimpleFieldPath<T>> groupings = criteria.getGroupings();
        for (String path : paths) {
            groupings.add(new SimpleFieldPath<>(path));
        }
        return this;
    }

    public Query<T> addGroupings(Path<T, ?> paths) {
        List<SimpleFieldPath<T>> groupings = criteria.getGroupings();
        groupings.add(new SimpleFieldPath<>(paths));
        return this;
    }

    public Query<T> addOrdersAsc(String... paths) {
        List<SimpleOrders<T>> orders = criteria.getOrders();
        for (String path : paths) {
            orders.add(new SimpleOrders<>(Sort.Direction.ASC, path));
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public Query<T> addOrdersAsc(Path<T, ?> paths) {
        List<SimpleOrders<T>> orders = criteria.getOrders();
        orders.add(new SimpleOrders<>(Sort.Direction.ASC, paths));
        return this;
    }


    public Query<T> addOrdersDesc(String... paths) {
        List<SimpleOrders<T>> orders = criteria.getOrders();
        for (String path : paths) {
            orders.add(new SimpleOrders<>(Sort.Direction.DESC, path));
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public Query<T> addOrdersDesc(Path<T, ?> path) {
        List<SimpleOrders<T>> orders = criteria.getOrders();
        orders.add(new SimpleOrders<>(Sort.Direction.DESC, path));
        return this;
    }

    public Query<T> addFetchs(String... paths) {
        List<SimpleFieldPath<T>> fetchs = criteria.getFetchs();
        for (String path : paths) {
            fetchs.add(new SimpleFieldPath<>(path));
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public Query<T> addFetchs(Path<T, ?> path) {
        List<SimpleFieldPath<T>> fetchs = criteria.getFetchs();
        fetchs.add(new SimpleFieldPath<>(path));
        return this;
    }

    @Override
    public Query<T> setLockModeType(LockModeType lockModeType) {
        criteria.setLockModeType(lockModeType);
        return this;
    }

}