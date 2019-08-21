package com.github.alittlehuang.data.query.specification;

/**
 * @author ALittleHuang
 */
public interface AttributePath<T> {

//    String[] getNames();

    String[] getNames(Class<? extends T> type);

}
