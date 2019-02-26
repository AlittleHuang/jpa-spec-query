package com.github.data.query.specification;

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

    THIS andIsNull(Path<T,?> name);

    THIS andIsNotNull(Path<T,?> name);

    THIS orIsNotNull(Path<T,?> name);

    THIS orIsNull(Path<T,?> name);

    THIS andLike(String name, String value);

    THIS andNotLike(String name, String value);

    THIS orLike(String name, String value);

    THIS orNotLike(String name, String value);

    <X> THIS andIn(String name, Collection<X> value);

    @SuppressWarnings("unchecked")
    <X> THIS andIn(String name, X... value);

    <X> THIS andNotIn(String name, Collection<X> value);

    @SuppressWarnings("unchecked")
    <X> THIS andNotIn(String name, X... value);

    <X> THIS orIn(String name, Collection<X> value);

    @SuppressWarnings("unchecked")
    <X> THIS orIn(String name, X... value);

    <X> THIS orNotIn(String name, Collection<X> value);

    @SuppressWarnings("unchecked")
    <X> THIS orNotIn(String name, X... value);

    <U, G extends Path<T, ? super U>> THIS andEqual(G path, G other);

    <U, G extends Path<T, ? super U>> THIS orEqual(G path, G other);

    <U, G extends Path<T, ? super U>> THIS andNotEqual(G path, G other);

    <U, G extends Path<T, ? super U>> THIS orNotEqual(G path, G other);

    <U, F extends Path<T, ? super U>> THIS andEqual(F getter, U value);

    <U, F extends Path<T, ? super U>> THIS orEqual(F getter, U value);

    <U, F extends Path<T, ? super U>> THIS andNotEqual(F getter, U value);

    <U, F extends Path<T, ? super U>> THIS orNotEqual(F getter, U value);

    <Y extends Comparable<? super Y>> THIS andGe(String name, Y value);

    <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS andGe(F getter, U value);

    <Y extends Comparable<? super Y>> THIS orGe(String name, Y value);

    <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS orGe(F getter, U value);

    <Y extends Comparable<? super Y>> THIS andLe(String name, Y value);

    <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS andLe(F getter, U value);

    <Y extends Comparable<? super Y>> THIS orLe(String name, Y value);

    <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS orLe(F getter, U value);

    <Y extends Comparable<? super Y>> THIS andGt(String name, Y value);

    <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS andGt(F getter, U value);

    <Y extends Comparable<? super Y>> THIS orGt(String name, Y value);

    <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS orGt(F getter, U value);

    <Y extends Comparable<? super Y>> THIS andLt(String name, Y value);

    <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS andLt(F getter, U value);

    <Y extends Comparable<? super Y>> THIS orLt(String name, Y value);

    <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS orLt(F getter, U value);

    <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS andBetween(F getter, U value, U otherValue);

    <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS orBetween(F getter, U value, U otherValue);

    <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS andNotBetween(F getter, U value, U otherValue);

    <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS orNotBetween(F getter, U value, U otherValue);

    THIS andIsNull(String name, Boolean isNull);

    THIS orIsNull(String name, Boolean isNull);

    THIS andLike(Path<T, String> getters, String value);

    THIS andNotLike(Path<T, String> getters, String value);

    THIS orLike(Path<T, String> getters, String value);

    THIS orNotLike(Path<T, String> getters, String value);

    <U, F extends Path<T, ? super U>> THIS andIn(Path<T, U> getters, Collection<U> value);

    @SuppressWarnings("unchecked")
    <U, F extends Path<T, ? super U>> THIS andIn(Path<T, U> getters, U... value);

    <U, F extends Path<T, ? super U>> THIS andNotIn(Path<T, U> getters, Collection<U> value);

    @SuppressWarnings("unchecked")
    <U, F extends Path<T, ? super U>> THIS andNotIn(Path<T, U> getters, U... value);

    <U, F extends Path<T, ? super U>> THIS orIn(Path<T, U> getters, Collection<U> value);

    @SuppressWarnings("unchecked")
    <U, F extends Path<T, ? super U>> THIS orIn(Path<T, U> getters, U... value);

    <U, F extends Path<T, ? super U>> THIS orNotIn(Path<T, U> getters, Collection<U> value);

    @SuppressWarnings("unchecked")
    <U, F extends Path<T, ? super U>> THIS orNotIn(Path<T, U> getters, U... value);

}
