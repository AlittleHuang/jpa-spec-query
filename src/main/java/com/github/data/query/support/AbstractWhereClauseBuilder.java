package com.github.data.query.support;

import com.github.data.query.specification.AttrExpression;
import com.github.data.query.specification.ConditionalOperator;
import com.github.data.query.specification.WhereClause;
import com.github.data.query.specification.WhereClauseBuilder;
import lombok.experimental.Delegate;

import javax.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.Collection;

import static com.github.data.query.specification.ConditionalOperator.*;
import static javax.persistence.criteria.Predicate.BooleanOperator.AND;
import static javax.persistence.criteria.Predicate.BooleanOperator.OR;

public abstract class AbstractWhereClauseBuilder<T, THIS extends WhereClauseBuilder<T, THIS>>
        extends SimpleWhereClause<T>
        implements WhereClauseBuilder<T, THIS> {

    private static final boolean NOT = true;
    private final SimpleWhereClause<T> root;

    public AbstractWhereClauseBuilder(AttrExpression<T> expression, SimpleWhereClause<T> root) {
        super(expression);
        this.root = root;
    }

    protected abstract THIS createSubItem(AttrExpression<T> expression);

    protected THIS self() {
        //noinspection unchecked
        return (THIS) this;
    }

    private AbstractWhereClauseBuilder<T, THIS> sub(AttrExpression<T> expression) {
        //noinspection unchecked
        return (AbstractWhereClauseBuilder) createSubItem(expression);
    }

    public AbstractWhereClauseBuilder() {
        super(null);
        this.root = this;
    }

    private THIS add(AttrExpression<T> expression,
                     Object value,
                     Predicate.BooleanOperator booleanOperator,
                     boolean negate,
                     ConditionalOperator conditionalOperator) {
        AbstractWhereClauseBuilder<T, THIS> item = sub(expression);
        item.parameter = value;
        item.booleanOperator = booleanOperator;
        item.conditionalOperator = conditionalOperator;
        item.negate = negate;
        getCompoundItems().add(item);
        return self();
    }

    private THIS add(Expressions<T, ?> expression,
                     Object value,
                     Predicate.BooleanOperator booleanOperator,
                     boolean negate,
                     ConditionalOperator conditionalOperator) {
        return add((AttrExpression<T>) ( expression ), value, booleanOperator, negate, conditionalOperator);
    }

    private THIS add(String expression,
                     Object value,
                     Predicate.BooleanOperator booleanOperator,
                     boolean negate,
                     ConditionalOperator conditionalOperator) {
        return add(new SimpleExpression<>(expression), value, booleanOperator, negate, conditionalOperator);
    }

    private THIS add(Expressions<T, ?> expression,
                     Object value,
                     Predicate.BooleanOperator booleanOperator,
                     ConditionalOperator conditionalOperator) {
        return add(expression, value, booleanOperator, false, conditionalOperator);
    }

    private THIS add(String expression,
                     Object value,
                     Predicate.BooleanOperator booleanOperator,
                     ConditionalOperator conditionalOperator) {
        return add(expression, value, booleanOperator, false, conditionalOperator);
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
        if ( whereClause instanceof SimpleWhereClause ) {
            sub = whereClause;
            ( (SimpleWhereClause) sub ).booleanOperator = booleanOperator;
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
    public THIS andEq(String name, Object val) {
        return add(name, val, AND, EQUAL);
    }

    @Override
    public THIS orEq(String name, Object val) {
        return add(name, val, OR, EQUAL);
    }

    @Override
    public THIS andNotEq(String name, Object val) {
        return add(name, val, AND, NOT, EQUAL);
    }

    @Override
    public THIS orNotEq(String name, Object val) {
        return add(name, val, OR, NOT, EQUAL);
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
    public THIS andIsNull(Expressions<T, ?> name) {
        return add(name, null, AND, IS_NULL);
    }

    @Override
    public THIS andIsNotNull(Expressions<T, ?> name) {
        return add(name, null, AND, NOT, IS_NULL);
    }

    @Override
    public THIS orIsNotNull(Expressions<T, ?> name) {
        return add(name, null, OR, NOT, IS_NULL);
    }

    @Override
    public THIS orIsNull(Expressions<T, ?> name) {
        return add(name, null, OR, IS_NULL);
    }

    @Override
    public THIS andLike(String name, String val) {
        return add(name, val, AND, LIKE);
    }

    @Override
    public THIS andNotLike(String name, String val) {
        return add(name, val, AND, NOT, LIKE);
    }

    @Override
    public THIS orLike(String name, String val) {
        return add(name, val, OR, LIKE);
    }

    @Override
    public THIS orNotLike(String name, String val) {
        return add(name, val, OR, NOT, LIKE);
    }

    @Override
    public <X> THIS andIn(String name, Collection<X> val) {
        return add(name, val, AND, IN);
    }

    @SafeVarargs
    @Override
    public final <X> THIS andIn(String name, X... val) {
        return andIn(name, Arrays.asList(val));
    }

    @Override
    public <X> THIS andNotIn(String name, Collection<X> values) {
        return add(name, values, AND, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <X> THIS andNotIn(String name, X... values) {
        return andNotIn(name, Arrays.asList(values));
    }

    @Override
    public <X> THIS orIn(String name, Collection<X> val) {
        return add(name, val, OR, IN);
    }

    @SafeVarargs
    @Override
    public final <X> THIS orIn(String name, X... values) {
        return orIn(name, Arrays.asList(values));
    }

    @Override
    public <X> THIS orNotIn(String name, Collection<X> values) {
        return add(name, values, OR, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <X> THIS orNotIn(String name, X... values) {
        return orNotIn(name, Arrays.asList(values));
    }

    @Override
    public <X, E extends Expressions<T, ?>> THIS andEqual(E expression, E other) {
        return add(expression, other, AND, EQUAL);
    }

    @Override
    public <X, E extends Expressions<T, ?>> THIS orEqual(E expression, E other) {
        return add(expression, other, OR, EQUAL);
    }

    @Override
    public <X, E extends Expressions<T, ?>> THIS andNotEqual(E expression, E other) {
        return add(expression, other, AND, NOT, EQUAL);
    }

    @Override
    public <X, E extends Expressions<T, ?>> THIS orNotEqual(E expression, E other) {
        return add(expression, other, OR, NOT, EQUAL);
    }

    @Override
    public <X, E extends Expressions<T, ? super X>> THIS andEq(E getter, X value) {
        return add(getter, value, AND, EQUAL);
    }

    @Override
    public <X, E extends Expressions<T, ? super X>> THIS orEq(E getter, X value) {
        return add(getter, value, OR, EQUAL);
    }

    @Override
    public <X, E extends Expressions<T, ? super X>> THIS andNotEq(E getter, X value) {
        return add(getter, value, AND, NOT, EQUAL);
    }

    @Override
    public <X, E extends Expressions<T, ? super X>> THIS orNotEq(E getter, X value) {
        return add(getter, value, OR, NOT, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS andGe(String name, Y value) {
        return add(name, value, AND, GREATER_THAN_OR_EQUAL_TO);
    }

    @Override
    public <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS andGe(E getter, X value) {
        return add(getter, value, AND, GREATER_THAN_OR_EQUAL_TO);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS orGe(String name, Y value) {
        return add(name, value, OR, GREATER_THAN_OR_EQUAL_TO);
    }

    @Override
    public <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS orGe(E getter, X value) {
        return add(getter, value, OR, GREATER_THAN_OR_EQUAL_TO);
    }

    @Override
    public <X extends Comparable<? super X>> THIS andGreaterThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return add(expression, other, AND, GREATER_THAN_OR_EQUAL_TO);
    }

    @Override
    public <X extends Comparable<? super X>> THIS orGreaterThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return add(expression, other, OR, GREATER_THAN_OR_EQUAL_TO);
    }

    @Override
    public <X extends Comparable<? super X>> THIS andNotGreaterThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return add(expression, other, AND, NOT, GREATER_THAN_OR_EQUAL_TO);
    }

    @Override
    public <X extends Comparable<? super X>> THIS orNotGreaterThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return add(expression, other, OR, NOT, GREATER_THAN_OR_EQUAL_TO);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS andLe(String name, Y value) {
        return add(name, value, AND, LESS_THAN_OR_EQUAL_TO);
    }

    @Override
    public <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS andLe(E getter, X value) {
        return add(getter, value, AND, LESS_THAN_OR_EQUAL_TO);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS orLe(String name, Y value) {
        return add(name, value, OR, LESS_THAN_OR_EQUAL_TO);
    }

    @Override
    public <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS orLe(E getter, X value) {
        return add(getter, value, OR, LESS_THAN_OR_EQUAL_TO);
    }

    @Override
    public <X extends Comparable<? super X>> THIS andLessThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return add(expression, other, AND, LESS_THAN_OR_EQUAL_TO);
    }

    @Override
    public <X extends Comparable<? super X>> THIS orLessThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return add(expression, other, OR, LESS_THAN_OR_EQUAL_TO);
    }

    @Override
    public <X extends Comparable<? super X>> THIS andNotLessThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return add(expression, other, AND, NOT, LESS_THAN_OR_EQUAL_TO);
    }

    @Override
    public <X extends Comparable<? super X>> THIS orNotLessThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return add(expression, other, OR, NOT, LESS_THAN_OR_EQUAL_TO);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS andGt(String name, Y value) {
        return add(name, value, AND, GREATER_THAN);
    }

    @Override
    public <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS andGt(E getter, X value) {
        return add(getter, value, AND, GREATER_THAN);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS orGt(String name, Y value) {
        return add(name, value, OR, GREATER_THAN);
    }

    @Override
    public <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS orGt(E getter, X value) {
        return add(getter, value, OR, GREATER_THAN);
    }

    @Override
    public <X extends Comparable<? super X>> THIS andGreaterThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return add(expression, other, AND, GREATER_THAN);
    }

    @Override
    public <X extends Comparable<? super X>> THIS orGreaterThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return add(expression, other, OR, GREATER_THAN);
    }

    @Override
    public <X extends Comparable<? super X>> THIS andNotGreaterThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return add(expression, other, AND, NOT, GREATER_THAN);
    }

    @Override
    public <X extends Comparable<? super X>> THIS orNotGreaterThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return add(expression, other, OR, NOT, GREATER_THAN);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS andLt(String name, Y value) {
        return add(name, value, AND, LESS_THAN);
    }

    @Override
    public <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS andLt(E getter, X value) {
        return add(getter, value, AND, LESS_THAN);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS orLt(String name, Y value) {
        return add(name, value, OR, LESS_THAN);
    }

    @Override
    public <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS orLt(E getter, X value) {
        return add(getter, value, OR, LESS_THAN);
    }

    @Override
    public <X extends Comparable<? super X>> THIS andLessThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return add(expression, other, AND, LESS_THAN);
    }

    @Override
    public <X extends Comparable<? super X>> THIS orLessThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return add(expression, other, OR, LESS_THAN);
    }

    @Override
    public <X extends Comparable<? super X>> THIS andNotLessThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return add(expression, other, AND, NOT, LESS_THAN);
    }

    @Override
    public <X extends Comparable<? super X>> THIS orNotLessThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return add(expression, other, OR, NOT, LESS_THAN);
    }

    @Override
    public <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS andBetween(E getter, X value, X otherValue) {
        return add(getter, Arrays.asList(value, otherValue), AND, BETWEEN);
    }

    @Override
    public <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS orBetween(E getter, X value, X otherValue) {
        return add(getter, Arrays.asList(value, otherValue), OR, BETWEEN);
    }

    @Override
    public <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS andNotBetween(E getter, X value, X otherValue) {
        return add(getter, Arrays.asList(value, otherValue), AND, NOT, BETWEEN);
    }

    @Override
    public <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS orNotBetween(E getter, X value, X otherValue) {
        return add(getter, Arrays.asList(value, otherValue), OR, NOT, BETWEEN);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS andBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), AND, BETWEEN);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS orBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), OR, BETWEEN);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS andNotBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), AND, NOT, BETWEEN);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS orNotBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), OR, NOT, BETWEEN);
    }

    @Override
    public THIS andLike(Expressions<T, String> expression, String value) {
        return add(expression, value, AND, LIKE);
    }

    @Override
    public THIS andNotLike(Expressions<T, String> expression, String value) {
        return add(expression, value, AND, NOT, LIKE);
    }

    @Override
    public THIS orLike(Expressions<T, String> expression, String value) {
        return add(expression, value, OR, LIKE);
    }

    @Override
    public THIS orNotLike(Expressions<T, String> expression, String value) {
        return add(expression, value, OR, NOT, LIKE);
    }

    @Override
    public <X, E extends Expressions<T, ? super X>> THIS andIn(E expression, Collection<X> value) {
        return add(expression, value, AND, IN);
    }

    @SafeVarargs
    @Override
    public final <X, E extends Expressions<T, ? super X>> THIS andIn(E expression, X... value) {
        return andIn(expression, Arrays.asList(value));
    }

    @Override
    public <X, E extends Expressions<T, ? super X>> THIS andNotIn(E expression, Collection<X> value) {
        return add(expression, value, AND, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <X, E extends Expressions<T, ? super X>> THIS andNotIn(E expression, X... value) {
        return andNotIn(expression, Arrays.asList(value));
    }

    @Override
    public <X, E extends Expressions<T, ? super X>> THIS orIn(E expression, Collection<X> value) {
        return add(expression, value, OR, IN);
    }

    @SafeVarargs
    @Override
    public final <X, E extends Expressions<T, ? super X>> THIS orIn(E expression, X... value) {
        return orIn(expression, Arrays.asList(value));
    }

    @Override
    public <X, E extends Expressions<T, ? super X>> THIS orNotIn(E expression, Collection<X> value) {
        return add(expression, value, OR, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <X, E extends Expressions<T, ? super X>> THIS orNotIn(E expression, X... value) {
        return orNotIn(expression, Arrays.asList(value));
    }

    @Override
    public SimpleWhereClause<T> getWhereClause() {
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
