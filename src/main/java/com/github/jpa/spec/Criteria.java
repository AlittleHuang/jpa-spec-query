package com.github.jpa.spec;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.LockModeType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unchecked")
public interface Criteria<T> {

    static <T> Path<?> getPath(Root<T> root, String name) {
        String[] paths = name.split("\\.");
        Path<?> path = root;
        for (String p : paths) {
            path = path.get(p);
        }
        return path;
    }

    Class<T> getType();

    Page<T> getPage(Pageable pageable);

    default Page<T> getPage() {
        return getPage(getPageable());
    }

    Pageable getPageable();

    Criteria<T> setPageable(Pageable pageable);

    long count();

    boolean exists();

    List<?> getObjList();

    List<T> getList();

    default T getOne() {
        List<T> list = getList();
        org.springframework.util.Assert.state(list.size() <= 1, "find more than one");
        return list.isEmpty() ? null : list.get(0);
    }

    default Object getOneObject() {
        List<?> list = getObjList();
        org.springframework.util.Assert.state(list.size() <= 1, "find more than one");
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

    default Criteria<T> addOrderByDesc(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            addOrderByDesc(getAttributeNameByGetter(getter));
        }
        return this;
    }

    Criteria<T> addOrderByAsc(String... attributeNames);

    default Criteria<T> addOrderByAsc(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            addOrderByAsc(getAttributeNameByGetter(getter));
        }
        return this;
    }

    void setPageResultEmpty();

    Criteria<T> groupBy(String... attributes);

    default Criteria<T> groupBy(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            groupBy(getAttributeNameByGetter(getter));
        }
        return this;
    }

    Criteria<T> addSelect(String property);

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

    Criteria<T> addSelectMin(String property);

    default Criteria<T> addSelectMin(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            addSelectMin(getAttributeNameByGetter(getter));
        }
        return this;
    }

    Criteria<T> addSelectMax(String property);

    default Criteria<T> addSelectMax(Getters<T, ?>... getters) {
        for (Getters<T, ?> getter : getters) {
            addSelectMax(getAttributeNameByGetter(getter));
        }
        return this;
    }

    Criteria<T> addSelectSum(String property);

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

    Predicate toPredicate(List<Criteria<T>> criteriaList);

    Criteria<T> operator(Predicate.BooleanOperator operator);

    Predicate.BooleanOperator getOperator();

    Criteria<T> andEqualAsPath(String path, String other);

    default <U, G extends Getters<T, ? super U>> Criteria<T> andEqualAsPath(G path, G other) {
        return andEqualAsPath(getAttributeNameByGetter(path), getAttributeNameByGetter(other));
    }

    Criteria<T> orEqualAsPath(String path, String other);

    default <U, G extends Getters<T, ? super U>> Criteria<T> orEqualAsPath(G path, G other) {
        return orEqualAsPath(getAttributeNameByGetter(path), getAttributeNameByGetter(other));
    }

    Criteria<T> andNotEqualAsPath(String path, String other);

    default <U, G extends Getters<T, ? super U>> Criteria<T> andNotEqualAsPath(G path, G other) {
        return andNotEqualAsPath(getAttributeNameByGetter(path), getAttributeNameByGetter(other));
    }

    Criteria<T> orNotEqualAsPath(String path, String other);

    default <U, G extends Getters<T, ? super U>> Criteria<T> orNotEqualAsPath(G path, G other) {
        return orNotEqualAsPath(getAttributeNameByGetter(path), getAttributeNameByGetter(other));
    }

    Criteria<T> andEqual(String name, Object value);

    default <U, F extends Getters<T, ? super U>> Criteria<T> andEqual(F getter, U value) {
        return andEqual(getAttributeNameByGetter(getter), value);
    }

    Criteria<T> orEqual(String name, Object value);

    default <U, F extends Getters<T, ? super U>> Criteria<T> orEqual(F getter, U value) {
        return orEqual(getAttributeNameByGetter(getter), value);
    }

    Criteria<T> andNotEqual(String name, Object value);

    default <U, F extends Getters<T, ? super U>> Criteria<T> andNotEqual(F getter, U value) {
        return andNotEqual(getAttributeNameByGetter(getter), value);
    }

    Criteria<T> orNotEqual(String name, Object value);

    default <U, F extends Getters<T, ? super U>> Criteria<T> orNotEqual(F getter, U value) {
        return orNotEqual(getAttributeNameByGetter(getter), value);
    }

    Criteria<T> andEqIgnoreEmpty(String name, Object value);

    Criteria<T> orEqIgnoreEmpty(String name, Object value);

    Criteria<T> andNotEqIgnoreEmpty(String name, Object value);

    Criteria<T> orNotEqIgnoreEmpty(String name, Object value);

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

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> Criteria<T> andBetween(F getter, U value, U otherValue) {
        return andBetween(getAttributeNameByGetter(getter), value, otherValue);
    }

    <Y extends Comparable<? super Y>> Criteria<T> orBetween(String name, Y value0, Y value1);

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> Criteria<T> orBetween(F getter, U value, U otherValue) {
        return orBetween(getAttributeNameByGetter(getter), value, otherValue);
    }

    <Y extends Comparable<? super Y>> Criteria<T> andNotBetween(String name, Y value0, Y value1);

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> Criteria<T> andNotBetween(F getter, U value, U otherValue) {
        return andNotBetween(getAttributeNameByGetter(getter), value, otherValue);
    }

    <Y extends Comparable<? super Y>> Criteria<T> orNotBetween(String name, Y value0, Y value1);

    default <U extends Comparable<? super U>, F extends Getters<T, ? super U>> Criteria<T> orNotBetween(F getter, U value, U otherValue) {
        return orNotBetween(getAttributeNameByGetter(getter), value, otherValue);
    }

    Criteria<T> andIsNull(String name);

    default Criteria<T> andIsNull(String name, Boolean isNull) {
        if (isNull == null) {
            return this;
        } else if (isNull) {
            return andIsNull(name);
        } else {
            return andIsNotNull(name);
        }
    }

    Criteria<T> andIsNotNull(String name);

    Criteria<T> orIsNotNull(String name);

    Criteria<T> orIsNull(String name);

    default Criteria<T> orIsNull(String name, Boolean isNull) {
        if (isNull == null) {
            return this;
        } else if (isNull) {
            return orIsNull(name);
        } else {
            return orIsNotNull(name);
        }
    }

    Criteria<T> andLike(String name, String value);

    default Criteria<T> andLike(Getters<T, String> getters, String value) {
        return andLike(getAttributeNameByGetter(getters), value);
    }

    Criteria<T> andNotLike(String name, String value);

    default Criteria<T> andNotLike(Getters<T, String> getters, String value) {
        return andNotLike(getAttributeNameByGetter(getters), value);
    }

    Criteria<T> orLike(String name, String value);

    default Criteria<T> orLike(Getters<T, String> getters, String value) {
        return orLike(getAttributeNameByGetter(getters), value);
    }

    Criteria<T> orNotLike(String name, String value);

    default Criteria<T> orNotLike(Getters<T, String> getters, String value) {
        return orNotLike(getAttributeNameByGetter(getters), value);
    }

    <X> Criteria<T> andIn(String name, Collection<X> value);

    default <U, F extends Getters<T, ? super U>> Criteria<T> andIn(Getters<T, U> getters, Collection<U> value) {
        return andIn(getAttributeNameByGetter(getters), value);
    }

    <X> Criteria<T> andIn(String name, X... value);

    default <U, F extends Getters<T, ? super U>> Criteria<T> andIn(Getters<T, U> getters, U... value) {
        return andIn(getAttributeNameByGetter(getters), value);
    }

    <X> Criteria<T> andNotIn(String name, Collection<X> value);

    default <U, F extends Getters<T, ? super U>> Criteria<T> andNotIn(Getters<T, U> getters, Collection<U> value) {
        return andNotIn(getAttributeNameByGetter(getters), value);
    }

    <X> Criteria<T> andNotIn(String name, X... value);

    default <U, F extends Getters<T, ? super U>> Criteria<T> andNotIn(Getters<T, U> getters, U... value) {
        return andNotIn(getAttributeNameByGetter(getters), value);
    }

    <X> Criteria<T> orIn(String name, Collection<X> value);

    default <U, F extends Getters<T, ? super U>> Criteria<T> orIn(Getters<T, U> getters, Collection<U> value) {
        return orIn(getAttributeNameByGetter(getters), value);
    }

    <X> Criteria<T> orIn(String name, X... value);

    default <U, F extends Getters<T, ? super U>> Criteria<T> orIn(Getters<T, U> getters, U... value) {
        return orIn(getAttributeNameByGetter(getters), value);
    }

    <X> Criteria<T> orNotIn(String name, Collection<X> value);

    default <U, F extends Getters<T, ? super U>> Criteria<T> orNotIn(Getters<T, U> getters, Collection<U> value) {
        return orNotIn(getAttributeNameByGetter(getters), value);
    }

    <X> Criteria<T> orNotIn(String name, X... value);

    default <U, F extends Getters<T, ? super U>> Criteria<T> orNotIn(Getters<T, U> getters, U... value) {
        return orNotIn(getAttributeNameByGetter(getters), value);
    }

    Predicate toPredicate();

    Criteria<T> setLockMode(LockModeType lockModeType);

    Criteria<T> fetch(Getters<T, ?> getters);

    String getAttributeNameByGetter(Getters<T, ?> getters);

}
