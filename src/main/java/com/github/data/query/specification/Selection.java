package com.github.data.query.specification;

public interface Selection<T> extends Attribute<T> {

    default AggregateFunctions getAggregateFunctions(){
        return AggregateFunctions.NONE;
    }

}
