package com.github.data.query.specification;

public interface Selection<T> extends Expression<T> {

    default AggregateFunctions getAggregateFunctions(){
        return AggregateFunctions.NONE;
    }

}
