package com.github.jpa.spec;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.LockModeType;
import javax.persistence.criteria.Predicate;
import java.util.Collection;
import java.util.List;

import static org.springframework.util.Assert.state;

@SuppressWarnings("unchecked")
public interface CriteriaFilter<T> {//筛选条件

    CriteriaFilter<T> setPageable(Pageable pageable);

    CriteriaFilter<T> limit(int offset, int maxResults);

    CriteriaFilter<T> limit(int maxResults);

    CriteriaFilter<T> limitMaxIndex(int maxIndex);

    CriteriaFilter<T> setPageable(int page, int size);

    /**
     * 返回一个新的Criteria,与原Criteria以OR条件链接
     */
    CriteriaFilter<T> or();

    /**
     * 返回一个新的Criteria,与原Criteria以AND条件链接
     */
    CriteriaFilter<T> and();

    CriteriaFilter<T> addOrderByDesc(String... attributeNames);

    CriteriaFilter<T> addOrderByAsc(String... attributeNames);

    CriteriaFilter<T> groupBy(String... attributes);

    CriteriaFilter<T> addSelect(String property);

    CriteriaFilter<T> addSelectMin(String property);

    CriteriaFilter<T> addSelectMax(String property);

    CriteriaFilter<T> addSelectSum(String property);

    CriteriaFilter<T> setOperator(Predicate.BooleanOperator operator);

    Predicate.BooleanOperator getOperator();

    CriteriaFilter<T> andEqualAsPath(String path, String other);

    CriteriaFilter<T> orEqualAsPath(String path, String other);

    CriteriaFilter<T> andNotEqualAsPath(String path, String other);

    CriteriaFilter<T> orNotEqualAsPath(String path, String other);

    CriteriaFilter<T> andEqual(String name, Object value);

    CriteriaFilter<T> orEqual(String name, Object value);

    CriteriaFilter<T> andNotEqual(String name, Object value);

    CriteriaFilter<T> orNotEqual(String name, Object value);

    CriteriaFilter<T> andEqIgnoreEmpty(String name, Object value);

    CriteriaFilter<T> orEqIgnoreEmpty(String name, Object value);

    CriteriaFilter<T> andNotEqIgnoreEmpty(String name, Object value);

    CriteriaFilter<T> orNotEqIgnoreEmpty(String name, Object value);

    <Y extends Comparable<? super Y>> CriteriaFilter<T> andGe(String name, Y value, Class<Y> type);

    <Y extends Comparable<? super Y>> CriteriaFilter<T> orGe(String name, Y value, Class<Y> type);

    <Y extends Comparable<? super Y>> CriteriaFilter<T> andGt(String name, Y value, Class<Y> type);

    <Y extends Comparable<? super Y>> CriteriaFilter<T> orGt(String name, Y value, Class<Y> type);

    <Y extends Comparable<? super Y>> CriteriaFilter<T> andLe(String name, Y value, Class<Y> type);

    <Y extends Comparable<? super Y>> CriteriaFilter<T> orLe(String name, Y value, Class<Y> type);

    <Y extends Comparable<? super Y>> CriteriaFilter<T> andLt(String name, Y value, Class<Y> type);

    <Y extends Comparable<? super Y>> CriteriaFilter<T> orLt(String name, Y value, Class<Y> type);

    <Y extends Comparable<? super Y>> CriteriaFilter<T> andBetween(String name, Y value0, Y value1, Class<Y> type);

    <Y extends Comparable<? super Y>> CriteriaFilter<T> orBetween(String name, Y value0, Y value1, Class<Y> type);

    <Y extends Comparable<? super Y>> CriteriaFilter<T> andNotBetween(String name, Y value0, Y value1, Class<Y> type);

    <Y extends Comparable<? super Y>> CriteriaFilter<T> orNotBetween(String name, Y value0, Y value1, Class<Y> type);

    <Y extends Comparable<? super Y>> CriteriaFilter<T> andBetween(String name, Y value0, Y value1);

    <Y extends Comparable<? super Y>> CriteriaFilter<T> orBetween(String name, Y value0, Y value1);

    <Y extends Comparable<? super Y>> CriteriaFilter<T> andNotBetween(String name, Y value0, Y value1);

    <Y extends Comparable<? super Y>> CriteriaFilter<T> orNotBetween(String name, Y value0, Y value1);

    CriteriaFilter<T> andIsNull(String name);

    CriteriaFilter<T> andIsNotNull(String name);

    CriteriaFilter<T> orIsNotNull(String name);

    CriteriaFilter<T> orIsNull(String name);

    CriteriaFilter<T> andLike(String name, String value);

    CriteriaFilter<T> andNotLike(String name, String value);

    CriteriaFilter<T> orLike(String name, String value);

