package com.github.jpa.spec;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.List;

public interface Criteria<T> {

    static <T> Path<?> getPath(Root<T> root, String name) {
        String[] paths = name.split("\\.");
        Path<?> path = root;
        for (String p : paths) {
            path = path.get(p);
        }
        return path;
    }

    Page<T> getPage(Pageable pageable);

    default Page<T> getPage() {
        return getPage(getPageable());
    }

    Pageable getPageable();

    long count();

    default boolean exists() {
        return count() > 0;
    }

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

    Criteria<T> setPageable(Pageable pageable);

    Criteria<T> or();

    Criteria<T> and();

    Criteria<T> addOrderByDesc(String... attributeNames);

    Criteria<T> addOrderByAsc(String... attributeNames);

    void setPageResultEmpty();

    Criteria<T> groupBy(String... attributes);

    Criteria<T> criteria();

    Criteria<T> addSelect(String property);

    default Criteria<T> addSelect(String... propertys) {
        for (String property : propertys) {
            addSelect(property);
        }
        return this;
    }


    Criteria<T> addSelectMin(String property);

    Criteria<T> addSelectMax(String property);

    Criteria<T> addSelectSum(String property);

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

    Criteria<T> orEqualAsPath(String path, String other);

    Criteria<T> andNotEqualAsPath(String path, String other);

    Criteria<T> orNotNotEqualAsPath(String path, String other);

    Criteria<T> andEqual(String name, Object value);

    Criteria<T> orEqual(String name, Object value);

    Criteria<T> andNotEqual(String name, Object value);

    Criteria<T> orNotNotEqual(String name, Object value);

    Criteria<T> andEqIgnoreEmpty(String name, Object value);

    Criteria<T> orEqIgnoreEmpty(String name, Object value);

    Criteria<T> andNotEqIgnoreEmpty(String name, Object value);

    Criteria<T> orNotNotEqIgnoreEmpty(String name, Object value);

    default <Y extends Comparable<? super Y>> Criteria<T> andGe(String name, Y value) {
        return andGe(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <Y extends Comparable<? super Y>> Criteria<T> orGe(String name, Y value) {
        return orGe(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <Y extends Comparable<? super Y>> Criteria<T> andLe(String name, Y value) {
        return andLe(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <Y extends Comparable<? super Y>> Criteria<T> orLe(String name, Y value) {
        return orLe(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <Y extends Comparable<? super Y>> Criteria<T> andGt(String name, Y value) {
        return andGt(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <Y extends Comparable<? super Y>> Criteria<T> orGt(String name, Y value) {
        return orGt(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <Y extends Comparable<? super Y>> Criteria<T> andLt(String name, Y value) {
        return andLt(name, value, value == null ? null : (Class<Y>) value.getClass());
    }

    default <Y extends Comparable<? super Y>> Criteria<T> orLt(String name, Y value) {
        return orLt(name, value, value == null ? null : (Class<Y>) value.getClass());
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

    <Y extends Comparable<? super Y>> Criteria<T> orBetween(String name, Y value0, Y value1);

    <Y extends Comparable<? super Y>> Criteria<T> andNotBetween(String name, Y value0, Y value1);

    <Y extends Comparable<? super Y>> Criteria<T> orNotBetween(String name, Y value0, Y value1);

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

    Criteria<T> andLike(String name, String value);

    Criteria<T> andNotLike(String name, String value);

    Criteria<T> orLike(String name, String value);

    Criteria<T> orNotLike(String name, String value);

    <X> Criteria<T> andIn(String name, Collection<X> value);

    <X> Criteria<T> andIn(String name, X... value);

    <X> Criteria<T> andNotIn(String name, Collection<X> value);

    <X> Criteria<T> andNotIn(String name, X[] value);

    <X> Criteria<T> orIn(String name, Collection<X> value);

    <X> Criteria<T> orIn(String name, X[] value);

    <X> Criteria<T> orNotIn(String name, Collection<X> value);

    <X> Criteria<T> orNotIn(String name, X[] value);

    Predicate toPredicate();

}
