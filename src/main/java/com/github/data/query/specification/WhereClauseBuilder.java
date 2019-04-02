package com.github.data.query.specification;

import com.github.data.query.support.Expressions;

import java.util.Collection;

public interface WhereClauseBuilder<T, THIS extends WhereClauseBuilder<T, THIS>> {

    /**
     * 返回一个新的对象,与原条件以OR条件链接
     */
    THIS or();

    THIS or(WhereClause<T> whereClause);

    /**
     * 返回一个新的对象,与原条件以AND条件链接
     */
    THIS and();

    THIS and(WhereClause<T> whereClause);

    THIS andEq(String name, Object value);

    default THIS eq(String name, Object value) {
        return andEq(name, value);
    }

    THIS orEq(String name, Object value);

    THIS andNotEq(String name, Object value);

    default THIS notEq(String name, Object value) {
        return andNotEq(name, value);
    }

    THIS orNotEq(String name, Object value);

    <Y extends Comparable<? super Y>> THIS andBetween(String name, Y value0, Y value1);

    default <Y extends Comparable<? super Y>> THIS between(String name, Y value0, Y value1) {
        return andBetween(name, value0, value1);
    }

    <Y extends Comparable<? super Y>> THIS orBetween(String name, Y value0, Y value1);

    <Y extends Comparable<? super Y>> THIS andNotBetween(String name, Y value0, Y value1);

    default <Y extends Comparable<? super Y>> THIS notBetween(String name, Y value0, Y value1) {
        return andNotBetween(name, value0, value1);
    }

    <Y extends Comparable<? super Y>> THIS orNotBetween(String name, Y value0, Y value1);

    THIS andIsNull(String name);

    default THIS isNull(String name) {
        return andIsNull(name);
    }

    THIS andIsNotNull(String name);

    default THIS isNotNull(String name) {
        return andIsNotNull(name);
    }

    THIS orIsNotNull(String name);

    THIS orIsNull(String name);

    THIS andIsNull(Expressions<T, ?> name);

    default THIS isNull(Expressions<T, ?> name) {
        return andIsNull(name);
    }

    THIS andIsNotNull(Expressions<T, ?> name);

    default THIS isNotNull(Expressions<T, ?> name) {
        return andIsNotNull(name);
    }

    THIS orIsNotNull(Expressions<T, ?> name);

    THIS orIsNull(Expressions<T, ?> name);

    THIS andLike(String name, String value);

    default THIS like(String name, String value) {
        return andLike(name, value);
    }

    THIS andNotLike(String name, String value);

    default THIS notLike(String name, String value) {
        return andNotLike(name, value);
    }

    THIS orLike(String name, String value);

    THIS orNotLike(String name, String value);

    <X> THIS andIn(String name, Collection<X> value);

    default <X> THIS in(String name, Collection<X> value) {
        return andIn(name, value);
    }

    @SuppressWarnings( "unchecked" )
    <X> THIS andIn(String name, X... value);

    default THIS in(String name, Object... value) {
        return andIn(name, value);
    }

    <X> THIS andNotIn(String name, Collection<X> value);

    default <X> THIS notIn(String name, Collection<X> value) {
        return andNotIn(name, value);
    }

    @SuppressWarnings( "unchecked" )
    <X> THIS andNotIn(String name, X... value);

    default THIS notIn(String name, Object... value) {
        return andNotIn(name, value);
    }

    <X> THIS orIn(String name, Collection<X> value);

    @SuppressWarnings( "unchecked" )
    <X> THIS orIn(String name, X... value);

    <X> THIS orNotIn(String name, Collection<X> value);

    @SuppressWarnings( "unchecked" )
    <X> THIS orNotIn(String name, X... value);

    <U, G extends Expressions<T, ?>> THIS andEqual(G expression, G other);

    default <U, G extends Expressions<T, ?>> THIS equal(G expression, G other) {
        return andEqual(expression, other);
    }

    <U, G extends Expressions<T, ?>> THIS orEqual(G expression, G other);

    <U, G extends Expressions<T, ?>> THIS andNotEqual(G expression, G other);

    default <U, G extends Expressions<T, ?>> THIS notEqual(G expression, G other) {
        return andNotEqual(expression, other);
    }

    <U, G extends Expressions<T, ?>> THIS orNotEqual(G expression, G other);

    <U, E extends Expressions<T, ? super U>> THIS andEq(E expression, U value);

    default <U, E extends Expressions<T, ? super U>> THIS eq(E expression, U value) {
        return andEq(expression, value);
    }

    <U, E extends Expressions<T, ? super U>> THIS orEq(E expression, U value);

