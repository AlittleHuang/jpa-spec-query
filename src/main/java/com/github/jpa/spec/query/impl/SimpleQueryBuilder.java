package com.github.jpa.spec.query.impl;

import com.github.jpa.spec.Getters;
import com.github.jpa.spec.query.api.ConditionalOperator;
import com.github.jpa.spec.query.api.Query;
import com.github.jpa.spec.query.api.Stored;
import lombok.experimental.Delegate;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.Predicate.BooleanOperator;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.github.jpa.spec.query.api.ConditionalOperator.*;
import static javax.persistence.criteria.Predicate.BooleanOperator.AND;
import static javax.persistence.criteria.Predicate.BooleanOperator.OR;

public class SimpleQueryBuilder<T> implements Query<T> {

    private static final boolean NOT = true;

    private final WhereClauseItem<T> where;
    private final SimpleCriteria<T> criteria;
    private final AbstractStored<T> stored;

    @Delegate
    private Stored getStored() {
        return stored;
    }

    private SimpleQueryBuilder<T> add(SimpleQueryBuilder<T> sub) {
        where.compoundItems.add(sub.where);
        return this;
    }

    public SimpleQueryBuilder(AbstractStored<T> stored) {
        where = new WhereClauseItem<>(null);
        criteria = new SimpleCriteria<>(where);
        stored.criteria = criteria;
        this.stored = stored;
    }

    private SimpleQueryBuilder<T> add(String paths,
                                      Object value,
                                      BooleanOperator operator,
                                      ConditionalOperator conditionalOperator) {
        return add(paths, value, operator, false, conditionalOperator);
    }

    private SimpleQueryBuilder<T> add(String paths,
                                      Object value,
                                      BooleanOperator operator,
                                      boolean isNot,
                                      ConditionalOperator conditionalOperator) {
        SimpleQueryBuilder<T> sub = new SimpleQueryBuilder<>(new SimpleFieldPath<>(paths), value, criteria, stored);
        sub.where.booleanOperator = operator;
        sub.where.conditionalOperator = conditionalOperator;
        this.where.negate = isNot;
        return add(sub);
    }

    private SimpleQueryBuilder(SimpleFieldPath path, Object value, SimpleCriteria<T> criteria, AbstractStored<T> stored) {
        where = new WhereClauseItem<>(path);
        this.where.value = value;
        this.criteria = criteria;
        this.stored = stored;
    }

    private SimpleQueryBuilder<T> add(Getters<T, ?> paths,
                                      Object value,
                                      BooleanOperator operator,
                                      ConditionalOperator conditionalOperator) {
        return add(paths, value, operator, false, conditionalOperator);
    }

    private SimpleQueryBuilder<T> add(Getters<T, ?> paths,
                                      Object value,
                                      BooleanOperator operator,
                                      boolean negate,
                                      ConditionalOperator conditionalOperator) {
        SimpleQueryBuilder<T> item = new SimpleQueryBuilder<>(new SimpleFieldPath<>(paths), value, criteria, stored);
        item.where.booleanOperator = operator;
        item.where.conditionalOperator = conditionalOperator;
        return add(item);
    }

    public SimpleQueryBuilder<T> and() {
        SimpleQueryBuilder<T> sub = new SimpleQueryBuilder<>(null, null, criteria, stored);
        this.where.compoundItems.add(sub.where);
        return sub;
    }

    public SimpleQueryBuilder<T> or() {
        SimpleQueryBuilder<T> item = new SimpleQueryBuilder<>(null, null, criteria, stored);
        item.where.booleanOperator = OR;
        this.where.compoundItems.add(item.where);
        return item;
    }

    @Override
    public SimpleQueryBuilder<T> andEqualAsPath(String path, String other) {
        return add(path, new SimpleFieldPath<>(other), AND, EQUAL);
    }

    @Override
    public SimpleQueryBuilder<T> orEqualAsPath(String path, String other) {
        return add(path, new SimpleFieldPath<>(other), OR, EQUAL);
    }

