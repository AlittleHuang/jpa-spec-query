package com.github.jpa.spec;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.LockModeType;
import javax.persistence.criteria.Predicate;
import java.util.Collection;
import java.util.List;

import static org.springframework.util.Assert.*;

@SuppressWarnings("unchecked")
public interface Criteria<T> {

    Page<T> getPage();

    Criteria<T> setPageable(Pageable pageable);

    long count();

    boolean exists();

    <X> List<X> getObjList();

    List<T> getList();

    default T getOne() {
        List<T> list = getList();
        state(list.size() <= 1, "found more than one");
        return list.isEmpty() ? null : list.get(0);
    }

    default Object getOneObject() {
        List<?> list = getObjList();
        state(list.size() <= 1, "found more than one");
        return list.isEmpty() ? null : list.get(0);
    }

    Criteria<T> limit(int offset, int maxResults);

    Criteria<T> limit(int maxResults);

    Criteria<T> limitMaxIndex(int maxIndex);

    Criteria<T> setPageable(int page, int size);

    /**
     * 返回一个新的Criteria,与原Criteria以OR条件链接
     */
    Criteria<T> or();

    /**
     * 返回一个新的Criteria,与原Criteria以AND条件链接
     */
    Criteria<T> and();

    Criteria<T> addOrderByDesc(String... attributeNames);

    Criteria<T> addOrderByAsc(String... attributeNames);

    Criteria<T> groupBy(String... attributes);

    Criteria<T> addSelect(String property);

    Criteria<T> addSelectMin(String property);

    Criteria<T> addSelectMax(String property);

    Criteria<T> addSelectSum(String property);

    Criteria<T> setOperator(Predicate.BooleanOperator operator);

    Predicate.BooleanOperator getOperator();

    Criteria<T> andEqualAsPath(String path, String other);

    Criteria<T> orEqualAsPath(String path, String other);

    Criteria<T> andNotEqualAsPath(String path, String other);

    Criteria<T> orNotEqualAsPath(String path, String other);

    Criteria<T> andEqual(String name, Object value);

    Criteria<T> orEqual(String name, Object value);

    Criteria<T> andNotEqual(String name, Object value);

    Criteria<T> orNotEqual(String name, Object value);

    Criteria<T> andEqIgnoreEmpty(String name, Object value);

    Criteria<T> orEqIgnoreEmpty(String name, Object value);

    Criteria<T> andNotEqIgnoreEmpty(String name, Object value);

    Criteria<T> orNotEqIgnoreEmpty(String name, Object value);

    <Y extends Comparable<? super Y>> Criteria<T> andGe(String name, Y value, Class<Y> type);

    <Y extends Comparable<? super Y>> Criteria<T> orGe(String name, Y value, Class<Y> type);

    <Y extends Comparable<? super Y>> Criteria<T> andGt(String name, Y value, Class<Y> type);

    <Y extends Comparable<? super Y>> Criteria<T> orGt(String name, Y value, Class<Y> type);

    <Y extends Comparable<? super Y>> Criteria<T> andLe(String name, Y value, Class<Y> type);

    <Y extends Comparable<? super Y>> Criteria<T> orLe(String name, Y value, Class<Y> type);

    <Y extends Comparable<? super Y>> Criteria<T> andLt(String name, Y value, Class<Y> type);

    <Y extends Comparable<? super Y>> Criteria<T> orLt(String name, Y value, Class<Y> type);

    <Y extends Comparable<? super Y>> Criteria<T> andBetween(String name, Y value0, Y value1, Class<Y> type);

    <Y extends Comparable<? super Y>> Criteria<T> orBetween(String name, Y value0, Y value1, Class<Y> type);

    <Y extends Comparable<? super Y>> Criteria<T> andNotBetween(String name, Y value0, Y value1, Class<Y> type);

    <Y extends Comparable<? super Y>> Criteria<T> orNotBetween(String name, Y value0, Y value1, Class<Y> type);

    <Y extends Comparable<? super Y>> Criteria<T> andBetween(String name, Y value0, Y value1);

    <Y extends Comparable<? super Y>> Criteria<T> orBetween(String name, Y value0, Y value1);

    <Y extends Comparable<? super Y>> Criteria<T> andNotBetween(String name, Y value0, Y value1);

    <Y extends Comparable<? super Y>> Criteria<T> orNotBetween(String name, Y value0, Y value1);

    Criteria<T> andIsNull(String name);

    Criteria<T> andIsNotNull(String name);

    Criteria<T> orIsNotNull(String name);