    <U, E extends Expressions<T, ? super U>> THIS andNotEq(E expression, U value);

    default <U, E extends Expressions<T, ? super U>> THIS notEq(E expression, U value) {
        return andNotEq(expression, value);
    }

    <U, E extends Expressions<T, ? super U>> THIS orNotEq(E expression, U value);

    <Y extends Comparable<? super Y>> THIS andGe(String name, Y value);

    default <Y extends Comparable<? super Y>> THIS ge(String name, Y value) {
        return andGe(name, value);
    }

    <U extends Comparable<? super U>, E extends Expressions<T, ? super U>> THIS andGe(E expression, U value);

    default <U extends Comparable<? super U>, E extends Expressions<T, ? super U>> THIS ge(E expression, U value) {
        return andGe(expression, value);
    }

    <Y extends Comparable<? super Y>> THIS orGe(String name, Y value);

    <U extends Comparable<? super U>, E extends Expressions<T, ? super U>> THIS orGe(E expression, U value);


    <U extends Comparable<? super U>> THIS andGreaterThanOrEqual(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other);

    default <U extends Comparable<? super U>> THIS greaterThanOrEqual(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other) {
        return andGreaterThanOrEqual(expression, other);
    }

    <U extends Comparable<? super U>> THIS orGreaterThanOrEqual(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other);

    <U extends Comparable<? super U>> THIS andNotGreaterThanOrEqual(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other);

    default <U extends Comparable<? super U>> THIS notGreaterThanOrEqual(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other) {
        return andNotGreaterThanOrEqual(expression, other);
    }

    <U extends Comparable<? super U>> THIS orNotGreaterThanOrEqual(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other);


    <Y extends Comparable<? super Y>> THIS andLe(String name, Y value);

    default <Y extends Comparable<? super Y>> THIS le(String name, Y value) {
        return andLe(name, value);
    }

    <U extends Comparable<? super U>, E extends Expressions<T, ? super U>> THIS andLe(E expression, U value);

    default <U extends Comparable<? super U>, E extends Expressions<T, ? super U>> THIS le(E expression, U value) {
        return andLe(expression, value);
    }

    <Y extends Comparable<? super Y>> THIS orLe(String name, Y value);

    <U extends Comparable<? super U>, E extends Expressions<T, ? super U>> THIS orLe(E expression, U value);

    <U extends Comparable<? super U>> THIS andLessThanOrEqual(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other);

    default <U extends Comparable<? super U>> THIS lessThanOrEqual(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other) {
        return andLessThanOrEqual(expression, other);
    }

    <U extends Comparable<? super U>> THIS orLessThanOrEqual(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other);

    <U extends Comparable<? super U>> THIS andNotLessThanOrEqual(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other);

    default <U extends Comparable<? super U>> THIS notLessThanOrEqual(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other) {
        return andNotLessThanOrEqual(expression, other);
    }

    <U extends Comparable<? super U>> THIS orNotLessThanOrEqual(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other);

    <Y extends Comparable<? super Y>> THIS andGt(String name, Y value);

    default <Y extends Comparable<? super Y>> THIS gt(String name, Y value) {
        return andGt(name, value);
    }

    <U extends Comparable<? super U>, E extends Expressions<T, ? super U>> THIS andGt(E expression, U value);

    default <U extends Comparable<? super U>, E extends Expressions<T, ? super U>> THIS gt(E expression, U value) {
        return andGt(expression, value);
    }

    <Y extends Comparable<? super Y>> THIS orGt(String name, Y value);

    <U extends Comparable<? super U>, E extends Expressions<T, ? super U>> THIS orGt(E expression, U value);

    <U extends Comparable<? super U>> THIS andGreaterThan(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other);

    default <U extends Comparable<? super U>> THIS greaterThan(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other) {
        return andGreaterThan(expression, other);
    }

    <U extends Comparable<? super U>> THIS orGreaterThan(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other);

    <U extends Comparable<? super U>> THIS andNotGreaterThan(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other);

    default <U extends Comparable<? super U>> THIS notGreaterThan(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other) {
        return andNotGreaterThan(expression, other);
    }

    <U extends Comparable<? super U>> THIS orNotGreaterThan(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other);

    <Y extends Comparable<? super Y>> THIS andLt(String name, Y value);

    default <Y extends Comparable<? super Y>> THIS lt(String name, Y value) {
        return andLt(name, value);
    }

    <U extends Comparable<? super U>, E extends Expressions<T, ? super U>> THIS andLt(E expression, U value);

    default <U extends Comparable<? super U>, E extends Expressions<T, ? super U>> THIS lt(E expression, U value) {
        return andLt(expression, value);
    }