    CriteriaFilter<T> orNotLike(String name, String value);

    <X> CriteriaFilter<T> andIn(String name, Collection<X> value);

    <X> CriteriaFilter<T> andIn(String name, X... value);

    <X> CriteriaFilter<T> andNotIn(String name, Collection<X> value);

    <X> CriteriaFilter<T> andNotIn(String name, X... value);

    <X> CriteriaFilter<T> orIn(String name, Collection<X> value);

    <X> CriteriaFilter<T> orIn(String name, X... value);

    <X> CriteriaFilter<T> orNotIn(String name, Collection<X> value);

    <X> CriteriaFilter<T> orNotIn(String name, X... value);

    Predicate toPredicate();

    CriteriaFilter<T> setLockMode(LockModeType lockModeType);

    CriteriaFilter<T> fetch(Getters<T, ?> getters);

    String getAttributeNameByGetter(Getters<T, ?> getters);

    default CriteriaFilter<T> addOrderByDesc(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            addOrderByDesc(getAttributeNameByGetter(getter));
        }
        return this;
    }

    default CriteriaFilter<T> addOrderByAsc(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            addOrderByAsc(getAttributeNameByGetter(getter));
        }
        return this;
    }

    default CriteriaFilter<T> groupBy(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            groupBy(getAttributeNameByGetter(getter));
        }
        return this;
    }

    default CriteriaFilter<T> addSelect(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            addSelect(getAttributeNameByGetter(getter));
        }
        return this;
    }

    default CriteriaFilter<T> addSelect(String... propertys) {
        for (String property : propertys) {
            addSelect(property);
        }
        return this;
    }

    default CriteriaFilter<T> addSelectMin(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            addSelectMin(getAttributeNameByGetter(getter));
        }
        return this;
    }

    default CriteriaFilter<T> addSelectMax(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            addSelectMax(getAttributeNameByGetter(getter));
        }
        return this;
    }

    default CriteriaFilter<T> addSelectSum(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            addSelectSum(getAttributeNameByGetter(getter));
        }
        return this;
    }

    default CriteriaFilter<T> addSelectSum(String... properties) {
        for (String property : properties) {
            addSelectSum(property);
        }
        return this;
    }

    default <U, G extends Getters<T, ? super U>> CriteriaFilter<T> andEqualAsPath(G path, G other) {
        return andEqualAsPath(getAttributeNameByGetter(path), getAttributeNameByGetter(other));
    }

    default <U, G extends Getters<T, ? super U>> CriteriaFilter<T> orEqualAsPath(G path, G other) {
        return orEqualAsPath(getAttributeNameByGetter(path), getAttributeNameByGetter(other));
    }

    default <U, G extends Getters<T, ? super U>> CriteriaFilter<T> andNotEqualAsPath(G path, G other) {
        return andNotEqualAsPath(getAttributeNameByGetter(path), getAttributeNameByGetter(other));
    }

    default <U, G extends Getters<T, ? super U>> CriteriaFilter<T> orNotEqualAsPath(G path, G other) {
        return orNotEqualAsPath(getAttributeNameByGetter(path), getAttributeNameByGetter(other));
    }

    default <U, F extends Getters<T, ? super U>> CriteriaFilter<T> andEqual(F getter, U value) {
        return andEqual(getAttributeNameByGetter(getter), value);
    }

    default <U, F extends Getters<T, ? super U>> CriteriaFilter<T> orEqual(F getter, U value) {
        return orEqual(getAttributeNameByGetter(getter), value);
    }

    default <U, F extends Getters<T, ? super U>> CriteriaFilter<T> andNotEqual(F getter, U value) {
        return andNotEqual(getAttributeNameByGetter(getter), value);
    }

    default <U, F extends Getters<T, ? super U>> CriteriaFilter<T> orNotEqual(F getter, U value) {
        return orNotEqual(getAttributeNameByGetter(getter), value);
    }

    default <Y extends Comparable<? super Y>> CriteriaFilter<T> andGe(String name, Y value) {
        return andGe(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> CriteriaFilter<T> andGe(F getter, U value) {
        return andGe(getAttributeNameByGetter(getter), value);
    }

    default <Y extends Comparable<? super Y>> CriteriaFilter<T> orGe(String name, Y value) {
        return orGe(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> CriteriaFilter<T> orGe(F getter, U value) {
        return orGe(getAttributeNameByGetter(getter), value);
    }

    default <Y extends Comparable<? super Y>> CriteriaFilter<T> andLe(String name, Y value) {
        return andLe(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> CriteriaFilter<T> andLe(F getter, U value) {
        return andLe(getAttributeNameByGetter(getter), value);
    }

    default <Y extends Comparable<? super Y>> CriteriaFilter<T> orLe(String name, Y value) {
        return orLe(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> CriteriaFilter<T> orLe(F getter, U value) {
        return orLe(getAttributeNameByGetter(getter), value);
    }

    default <Y extends Comparable<? super Y>> CriteriaFilter<T> andGt(String name, Y value) {
        return andGt(name, value, value != null ? (Class<Y>) value.getClass() : null);
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> CriteriaFilter<T> andGt(F getter, U value) {
        return andGt(getAttributeNameByGetter(getter), value);
    }

    default <Y extends Comparable<? super Y>> CriteriaFilter<T> orGt(String name, Y value) {
        return orGt(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> CriteriaFilter<T> orGt(F getter, U value) {
        return orGt(getAttributeNameByGetter(getter), value);
    }

    default <Y extends Comparable<? super Y>> CriteriaFilter<T> andLt(String name, Y value) {
        return andLt(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> CriteriaFilter<T> andLt(F getter, U value) {
        return andLt(getAttributeNameByGetter(getter), value);
    }

    default <Y extends Comparable<? super Y>> CriteriaFilter<T> orLt(String name, Y value) {
        return orLt(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> CriteriaFilter<T> orLt(F getter, U value) {
        return orLt(getAttributeNameByGetter(getter), value);
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> CriteriaFilter<T> andBetween(F getter, U value, U otherValue) {
        return andBetween(getAttributeNameByGetter(getter), value, otherValue);
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> CriteriaFilter<T> orBetween(F getter, U value, U otherValue) {
        return orBetween(getAttributeNameByGetter(getter), value, otherValue);
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> CriteriaFilter<T> andNotBetween(F getter, U value, U otherValue) {
        return andNotBetween(getAttributeNameByGetter(getter), value, otherValue);
    }

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> CriteriaFilter<T> orNotBetween(F getter, U value, U otherValue) {
        return orNotBetween(getAttributeNameByGetter(getter), value, otherValue);
    }

    default CriteriaFilter<T> andIsNull(String name, Boolean isNull) {
        if (isNull == null) {
            return this;
        } else if (isNull) {
            return andIsNull(name);
        } else {
            return andIsNotNull(name);
        }
    }

    default CriteriaFilter<T> orIsNull(String name, Boolean isNull) {
        if (isNull == null) {
            return this;
        } else if (isNull) {
            return orIsNull(name);
        } else {
            return orIsNotNull(name);
        }
    }

    default CriteriaFilter<T> andLike(Getters<T, String> getters, String value) {
        return andLike(getAttributeNameByGetter(getters), value);
    }

    default CriteriaFilter<T> andNotLike(Getters<T, String> getters, String value) {
        return andNotLike(getAttributeNameByGetter(getters), value);
    }

    default CriteriaFilter<T> orLike(Getters<T, String> getters, String value) {
        return orLike(getAttributeNameByGetter(getters), value);
    }

    default CriteriaFilter<T> orNotLike(Getters<T, String> getters, String value) {
        return orNotLike(getAttributeNameByGetter(getters), value);
    }

    default <U, F extends Getters<T, ? super U>> CriteriaFilter<T> andIn(Getters<T, U> getters, Collection<U> value) {
        return andIn(getAttributeNameByGetter(getters), value);
    }

    default <U, F extends Getters<T, ? super U>> CriteriaFilter<T> andIn(Getters<T, U> getters, U... value) {
        return andIn(getAttributeNameByGetter(getters), value);
    }

    default <U, F extends Getters<T, ? super U>> CriteriaFilter<T> andNotIn(Getters<T, U> getters, Collection<U> value) {
        return andNotIn(getAttributeNameByGetter(getters), value);
    }

    default <U, F extends Getters<T, ? super U>> CriteriaFilter<T> andNotIn(Getters<T, U> getters, U... value) {
        return andNotIn(getAttributeNameByGetter(getters), value);
    }

    default <U, F extends Getters<T, ? super U>> CriteriaFilter<T> orIn(Getters<T, U> getters, Collection<U> value) {
        return orIn(getAttributeNameByGetter(getters), value);
    }

    default <U, F extends Getters<T, ? super U>> CriteriaFilter<T> orIn(Getters<T, U> getters, U... value) {
        return orIn(getAttributeNameByGetter(getters), value);
    }

    default <U, F extends Getters<T, ? super U>> CriteriaFilter<T> orNotIn(Getters<T, U> getters, Collection<U> value) {
        return orNotIn(getAttributeNameByGetter(getters), value);
    }

    default <U, F extends Getters<T, ? super U>> CriteriaFilter<T> orNotIn(Getters<T, U> getters, U... value) {
        return orNotIn(getAttributeNameByGetter(getters), value);
    }

}
