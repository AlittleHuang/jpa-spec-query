package com.github.alittlehuang.data.query.specification;

public interface Selection<T> extends Expression<T> {

    default AggregateFunctions getAggregateFunctions(){
        return AggregateFunctions.NONE;
    }

}