    <Y extends Comparable<? super Y>> THIS orLt(String name, Y value);

    <U extends Comparable<? super U>, E extends Expressions<T, ? super U>> THIS orLt(E expression, U value);

    <U extends Comparable<? super U>> THIS andLessThan(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other);

    default <U extends Comparable<? super U>> THIS lessThan(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other) {
        return andLessThan(expression, other);
    }

    <U extends Comparable<? super U>> THIS orLessThan(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other);

    <U extends Comparable<? super U>> THIS andNotLessThan(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other);

    default <U extends Comparable<? super U>> THIS notLessThan(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other) {
        return andNotLessThan(expression, other);
    }

    <U extends Comparable<? super U>> THIS orNotLessThan(Expressions<T, ? extends U> expression, Expressions<T, ? extends U> other);

    <U extends Comparable<? super U>, E extends Expressions<T, ? super U>> THIS andBetween(E expression, U value, U otherValue);

    default <U extends Comparable<? super U>, E extends Expressions<T, ? super U>> THIS between(E expression, U value, U otherValue) {
        return andBetween(expression, value, otherValue);
    }

    <U extends Comparable<? super U>, E extends Expressions<T, ? super U>> THIS orBetween(E expression, U value, U otherValue);

    <U extends Comparable<? super U>, E extends Expressions<T, ? super U>> THIS andNotBetween(E expression, U value, U otherValue);

    default <U extends Comparable<? super U>, E extends Expressions<T, ? super U>> THIS notBetween(E expression, U value,
                                                                                                   U otherValue) {
        return andNotBetween(expression, value, otherValue);
    }

    <U extends Comparable<? super U>, E extends Expressions<T, ? super U>> THIS orNotBetween(E expression, U value, U otherValue);

    default THIS andIsNull(String name, boolean isNull) {
        return isNull ? isNull(name) : isNotNull(name);
    }

    default THIS andIsNull(Expressions<T, ?> expressions, boolean isNull) {
        return isNull ? isNull(expressions) : isNotNull(expressions);
    }

    default THIS isNull(String name, boolean isNull) {
        return andIsNull(name, isNull);
    }

    default THIS isNull(Expressions<T, ?> expressions, boolean isNull) {
        return andIsNull(expressions, isNull);
    }

    default THIS orIsNull(String name, boolean isNull) {
        return isNull ? orIsNull(name) : orIsNotNull(name);
    }

    default THIS orIsNull(Expressions<T, ?> expressions, boolean isNull) {
        return isNull ? orIsNull(expressions) : orIsNotNull(expressions);
    }

    THIS andLike(Expressions<T, String> expression, String value);

    default THIS like(Expressions<T, String> expression, String value) {
        return andLike(expression, value);
    }

    THIS andNotLike(Expressions<T, String> expression, String value);

    default THIS notLike(Expressions<T, String> expression, String value) {
        return andNotLike(expression, value);
    }

    THIS orLike(Expressions<T, String> expression, String value);

    THIS orNotLike(Expressions<T, String> expression, String value);

    <U, E extends Expressions<T, ? super U>> THIS andIn(E expression, Collection<U> value);

    default <U, E extends Expressions<T, ? super U>> THIS in(E expression, Collection<U> value) {
        return andIn(expression, value);
    }

    @SuppressWarnings( "unchecked" )
    <U, E extends Expressions<T, ? super U>> THIS andIn(E expression, U... value);

    @SuppressWarnings( "unchecked" )
    default <U, E extends Expressions<T, ? super U>> THIS in(E expression, U... value) {
        return andIn(expression, value);
    }

    <U, E extends Expressions<T, ? super U>> THIS andNotIn(E expression, Collection<U> value);

    default <U, E extends Expressions<T, ? super U>> THIS notIn(E expression, Collection<U> value) {
        return andNotIn(expression, value);
    }

    @SuppressWarnings( "unchecked" )
    <U, E extends Expressions<T, ? super U>> THIS andNotIn(E expression, U... value);

    @SuppressWarnings( "unchecked" )
    default <U, E extends Expressions<T, ? super U>> THIS notIn(E expression, U... value) {
        return andNotIn(expression, value);
    }

    <U, E extends Expressions<T, ? super U>> THIS orIn(E expression, Collection<U> value);

    @SuppressWarnings( "unchecked" )
    <U, E extends Expressions<T, ? super U>> THIS orIn(E expression, U... value);

    <U, E extends Expressions<T, ? super U>> THIS orNotIn(E expression, Collection<U> value);

    @SuppressWarnings( "unchecked" )
    <U, E extends Expressions<T, ? super U>> THIS orNotIn(E expression, U... value);

    WhereClause getWhereClause();
}
