package com.github.alittlehuang.data.query.specification;


import com.github.alittlehuang.data.query.page.Page;
import com.github.alittlehuang.data.util.Assert;

import java.util.List;


public interface QueryStored<T> {

    List<T> getResultList();

    default T getSingleResult() {
        List<T> list = getResultList();
        Assert.state(list.size() <= 1, "found more than one");
        return list.isEmpty() ? null : list.get(0);
    }

    <X> List<X> getObjectList();

    default <X> X getSingleObject(){
        List<X> list = getObjectList();
        Assert.state(list.size() <= 1, "found more than one");
        return list.isEmpty() ? null : list.get(0);
    }

    Page<T> getPage(long page, long size);

    Page<T> getPage();

    long count();

    boolean exists();

    Class<T> getJavaType();

}