    @Override
    public SimpleQueryBuilder<T> andNotEqualAsPath(String path, String other) {
        return add(path, new SimpleFieldPath<>(other), AND, NOT, EQUAL);
    }

    @Override
    public SimpleQueryBuilder<T> orNotEqualAsPath(String path, String other) {
        return add(path, new SimpleFieldPath<>(other), OR, NOT, EQUAL);
    }

    @Override
    public SimpleQueryBuilder<T> andEqual(String name, Object value) {
        return add(name, value, AND, EQUAL);
    }

    @Override
    public SimpleQueryBuilder<T> orEqual(String name, Object value) {
        return add(name, value, OR, EQUAL);
    }

    @Override
    public SimpleQueryBuilder<T> andNotEqual(String name, Object value) {
        return add(name, value, AND, NOT, EQUAL);
    }

    @Override
    public SimpleQueryBuilder<T> orNotEqual(String name, Object value) {
        return add(name, value, OR, NOT, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> SimpleQueryBuilder<T> andBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), AND, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> SimpleQueryBuilder<T> orBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), OR, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> SimpleQueryBuilder<T> andNotBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), AND, NOT, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> SimpleQueryBuilder<T> orNotBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), OR, NOT, EQUAL);
    }

    @Override
    public SimpleQueryBuilder<T> andIsNull(String name) {
        return add(name, null, AND, IS_NULL);
    }

    @Override
    public SimpleQueryBuilder<T> andIsNotNull(String name) {
        return add(name, null, AND, NOT, IS_NULL);
    }

    @Override
    public SimpleQueryBuilder<T> orIsNotNull(String name) {
        return add(name, null, OR, NOT, IS_NULL);
    }

    @Override
    public SimpleQueryBuilder<T> orIsNull(String name) {
        return add(name, null, OR, IS_NULL);
    }

    @Override
    public SimpleQueryBuilder<T> andLike(String name, String value) {
        return add(name, value, AND, LIKE);
    }

    @Override
    public SimpleQueryBuilder<T> andNotLike(String name, String value) {
        return add(name, value, AND, NOT, LIKE);
    }

    @Override
    public SimpleQueryBuilder<T> orLike(String name, String value) {
        return add(name, value, OR, LIKE);
    }

    @Override
    public SimpleQueryBuilder<T> orNotLike(String name, String value) {
        return add(name, value, OR, NOT, LIKE);
    }

    @Override
    public <X> SimpleQueryBuilder<T> andIn(String name, Collection<X> value) {
        return add(name, value, AND, IN);
    }

    @SafeVarargs
    @Override
    public final <X> SimpleQueryBuilder<T> andIn(String name, X... value) {
        return andIn(name, Arrays.asList(value));
    }

    @Override
    public <X> SimpleQueryBuilder<T> andNotIn(String name, Collection<X> value) {
        return add(name, value, AND, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <X> SimpleQueryBuilder<T> andNotIn(String name, X... value) {
        return andNotIn(name, Arrays.asList(value));
    }

    @Override
    public <X> SimpleQueryBuilder<T> orIn(String name, Collection<X> value) {
        return add(name, value, OR, IN);
    }

    @SafeVarargs
    @Override
    public final <X> SimpleQueryBuilder<T> orIn(String name, X... value) {
        return orIn(name, Arrays.asList(value));
    }

    @Override
    public <X> SimpleQueryBuilder<T> orNotIn(String name, Collection<X> value) {
        return add(name, value, AND, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <X> SimpleQueryBuilder<T> orNotIn(String name, X... value) {
        return orNotIn(name, Arrays.asList(value));
    }

    @Override
    public <U, G extends Getters<T, ? super U>> SimpleQueryBuilder<T> andEqualAsPath(G path, G other) {
        return add(path, new SimpleFieldPath<>(other), AND, EQUAL);
    }

    @Override
    public <U, G extends Getters<T, ? super U>> SimpleQueryBuilder<T> orEqualAsPath(G path, G other) {
        return add(path, new SimpleFieldPath<>(other), OR, EQUAL);
    }

    @Override
    public <U, G extends Getters<T, ? super U>> SimpleQueryBuilder<T> andNotEqualAsPath(G path, G other) {
        return add(path, new SimpleFieldPath<>(other), AND, NOT, EQUAL);
    }

    @Override
    public <U, G extends Getters<T, ? super U>> SimpleQueryBuilder<T> orNotEqualAsPath(G path, G other) {
        return add(path, new SimpleFieldPath<>(other), OR, NOT, EQUAL);
    }

    @Override
    public <U, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> andEqual(F getter, U value) {
        return add(getter, value, AND, EQUAL);
    }

    @Override
    public <U, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> orEqual(F getter, U value) {
        return add(getter, value, OR, EQUAL);
    }

    @Override
    public <U, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> andNotEqual(F getter, U value) {
        return add(getter, value, AND, NOT, EQUAL);
    }

    @Override
    public <U, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> orNotEqual(F getter, U value) {
        return add(getter, value, OR, NOT, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> SimpleQueryBuilder<T> andGe(String name, Y value) {
        return add(name, value, AND, GE);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> andGe(F getter, U value) {
        return add(getter, value, AND, GE);
    }

    @Override
    public <Y extends Comparable<? super Y>> SimpleQueryBuilder<T> orGe(String name, Y value) {
        return add(name, value, OR, GE);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> orGe(F getter, U value) {
        return add(getter, value, OR, GE);
    }

    @Override
    public <Y extends Comparable<? super Y>> SimpleQueryBuilder<T> andLe(String name, Y value) {
        return add(name, value, AND, LT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> andLe(F getter, U value) {
        return add(getter, value, AND, LE);
    }

    @Override
    public <Y extends Comparable<? super Y>> SimpleQueryBuilder<T> orLe(String name, Y value) {
        return add(name, value, OR, LT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> orLe(F getter, U value) {
        return add(getter, value, OR, LE);
    }

    @Override
    public <Y extends Comparable<? super Y>> SimpleQueryBuilder<T> andGt(String name, Y value) {
        return add(name, value, AND, GT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> andGt(F getter, U value) {
        return add(getter, value, AND, GT);
    }

    @Override
    public <Y extends Comparable<? super Y>> SimpleQueryBuilder<T> orGt(String name, Y value) {
        return add(name, value, OR, GT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> orGt(F getter, U value) {
        return add(getter, value, OR, GT);
    }

    @Override
    public <Y extends Comparable<? super Y>> SimpleQueryBuilder<T> andLt(String name, Y value) {
        return add(name, value, AND, LT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> andLt(F getter, U value) {
        return add(getter, value, AND, LT);
    }

    @Override
    public <Y extends Comparable<? super Y>> SimpleQueryBuilder<T> orLt(String name, Y value) {
        return add(name, value, OR, LT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> orLt(F getter, U value) {
        return add(getter, value, OR, LT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> andBetween(F getter, U value, U otherValue) {
        return add(getter, Arrays.asList(value, otherValue), AND, BETWEEN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> orBetween(F getter, U value, U otherValue) {
        return add(getter, Arrays.asList(value, otherValue), OR, BETWEEN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> andNotBetween(F getter, U value, U otherValue) {
        return add(getter, Arrays.asList(value, otherValue), AND, NOT, BETWEEN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> orNotBetween(F getter, U value, U otherValue) {
        return add(getter, Arrays.asList(value, otherValue), OR, NOT, BETWEEN);
    }

    @Override
    public SimpleQueryBuilder<T> andIsNull(String name, Boolean isNull) {
        if (isNull)
            return add(name, null, AND, IS_NULL);
        else {
            return add(name, null, AND, NOT, IS_NULL);
        }
    }

    @Override
    public SimpleQueryBuilder<T> orIsNull(String name, Boolean isNull) {
        if (isNull)
            return add(name, null, OR, IS_NULL);
        else {
            return add(name, null, OR, NOT, IS_NULL);
        }
    }

    @Override
    public SimpleQueryBuilder<T> andLike(Getters<T, String> getters, String value) {
        return add(getters, value, AND, LIKE);
    }

    @Override
    public SimpleQueryBuilder<T> andNotLike(Getters<T, String> getters, String value) {
        return add(getters, value, AND, NOT, LIKE);
    }

    @Override
    public SimpleQueryBuilder<T> orLike(Getters<T, String> getters, String value) {
        return add(getters, value, OR, LIKE);
    }

    @Override
    public SimpleQueryBuilder<T> orNotLike(Getters<T, String> getters, String value) {
        return add(getters, value, OR, NOT, LIKE);
    }

    @Override
    public <U, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> andIn(Getters<T, U> getters, Collection<U> value) {
        return add(getters, value, AND, IN);
    }

    @SafeVarargs
    @Override
    public final <U, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> andIn(Getters<T, U> getters, U... value) {
        return andIn(getters, Arrays.asList(value));
    }

    @Override
    public <U, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> andNotIn(Getters<T, U> getters, Collection<U> value) {
        return add(getters, value, AND, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <U, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> andNotIn(Getters<T, U> getters, U... value) {
        return andNotIn(getters, Arrays.asList(value));
    }

    @Override
    public <U, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> orIn(Getters<T, U> getters, Collection<U> value) {
        return add(getters, value, OR, IN);
    }

    @SafeVarargs
    @Override
    public final <U, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> orIn(Getters<T, U> getters, U... value) {
        return orIn(getters, Arrays.asList(value));
    }

    @Override
    public <U, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> orNotIn(Getters<T, U> getters, Collection<U> value) {
        return add(getters, value, OR, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <U, F extends Getters<T, ? super U>> SimpleQueryBuilder<T> orNotIn(Getters<T, U> getters, U... value) {
        return orNotIn(getters, Arrays.asList(value));
    }

    public Query<T> addSelect(String... paths) {
        List<SimpleFieldPath<T>> selections = criteria.getSelections();
        for (String path : paths) {
            selections.add(new SimpleFieldPath<>(path));
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public Query<T> addSelect(Getters<T, ?>... paths) {
        List<SimpleFieldPath<T>> selections = criteria.getSelections();
        for (Getters<T, ?> path : paths) {
            selections.add(new SimpleFieldPath<>(path));
        }
        return this;
    }

    public Query<T> addGroupings(String... paths) {
        List<SimpleFieldPath<T>> groupings = criteria.getGroupings();
        for (String path : paths) {
            groupings.add(new SimpleFieldPath<>(path));
        }
        return this;
    }


    @SuppressWarnings("unchecked")
    public Query<T> addGroupings(Getters<T, ?>... paths) {
        List<SimpleFieldPath<T>> groupings = criteria.getGroupings();
        for (Getters<T, ?> path : paths) {
            groupings.add(new SimpleFieldPath<>(path));
        }
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
    public Query<T> addOrdersAsc(Getters<T, ?>... paths) {
        List<SimpleOrders<T>> orders = criteria.getOrders();
        for (Getters<T, ?> path : paths) {
            orders.add(new SimpleOrders<>(Sort.Direction.ASC, path));
        }
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
    public Query<T> addOrdersDesc(Getters<T, ?>... paths) {
        List<SimpleOrders<T>> orders = criteria.getOrders();
        for (Getters<T, ?> path : paths) {
            orders.add(new SimpleOrders<>(Sort.Direction.DESC, path));
        }
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
    public Query<T> addFetchs(Getters<T, ?>... paths) {
        List<SimpleFieldPath<T>> fetchs = criteria.getFetchs();
        for (Getters<T, ?> path : paths) {
            fetchs.add(new SimpleFieldPath<>(path));
        }
        return this;
    }

}