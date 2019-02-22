package com.github.jpa.spec;

import org.springframework.data.domain.Page;

import java.util.List;

import static org.springframework.util.Assert.state;

@SuppressWarnings("unchecked")
public interface Criteria<T> extends CriteriaFilter<T> {

    Page<T> getPage();

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

}