    Criteria<T> orIsNull(String name);

    Criteria<T> andLike(String name, String value);

    Criteria<T> andNotLike(String name, String value);

    Criteria<T> orLike(String name, String value);

    Criteria<T> orNotLike(String name, String value);

    <X> Criteria<T> andIn(String name, Collection<X> value);

    <X> Criteria<T> andIn(String name, X... value);

    <X> Criteria<T> andNotIn(String name, Collection<X> value);

    <X> Criteria<T> andNotIn(String name, X... value);

    <X> Criteria<T> orIn(String name, Collection<X> value);

    <X> Criteria<T> orIn(String name, X... value);

    <X> Criteria<T> orNotIn(String name, Collection<X> value);

    <X> Criteria<T> orNotIn(String name, X... value);

    Predicate toPredicate();

    Criteria<T> setLockMode(LockModeType lockModeType);

    Criteria<T> fetch(Getters<T, ?> getters);


    String getAttributeNameByGetter(Getters<T, ?> getters);

    default Criteria<T> addOrderByDesc(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            addOrderByDesc(getAttributeNameByGetter(getter));
        }
        return this;
    }

    default Criteria<T> addOrderByAsc(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            addOrderByAsc(getAttributeNameByGetter(getter));
        }
        return this;
    }

    default Criteria<T> groupBy(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            groupBy(getAttributeNameByGetter(getter));
        }
        return this;
    }

    default Criteria<T> addSelect(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            addSelect(getAttributeNameByGetter(getter));
        }
        return this;
    }

    default Criteria<T> addSelect(String... propertys) {
        for (String property : propertys) {
            addSelect(property);
        }
        return this;
    }

    default Criteria<T> addSelectMin(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            addSelectMin(getAttributeNameByGetter(getter));
        }
        return this;
    }

    default Criteria<T> addSelectMax(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            addSelectMax(getAttributeNameByGetter(getter));
        }
        return this;
    }

    default Criteria<T> addSelectSum(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            addSelectSum(getAttributeNameByGetter(getter));
        }
        return this;
    }

    default Criteria<T> addSelectSum(String... properties) {
        for (String property : properties) {
            addSelectSum(property);
        }
        return this;
    }

    default <U, G extends Getters<T, ? super U>> Criteria<T> andEqualAsPath(G path, G other) {
        return andEqualAsPath(getAttributeNameByGetter(path), getAttributeNameByGetter(other));
    }

    default <U, G extends Getters<T, ? super U>> Criteria<T> orEqualAsPath(G path, G other) {
        return orEqualAsPath(getAttributeNameByGetter(path), getAttributeNameByGetter(other));
    }

    default <U, G extends Getters<T, ? super U>> Criteria<T> andNotEqualAsPath(G path, G other) {
        return andNotEqualAsPath(getAttributeNameByGetter(path), getAttributeNameByGetter(other));
    }

    default <U, G extends Getters<T, ? super U>> Criteria<T> orNotEqualAsPath(G path, G other) {
        return orNotEqualAsPath(getAttributeNameByGetter(path), getAttributeNameByGetter(other));
    }

    default <U, F extends Getters<T, ? super U>> Criteria<T> andEqual(F getter, U value) {
        return andEqual(getAttributeNameByGetter(getter), value);
    }

    default <U, F extends Getters<T, ? super U>> Criteria<T> orEqual(F getter, U value) {
        return orEqual(getAttributeNameByGetter(getter), value);
    }

    default <U, F extends Getters<T, ? super U>> Criteria<T> andNotEqual(F getter, U value) {
        return andNotEqual(getAttributeNameByGetter(getter), value);
    }

    default <U, F extends Getters<T, ? super U>> Criteria<T> orNotEqual(F getter, U value) {
        return orNotEqual(getAttributeNameByGetter(getter), value);
    }

    default <Y extends Comparable<? super Y>> Criteria<T> andGe(String name, Y value) {
        return andGe(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> Criteria<T> andGe(F getter, U value) {
        return andGe(getAttributeNameByGetter(getter), value);
    }

    default <Y extends Comparable<? super Y>> Criteria<T> orGe(String name, Y value) {
        return orGe(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> Criteria<T> orGe(F getter, U value) {
        return orGe(getAttributeNameByGetter(getter), value);
    }

    default <Y extends Comparable<? super Y>> Criteria<T> andLe(String name, Y value) {
        return andLe(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> Criteria<T> andLe(F getter, U value) {
        return andLe(getAttributeNameByGetter(getter), value);
    }

    default <Y extends Comparable<? super Y>> Criteria<T> orLe(String name, Y value) {
        return orLe(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> Criteria<T> orLe(F getter, U value) {
        return orLe(getAttributeNameByGetter(getter), value);
    }

    default <Y extends Comparable<? super Y>> Criteria<T> andGt(String name, Y value) {
        return andGt(name, value, value != null ? (Class<Y>) value.getClass() : null);
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> Criteria<T> andGt(F getter, U value) {
        return andGt(getAttributeNameByGetter(getter), value);
    }

    default <Y extends Comparable<? super Y>> Criteria<T> orGt(String name, Y value) {
        return orGt(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> Criteria<T> orGt(F getter, U value) {
        return orGt(getAttributeNameByGetter(getter), value);
    }

    default <Y extends Comparable<? super Y>> Criteria<T> andLt(String name, Y value) {
        return andLt(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> Criteria<T> andLt(F getter, U value) {
        return andLt(getAttributeNameByGetter(getter), value);
    }

    default <Y extends Comparable<? super Y>> Criteria<T> orLt(String name, Y value) {
        return orLt(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> Criteria<T> orLt(F getter, U value) {
        return orLt(getAttributeNameByGetter(getter), value);
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> Criteria<T> andBetween(F getter, U value, U otherValue) {
        return andBetween(getAttributeNameByGetter(getter), value, otherValue);
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> Criteria<T> orBetween(F getter, U value, U otherValue) {
        return orBetween(getAttributeNameByGetter(getter), value, otherValue);
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> Criteria<T> andNotBetween(F getter, U value, U otherValue) {
        return andNotBetween(getAttributeNameByGetter(getter), value, otherValue);
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> Criteria<T> orNotBetween(F getter, U value, U otherValue) {
        return orNotBetween(getAttributeNameByGetter(getter), value, otherValue);
    }

    default Criteria<T> andIsNull(String name, Boolean isNull) {
        if (isNull == null) {
            return this;
        } else if (isNull) {
            return andIsNull(name);
        } else {
            return andIsNotNull(name);
        }
    }

    default Criteria<T> orIsNull(String name, Boolean isNull) {
        if (isNull == null) {
            return this;
        } else if (isNull) {
            return orIsNull(name);
        } else {
            return orIsNotNull(name);
        }
    }

    default Criteria<T> andLike(Getters<T, String> getters, String value) {
        return andLike(getAttributeNameByGetter(getters), value);
    }

    default Criteria<T> andNotLike(Getters<T, String> getters, String value) {
        return andNotLike(getAttributeNameByGetter(getters), value);
    }

    default Criteria<T> orLike(Getters<T, String> getters, String value) {
        return orLike(getAttributeNameByGetter(getters), value);
    }

    default Criteria<T> orNotLike(Getters<T, String> getters, String value) {
        return orNotLike(getAttributeNameByGetter(getters), value);
    }

    default <U, F extends Getters<T, ? super U>> Criteria<T> andIn(Getters<T, U> getters, Collection<U> value) {
        return andIn(getAttributeNameByGetter(getters), value);
    }

    default <U, F extends Getters<T, ? super U>> Criteria<T> andIn(Getters<T, U> getters, U... value) {
        return andIn(getAttributeNameByGetter(getters), value);
    }

    default <U, F extends Getters<T, ? super U>> Criteria<T> andNotIn(Getters<T, U> getters, Collection<U> value) {
        return andNotIn(getAttributeNameByGetter(getters), value);
    }

    default <U, F extends Getters<T, ? super U>> Criteria<T> andNotIn(Getters<T, U> getters, U... value) {
        return andNotIn(getAttributeNameByGetter(getters), value);
    }

    default <U, F extends Getters<T, ? super U>> Criteria<T> orIn(Getters<T, U> getters, Collection<U> value) {
        return orIn(getAttributeNameByGetter(getters), value);
    }

    default <U, F extends Getters<T, ? super U>> Criteria<T> orIn(Getters<T, U> getters, U... value) {
        return orIn(getAttributeNameByGetter(getters), value);
    }

    default <U, F extends Getters<T, ? super U>> Criteria<T> orNotIn(Getters<T, U> getters, Collection<U> value) {
        return orNotIn(getAttributeNameByGetter(getters), value);
    }

    default <U, F extends Getters<T, ? super U>> Criteria<T> orNotIn(Getters<T, U> getters, U... value) {
        return orNotIn(getAttributeNameByGetter(getters), value);
    }

}
