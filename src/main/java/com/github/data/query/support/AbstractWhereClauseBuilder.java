package com.github.data.query.support;

import com.github.data.query.specification.*;
import lombok.experimental.Delegate;

import javax.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.Collection;

import static com.github.data.query.specification.ConditionalOperator.*;
import static javax.persistence.criteria.Predicate.BooleanOperator.AND;
import static javax.persistence.criteria.Predicate.BooleanOperator.OR;

public abstract class AbstractWhereClauseBuilder<T, THIS extends WhereClauseBuilder<T, THIS>>
        extends WhereClauseItem<T>
        implements WhereClauseBuilder<T, THIS> {

    private static final boolean NOT = true;
    private final WhereClauseItem<T> root;

    public AbstractWhereClauseBuilder(Attribute path, WhereClauseItem<T> root) {
        super(path);
        this.root = root;
    }

    protected abstract THIS createSubItem(Attribute<T> paths);

    protected THIS self(){
        //noinspection unchecked
        return (THIS) this;
    }

    private AbstractWhereClauseBuilder<T, THIS> sub(Attribute<T> paths){
        //noinspection unchecked
        return (AbstractWhereClauseBuilder) createSubItem(paths);
    }

    public AbstractWhereClauseBuilder() {
        super(null);
        this.root = this;
    }

    private THIS add(Attribute<T> paths,
                     Object value,
                     Predicate.BooleanOperator booleanOperator,
                     boolean negate,
                     ConditionalOperator conditionalOperator) {
        AbstractWhereClauseBuilder<T, THIS> item = sub(paths);
        item.value = value;
        item.booleanOperator = booleanOperator;
        item.conditionalOperator = conditionalOperator;
        item.negate = negate;
        getCompoundItems().add(item);
        return self();
    }

    private THIS add(Getter<T, ?> paths,
                     Object value,
                     Predicate.BooleanOperator booleanOperator,
                     boolean negate,
                     ConditionalOperator conditionalOperator) {
        return add((Attribute<T>) (paths), value, booleanOperator, negate, conditionalOperator);
    }

    private THIS add(String paths,
                     Object value,
                     Predicate.BooleanOperator booleanOperator,
                     boolean negate,
                     ConditionalOperator conditionalOperator) {
        SimpleAttribute<T> path = new SimpleAttribute<>(paths);
        return add(path, value, booleanOperator, negate, conditionalOperator);
    }

    private THIS add(Getter<T, ?> paths,
                     Object value,
                     Predicate.BooleanOperator booleanOperator,
                     ConditionalOperator conditionalOperator) {
        return add(paths, value, booleanOperator, false, conditionalOperator);
    }

    private THIS add(String paths,
                     Object value,
                     Predicate.BooleanOperator booleanOperator,
                     ConditionalOperator conditionalOperator) {
        return add(paths, value, booleanOperator, false, conditionalOperator);
    }

    public THIS and() {
        AbstractWhereClauseBuilder<T, THIS> sub = sub(null);
        getCompoundItems().add(sub);
        return sub.self();
    }

    @Override
    public THIS and(WhereClause<T> whereClause) {
        WhereClause<T> sub;
        sub = sub(whereClause, AND);
        getCompoundItems().add(sub);
        return self();
    }

    private WhereClause<T> sub(WhereClause<T> whereClause,
                               Predicate.BooleanOperator booleanOperator) {
        WhereClause<T> sub;
        if (whereClause instanceof WhereClauseItem) {
            sub = whereClause;
            ((WhereClauseItem) sub).setBooleanOperator(booleanOperator);
        } else {
            sub = new AbstractWhereClause(whereClause) {
                @Override
                public Predicate.BooleanOperator getBooleanOperator() {
                    return booleanOperator;
                }
            };
        }
        return sub;
    }

    public THIS or() {
        AbstractWhereClauseBuilder<T, THIS> sub = sub(null);
        sub.booleanOperator = OR;
        getCompoundItems().add(sub);
        return sub.self();
    }

    @Override
    public THIS or(WhereClause<T> whereClause) {
        WhereClause<T> sub;
        sub = sub(whereClause, OR);
        getCompoundItems().add(sub);
        return self();
    }

    @Override
    public THIS andEq(String name, Object value) {
        return add(name, value, AND, EQUAL);
    }

    @Override
    public THIS orEq(String name, Object value) {
        return add(name, value, OR, EQUAL);
    }

    @Override
    public THIS andNotEq(String name, Object value) {
        return add(name, value, AND, NOT, EQUAL);
    }

    @Override
    public THIS orNotEq(String name, Object value) {
        return add(name, value, OR, NOT, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS andBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), AND, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS orBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), OR, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS andNotBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), AND, NOT, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS orNotBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), OR, NOT, EQUAL);
    }

    @Override
    public THIS andIsNull(String name) {
        return add(name, null, AND, IS_NULL);
    }

    @Override
    public THIS andIsNotNull(String name) {
        return add(name, null, AND, NOT, IS_NULL);
    }

    @Override
    public THIS orIsNotNull(String name) {
        return add(name, null, OR, NOT, IS_NULL);
    }

    @Override
    public THIS orIsNull(String name) {
        return add(name, null, OR, IS_NULL);
    }

    @Override
    public THIS andIsNull(Getter<T, ?> name) {
        return add(name, null, AND, IS_NULL);
    }

    @Override
    public THIS andIsNotNull(Getter<T, ?> name) {
        return add(name, null, AND, NOT, IS_NULL);
    }

    @Override
    public THIS orIsNotNull(Getter<T, ?> name) {
        return add(name, null, OR, NOT, IS_NULL);
    }

    @Override
    public THIS orIsNull(Getter<T, ?> name) {
        return add(name, null, OR, IS_NULL);
    }

    @Override
    public THIS andLike(String name, String value) {
        return add(name, value, AND, LIKE);
    }

    @Override
    public THIS andNotLike(String name, String value) {
        return add(name, value, AND, NOT, LIKE);
    }

    @Override
    public THIS orLike(String name, String value) {
        return add(name, value, OR, LIKE);
    }

    @Override
    public THIS orNotLike(String name, String value) {
        return add(name, value, OR, NOT, LIKE);
    }

    @Override
    public <X> THIS andIn(String name, Collection<X> value) {
        return add(name, value, AND, IN);
    }

    @SafeVarargs
    @Override
    public final <X> THIS andIn(String name, X... value) {
        return andIn(name, Arrays.asList(value));
    }

    @Override
    public <X> THIS andNotIn(String name, Collection<X> value) {
        return add(name, value, AND, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <X> THIS andNotIn(String name, X... value) {
        return andNotIn(name, Arrays.asList(value));
    }

    @Override
    public <X> THIS orIn(String name, Collection<X> value) {
        return add(name, value, OR, IN);
    }

    @SafeVarargs
    @Override
    public final <X> THIS orIn(String name, X... value) {
        return orIn(name, Arrays.asList(value));
    }

    @Override
    public <X> THIS orNotIn(String name, Collection<X> value) {
        return add(name, value, AND, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <X> THIS orNotIn(String name, X... value) {
        return orNotIn(name, Arrays.asList(value));
    }

    @Override
    public <U, G extends Getter<T, ? super U>> THIS andEqual(G path, G other) {
        return add(path, other, AND, EQUAL);
    }

    @Override
    public <U, G extends Getter<T, ? super U>> THIS orEqual(G path, G other) {
        return add(path, other, OR, EQUAL);
    }

    @Override
    public <U, G extends Getter<T, ? super U>> THIS andNotEqual(G path, G other) {
        return add(path, other, AND, NOT, EQUAL);
    }

    @Override
    public <U, G extends Getter<T, ? super U>> THIS orNotEqual(G path, G other) {
        return add(path, other, OR, NOT, EQUAL);
    }

    @Override
    public <U, F extends Getter<T, ? super U>> THIS andEq(F getter, U value) {
        return add(getter, value, AND, EQUAL);
    }

    @Override
    public <U, F extends Getter<T, ? super U>> THIS orEq(F getter, U value) {
        return add(getter, value, OR, EQUAL);
    }

    @Override
    public <U, F extends Getter<T, ? super U>> THIS andNotEq(F getter, U value) {
        return add(getter, value, AND, NOT, EQUAL);
    }

    @Override
    public <U, F extends Getter<T, ? super U>> THIS orNotEq(F getter, U value) {
        return add(getter, value, OR, NOT, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS andGe(String name, Y value) {
        return add(name, value, AND, GREATER_THAN_OR_EQUAL_TO);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS andGe(F getter, U value) {
        return add(getter, value, AND, GREATER_THAN_OR_EQUAL_TO);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS orGe(String name, Y value) {
        return add(name, value, OR, GREATER_THAN_OR_EQUAL_TO);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS orGe(F getter, U value) {
        return add(getter, value, OR, GREATER_THAN_OR_EQUAL_TO);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS andLe(String name, Y value) {
        return add(name, value, AND, LESS_THAN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS andLe(F getter, U value) {
        return add(getter, value, AND, LESS_THAN_OR_EQUAL_TO);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS orLe(String name, Y value) {
        return add(name, value, OR, LESS_THAN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS orLe(F getter, U value) {
        return add(getter, value, OR, LESS_THAN_OR_EQUAL_TO);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS andGt(String name, Y value) {
        return add(name, value, AND, GREATER_THAN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS andGt(F getter, U value) {
        return add(getter, value, AND, GREATER_THAN);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS orGt(String name, Y value) {
        return add(name, value, OR, GREATER_THAN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS orGt(F getter, U value) {
        return add(getter, value, OR, GREATER_THAN);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS andLt(String name, Y value) {
        return add(name, value, AND, LESS_THAN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS andLt(F getter, U value) {
        return add(getter, value, AND, LESS_THAN);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS orLt(String name, Y value) {
        return add(name, value, OR, LESS_THAN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS orLt(F getter, U value) {
        return add(getter, value, OR, LESS_THAN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS andBetween(F getter, U value, U otherValue) {
        return add(getter, Arrays.asList(value, otherValue), AND, BETWEEN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS orBetween(F getter, U value, U otherValue) {
        return add(getter, Arrays.asList(value, otherValue), OR, BETWEEN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS andNotBetween(F getter, U value, U otherValue) {
        return add(getter, Arrays.asList(value, otherValue), AND, NOT, BETWEEN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS orNotBetween(F getter, U value, U otherValue) {
        return add(getter, Arrays.asList(value, otherValue), OR, NOT, BETWEEN);
    }

    @Override
    public THIS andIsNull(String name, Boolean isNull) {
        if (isNull)
            return add(name, null, AND, IS_NULL);
        else {
            return add(name, null, AND, NOT, IS_NULL);
        }
    }

    @Override
    public THIS orIsNull(String name, Boolean isNull) {
        if (isNull)
            return add(name, null, OR, IS_NULL);
        else {
            return add(name, null, OR, NOT, IS_NULL);
        }
    }

    @Override
    public THIS andLike(Getter<T, String> getters, String value) {
        return add(getters, value, AND, LIKE);
    }

    @Override
    public THIS andNotLike(Getter<T, String> getters, String value) {
        return add(getters, value, AND, NOT, LIKE);
    }

    @Override
    public THIS orLike(Getter<T, String> getters, String value) {
        return add(getters, value, OR, LIKE);
    }

    @Override
    public THIS orNotLike(Getter<T, String> getters, String value) {
        return add(getters, value, OR, NOT, LIKE);
    }

    @Override
    public <U, F extends Getter<T, ? super U>> THIS andIn(F getters, Collection<U> value) {
        return add(getters, value, AND, IN);
    }

    @SafeVarargs
    @Override
    public final <U, F extends Getter<T, ? super U>> THIS andIn(F getters, U... value) {
        return andIn(getters, Arrays.asList(value));
    }

    @Override
    public <U, F extends Getter<T, ? super U>> THIS andNotIn(F getters, Collection<U> value) {
        return add(getters, value, AND, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <U, F extends Getter<T, ? super U>> THIS andNotIn(F getters, U... value) {
        return andNotIn(getters, Arrays.asList(value));
    }

    @Override
    public <U, F extends Getter<T, ? super U>> THIS orIn(F getters, Collection<U> value) {
        return add(getters, value, OR, IN);
    }

    @SafeVarargs
    @Override
    public final <U, F extends Getter<T, ? super U>> THIS orIn(F getters, U... value) {
        return orIn(getters, Arrays.asList(value));
    }

    @Override
    public <U, F extends Getter<T, ? super U>> THIS orNotIn(F getters, Collection<U> value) {
        return add(getters, value, OR, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <U, F extends Getter<T, ? super U>> THIS orNotIn(F getters, U... value) {
        return orNotIn(getters, Arrays.asList(value));
    }

    @Override
    public WhereClauseItem<T> getWhereClause() {
        return root;
    }

    private class AbstractWhereClause implements WhereClause<T> {
        @Delegate
        private final WhereClause<T> whereClause;

        private AbstractWhereClause(WhereClause<T> whereClause) {
            this.whereClause = whereClause;
        }

    }
}
