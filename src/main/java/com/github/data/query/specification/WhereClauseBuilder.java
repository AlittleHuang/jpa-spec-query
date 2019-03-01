package com.github.data.query.specification;

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

    THIS andIsNull(Getter<T, ?> name);

    default THIS isNull(Getter<T, ?> name) {
        return andIsNull(name);
    }

    THIS andIsNotNull(Getter<T, ?> name);

    default THIS isNotNull(Getter<T, ?> name) {
        return andIsNotNull(name);
    }

    THIS orIsNotNull(Getter<T, ?> name);

    THIS orIsNull(Getter<T, ?> name);

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

    @SuppressWarnings("unchecked")
    <X> THIS andIn(String name, X... value);

    default THIS in(String name, Object... value) {
        return andIn(name, value);
    }

    <X> THIS andNotIn(String name, Collection<X> value);

    default <X> THIS notIn(String name, Collection<X> value) {
        return andNotIn(name, value);
    }

    @SuppressWarnings("unchecked")
    <X> THIS andNotIn(String name, X... value);

    default THIS notIn(String name, Object... value) {
        return andNotIn(name, value);
    }

    <X> THIS orIn(String name, Collection<X> value);

    @SuppressWarnings("unchecked")
    <X> THIS orIn(String name, X... value);

    <X> THIS orNotIn(String name, Collection<X> value);

    @SuppressWarnings("unchecked")
    <X> THIS orNotIn(String name, X... value);

    <U, G extends Getter<T, ? super U>> THIS andEqual(G path, G other);

    default <U, G extends Getter<T, ? super U>> THIS equal(G path, G other) {
        return andEqual(path, other);
    }

    <U, G extends Getter<T, ? super U>> THIS orEqual(G path, G other);

    <U, G extends Getter<T, ? super U>> THIS andNotEqual(G path, G other);

    default <U, G extends Getter<T, ? super U>> THIS notEqual(G path, G other) {
        return andNotEqual(path, other);
    }

    <U, G extends Getter<T, ? super U>> THIS orNotEqual(G path, G other);

    <U, F extends Getter<T, ? super U>> THIS andEq(F getter, U value);

    default <U, F extends Getter<T, ? super U>> THIS eq(F getter, U value) {
        return andEq(getter, value);
    }

    <U, F extends Getter<T, ? super U>> THIS orEq(F getter, U value);

    <U, F extends Getter<T, ? super U>> THIS andNotEq(F getter, U value);
    default <U, F extends Getter<T, ? super U>> THIS notEq(F getter, U value){
        return andNotEq(getter, value);
    }

    <U, F extends Getter<T, ? super U>> THIS orNotEq(F getter, U value);

    <Y extends Comparable<? super Y>> THIS andGe(String name, Y value);
    default <Y extends Comparable<? super Y>> THIS ge(String name, Y value){
        return andGe(name, value);
    }

    <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS andGe(F getter, U value);
    default <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS ge(F getter, U value){
        return andGe(getter, value);
    }

    <Y extends Comparable<? super Y>> THIS orGe(String name, Y value);

    <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS orGe(F getter, U value);

    <Y extends Comparable<? super Y>> THIS andLe(String name, Y value);
    default <Y extends Comparable<? super Y>> THIS le(String name, Y value){
        return andLe(name, value);
    }

    <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS andLe(F getter, U value);

    default <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS le(F getter, U value) {
        return andLe(getter, value);
    }

    <Y extends Comparable<? super Y>> THIS orLe(String name, Y value);

    <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS orLe(F getter, U value);

    <Y extends Comparable<? super Y>> THIS andGt(String name, Y value);

    default <Y extends Comparable<? super Y>> THIS gt(String name, Y value) {
        return andGt(name, value);
    }

    <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS andGt(F getter, U value);

    default <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS gt(F getter, U value) {
        return andGt(getter, value);
    }

    <Y extends Comparable<? super Y>> THIS orGt(String name, Y value);

    <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS orGt(F getter, U value);

    <Y extends Comparable<? super Y>> THIS andLt(String name, Y value);

    default <Y extends Comparable<? super Y>> THIS lt(String name, Y value) {
        return andLt(name, value);
    }

    <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS andLt(F getter, U value);

    default <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS lt(F getter, U value) {
        return andLt(getter, value);
    }

    <Y extends Comparable<? super Y>> THIS orLt(String name, Y value);

    <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS orLt(F getter, U value);

    <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS andBetween(F getter, U value, U otherValue);

    default <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS between(F getter, U value, U otherValue) {
        return andBetween(getter, value, otherValue);
    }

    <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS orBetween(F getter, U value, U otherValue);

    <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS andNotBetween(F getter, U value, U otherValue);

    default <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS notBetween(F getter, U value,
                                                                                              U otherValue) {
        return andNotBetween(getter, value, otherValue);
    }

    <U extends Comparable<? super U>, F extends Getter<T, ? super U>> THIS orNotBetween(F getter, U value, U otherValue);

    THIS andIsNull(String name, Boolean isNull);

    default THIS isNull(String name, Boolean isNull) {
        return andIsNull(name,isNull);
    }

    THIS orIsNull(String name, Boolean isNull);

    THIS andLike(Getter<T, String> getters, String value);

    default THIS like(Getter<T, String> getters, String value) {
        return andLt(getters, value);
    }

    THIS andNotLike(Getter<T, String> getters, String value);

    default THIS notLike(Getter<T, String> getters, String value) {
        return andNotLike(getters, value);
    }

    THIS orLike(Getter<T, String> getters, String value);

    THIS orNotLike(Getter<T, String> getters, String value);

    <U, F extends Getter<T, ? super U>> THIS andIn(F getters, Collection<U> value);

    default <U, F extends Getter<T, ? super U>> THIS in(F getters, Collection<U> value) {
        return andIn(getters, value);
    }

    @SuppressWarnings("unchecked")
    <U, F extends Getter<T, ? super U>> THIS andIn(F getters, U... value);

    @SuppressWarnings("unchecked")
    default <U, F extends Getter<T, ? super U>> THIS in(F getters, U... value) {
        return andIn(getters, value);
    }

    <U, F extends Getter<T, ? super U>> THIS andNotIn(F getters, Collection<U> value);

    default <U, F extends Getter<T, ? super U>> THIS notIn(F getters, Collection<U> value) {
        return andNotIn(getters, value);
    }

    @SuppressWarnings("unchecked")
    <U, F extends Getter<T, ? super U>> THIS andNotIn(F getters, U... value);

    @SuppressWarnings("unchecked")
    default <U, F extends Getter<T, ? super U>> THIS notIn(F getters, U... value) {
        return andNotIn(getters, value);
    }

    <U, F extends Getter<T, ? super U>> THIS orIn(F getters, Collection<U> value);

    @SuppressWarnings("unchecked")
    <U, F extends Getter<T, ? super U>> THIS orIn(F getters, U... value);

    <U, F extends Getter<T, ? super U>> THIS orNotIn(F getters, Collection<U> value);

    @SuppressWarnings("unchecked")
    <U, F extends Getter<T, ? super U>> THIS orNotIn(F getters, U... value);

    WhereClause<T> getWhereClause();
}
