package com.github.jpa.spec.query.api;

import com.github.jpa.spec.Getters;

import java.util.Collection;

public interface WhereClauseBuilder<T, THIS extends WhereClauseBuilder<T, THIS>> {

    /**
     * 返回一个新的Criteria,与原Criteria以OR条件链接
     */
    THIS or();

    /**
     * 返回一个新的Criteria,与原Criteria以AND条件链接
     */
    THIS and();

    THIS andEqualAsPath(String path, String other);

    THIS orEqualAsPath(String path, String other);

    THIS andNotEqualAsPath(String path, String other);

    THIS orNotEqualAsPath(String path, String other);

    THIS andEqual(String name, Object value);

    THIS orEqual(String name, Object value);

    THIS andNotEqual(String name, Object value);

    THIS orNotEqual(String name, Object value);

    <Y extends Comparable<? super Y>> THIS andBetween(String name, Y value0, Y value1);

    <Y extends Comparable<? super Y>> THIS orBetween(String name, Y value0, Y value1);

    <Y extends Comparable<? super Y>> THIS andNotBetween(String name, Y value0, Y value1);

    <Y extends Comparable<? super Y>> THIS orNotBetween(String name, Y value0, Y value1);

    THIS andIsNull(String name);

    THIS andIsNotNull(String name);

    THIS orIsNotNull(String name);

    THIS orIsNull(String name);

    THIS andLike(String name, String value);

    THIS andNotLike(String name, String value);

    THIS orLike(String name, String value);

    THIS orNotLike(String name, String value);

    <X> THIS andIn(String name, Collection<X> value);

    <X> THIS andIn(String name, X... value);

    <X> THIS andNotIn(String name, Collection<X> value);

    <X> THIS andNotIn(String name, X... value);

    <X> THIS orIn(String name, Collection<X> value);

    <X> THIS orIn(String name, X... value);

    <X> THIS orNotIn(String name, Collection<X> value);

    <X> THIS orNotIn(String name, X... value);

    <U, G extends Getters<T, ? super U>> THIS andEqualAsPath(G path, G other);

    <U, G extends Getters<T, ? super U>> THIS orEqualAsPath(G path, G other);

    <U, G extends Getters<T, ? super U>> THIS andNotEqualAsPath(G path, G other);

    <U, G extends Getters<T, ? super U>> THIS orNotEqualAsPath(G path, G other);

    <U, F extends Getters<T, ? super U>> THIS andEqual(F getter, U value);

    <U, F extends Getters<T, ? super U>> THIS orEqual(F getter, U value);

    <U, F extends Getters<T, ? super U>> THIS andNotEqual(F getter, U value);

    <U, F extends Getters<T, ? super U>> THIS orNotEqual(F getter, U value);

    <Y extends Comparable<? super Y>> THIS andGe(String name, Y value);

    <U extends Comparable<? super U>, F extends Getters<T, ? super U>> THIS andGe(F getter, U value);

    <Y extends Comparable<? super Y>> THIS orGe(String name, Y value);

    <U extends Comparable<? super U>, F extends Getters<T, ? super U>> THIS orGe(F getter, U value);

    <Y extends Comparable<? super Y>> THIS andLe(String name, Y value);

    <U extends Comparable<? super U>, F extends Getters<T, ? super U>> THIS andLe(F getter, U value);

    <Y extends Comparable<? super Y>> THIS orLe(String name, Y value);

    <U extends Comparable<? super U>, F extends Getters<T, ? super U>> THIS orLe(F getter, U value);

    <Y extends Comparable<? super Y>> THIS andGt(String name, Y value);

    <U extends Comparable<? super U>, F extends Getters<T, ? super U>> THIS andGt(F getter, U value);

    <Y extends Comparable<? super Y>> THIS orGt(String name, Y value);

    <U extends Comparable<? super U>, F extends Getters<T, ? super U>> THIS orGt(F getter, U value);

    <Y extends Comparable<? super Y>> THIS andLt(String name, Y value);

    <U extends Comparable<? super U>, F extends Getters<T, ? super U>> THIS andLt(F getter, U value);

    <Y extends Comparable<? super Y>> THIS orLt(String name, Y value);

    <U extends Comparable<? super U>, F extends Getters<T, ? super U>> THIS orLt(F getter, U value);

    <U extends Comparable<? super U>, F extends Getters<T, ? super U>> THIS andBetween(F getter, U value, U otherValue);

    <U extends Comparable<? super U>, F extends Getters<T, ? super U>> THIS orBetween(F getter, U value, U otherValue);

    <U extends Comparable<? super U>, F extends Getters<T, ? super U>> THIS andNotBetween(F getter, U value, U otherValue);

    <U extends Comparable<? super U>, F extends Getters<T, ? super U>> THIS orNotBetween(F getter, U value, U otherValue);

    THIS andIsNull(String name, Boolean isNull);

    THIS orIsNull(String name, Boolean isNull);

    THIS andLike(Getters<T, String> getters, String value);

    THIS andNotLike(Getters<T, String> getters, String value);

    THIS orLike(Getters<T, String> getters, String value);

    THIS orNotLike(Getters<T, String> getters, String value);

    <U, F extends Getters<T, ? super U>> THIS andIn(Getters<T, U> getters, Collection<U> value);

    <U, F extends Getters<T, ? super U>> THIS andIn(Getters<T, U> getters, U... value);

    <U, F extends Getters<T, ? super U>> THIS andNotIn(Getters<T, U> getters, Collection<U> value);

    <U, F extends Getters<T, ? super U>> THIS andNotIn(Getters<T, U> getters, U... value);

    <U, F extends Getters<T, ? super U>> THIS orIn(Getters<T, U> getters, Collection<U> value);

    <U, F extends Getters<T, ? super U>> THIS orIn(Getters<T, U> getters, U... value);

    <U, F extends Getters<T, ? super U>> THIS orNotIn(Getters<T, U> getters, Collection<U> value);

    <U, F extends Getters<T, ? super U>> THIS orNotIn(Getters<T, U> getters, U... value);


}
