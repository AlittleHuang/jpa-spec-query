package com.github.alittlehuang.data.query.specification;

/**
 * @author ALittleHuang
 */
public interface Selection<T> extends Expression<T> {

    default AggregateFunctions getAggregateFunctions(){
        return AggregateFunctions.NONE;
    }

}
