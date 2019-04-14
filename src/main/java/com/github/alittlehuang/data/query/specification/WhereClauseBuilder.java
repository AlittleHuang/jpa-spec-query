package com.github.alittlehuang.data.query.specification;

import java.util.Collection;

/**
 * @author ALittleHuang
 */
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

    <X extends Comparable<? super X>> THIS andBetween(String name, X value0, X value1);

    default <X extends Comparable<? super X>> THIS between(String name, X value0, X value1) {
        return andBetween(name, value0, value1);
    }

    <X extends Comparable<? super X>> THIS orBetween(String name, X value0, X value1);

    <X extends Comparable<? super X>> THIS andNotBetween(String name, X value0, X value1);

    default <X extends Comparable<? super X>> THIS notBetween(String name, X value0, X value1) {
        return andNotBetween(name, value0, value1);
    }

    <X extends Comparable<? super X>> THIS orNotBetween(String name, X value0, X value1);

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

    <E extends Expressions<T, ?>> THIS andEqual(E expression, E other);

    default <X, E extends Expressions<T, ?>> THIS equal(E expression, E other) {
        return andEqual(expression, other);
    }

    <E extends Expressions<T, ?>> THIS orEqual(E expression, E other);

    <E extends Expressions<T, ?>> THIS andNotEqual(E expression, E other);

    default <X, E extends Expressions<T, ?>> THIS notEqual(E expression, E other) {
        return andNotEqual(expression, other);
    }

    <E extends Expressions<T, ?>> THIS orNotEqual(E expression, E other);

    <X, E extends Expressions<T, ? super X>> THIS andEq(E expression, X value);

    default <X, E extends Expressions<T, ? super X>> THIS eq(E expression, X value) {
        return andEq(expression, value);
    }

    <X, E extends Expressions<T, ? super X>> THIS orEq(E expression, X value);

    <X, E extends Expressions<T, ? super X>> THIS andNotEq(E expression, X value);

    default <X, E extends Expressions<T, ? super X>> THIS notEq(E expression, X value) {
        return andNotEq(expression, value);
    }

    <X, E extends Expressions<T, ? super X>> THIS orNotEq(E expression, X value);

    <X extends Comparable<? super X>> THIS andGe(String name, X value);

    default <X extends Comparable<? super X>> THIS ge(String name, X value) {
        return andGe(name, value);
    }

    <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS andGe(E expression, X value);

    default <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS ge(E expression, X value) {
        return andGe(expression, value);
    }

    <X extends Comparable<? super X>> THIS orGe(String name, X value);

    <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS orGe(E expression, X value);


    <X extends Comparable<? super X>> THIS andGreaterThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other);

    default <X extends Comparable<? super X>> THIS greaterThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return andGreaterThanOrEqual(expression, other);
    }

    <X extends Comparable<? super X>> THIS orGreaterThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other);

    <X extends Comparable<? super X>> THIS andNotGreaterThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other);

    default <X extends Comparable<? super X>> THIS notGreaterThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return andNotGreaterThanOrEqual(expression, other);
    }

    <X extends Comparable<? super X>> THIS orNotGreaterThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other);


    <X extends Comparable<? super X>> THIS andLe(String name, X value);

    default <X extends Comparable<? super X>> THIS le(String name, X value) {
        return andLe(name, value);
    }

    <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS andLe(E expression, X value);

    default <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS le(E expression, X value) {
        return andLe(expression, value);
    }

    <X extends Comparable<? super X>> THIS orLe(String name, X value);

    <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS orLe(E expression, X value);

    <X extends Comparable<? super X>> THIS andLessThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other);

    default <X extends Comparable<? super X>> THIS lessThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return andLessThanOrEqual(expression, other);
    }

    <X extends Comparable<? super X>> THIS orLessThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other);

    <X extends Comparable<? super X>> THIS andNotLessThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other);

    default <X extends Comparable<? super X>> THIS notLessThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return andNotLessThanOrEqual(expression, other);
    }

    <X extends Comparable<? super X>> THIS orNotLessThanOrEqual(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other);

    <X extends Comparable<? super X>> THIS andGt(String name, X value);

    default <X extends Comparable<? super X>> THIS gt(String name, X value) {
        return andGt(name, value);
    }

    <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS andGt(E expression, X value);

    default <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS gt(E expression, X value) {
        return andGt(expression, value);
    }

    <X extends Comparable<? super X>> THIS orGt(String name, X value);

    <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS orGt(E expression, X value);

    <X extends Comparable<? super X>> THIS andGreaterThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other);

    default <X extends Comparable<? super X>> THIS greaterThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return andGreaterThan(expression, other);
    }

    <X extends Comparable<? super X>> THIS orGreaterThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other);

    <X extends Comparable<? super X>> THIS andNotGreaterThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other);

    default <X extends Comparable<? super X>> THIS notGreaterThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return andNotGreaterThan(expression, other);
    }

    <X extends Comparable<? super X>> THIS orNotGreaterThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other);

    <X extends Comparable<? super X>> THIS andLt(String name, X value);

    default <X extends Comparable<? super X>> THIS lt(String name, X value) {
        return andLt(name, value);
    }

    <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS andLt(E expression, X value);

    default <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS lt(E expression, X value) {
        return andLt(expression, value);
    }

    <X extends Comparable<? super X>> THIS orLt(String name, X value);

    <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS orLt(E expression, X value);

    <X extends Comparable<? super X>> THIS andLessThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other);

    default <X extends Comparable<? super X>> THIS lessThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return andLessThan(expression, other);
    }

    <X extends Comparable<? super X>> THIS orLessThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other);

    <X extends Comparable<? super X>> THIS andNotLessThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other);

    default <X extends Comparable<? super X>> THIS notLessThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other) {
        return andNotLessThan(expression, other);
    }

    <X extends Comparable<? super X>> THIS orNotLessThan(Expressions<T, ? extends X> expression, Expressions<T, ? extends X> other);

    <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS andBetween(E expression, X value, X otherValue);

    default <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS between(E expression, X value, X otherValue) {
        return andBetween(expression, value, otherValue);
    }

    <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS orBetween(E expression, X value, X otherValue);

    <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS andNotBetween(E expression, X value, X otherValue);

    default <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS notBetween(E expression, X value,
                                                                                                   X otherValue) {
        return andNotBetween(expression, value, otherValue);
    }

    <X extends Comparable<? super X>, E extends Expressions<T, ? super X>> THIS orNotBetween(E expression, X value, X otherValue);

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

    <X, E extends Expressions<T, ? super X>> THIS andIn(E expression, Collection<X> value);

    default <X, E extends Expressions<T, ? super X>> THIS in(E expression, Collection<X> value) {
        return andIn(expression, value);
    }

    @SuppressWarnings( "unchecked" )
    <X, E extends Expressions<T, ? super X>> THIS andIn(E expression, X... value);

    @SuppressWarnings( "unchecked" )
    default <X, E extends Expressions<T, ? super X>> THIS in(E expression, X... value) {
        return andIn(expression, value);
    }

    <X, E extends Expressions<T, ? super X>> THIS andNotIn(E expression, Collection<X> value);

    default <X, E extends Expressions<T, ? super X>> THIS notIn(E expression, Collection<X> value) {
        return andNotIn(expression, value);
    }

    @SuppressWarnings( "unchecked" )
    <X, E extends Expressions<T, ? super X>> THIS andNotIn(E expression, X... value);

    @SuppressWarnings( "unchecked" )
    default <X, E extends Expressions<T, ? super X>> THIS notIn(E expression, X... value) {
        return andNotIn(expression, value);
    }

    <X, E extends Expressions<T, ? super X>> THIS orIn(E expression, Collection<X> value);

    @SuppressWarnings( "unchecked" )
    <X, E extends Expressions<T, ? super X>> THIS orIn(E expression, X... value);

    <X, E extends Expressions<T, ? super X>> THIS orNotIn(E expression, Collection<X> value);

    @SuppressWarnings( "unchecked" )
    <X, E extends Expressions<T, ? super X>> THIS orNotIn(E expression, X... value);

    WhereClause<T> getWhereClause();
}
