package com.github.data.query.specification;

public interface Selection<T> extends Expressions<T> {

    default AggregateFunctions getAggregateFunctions(){
        return AggregateFunctions.NONE;
    }

}
