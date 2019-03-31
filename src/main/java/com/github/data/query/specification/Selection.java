package com.github.data.query.specification;

public interface Selection<T> extends AttrExpression<T> {

    default AggregateFunctions getAggregateFunctions(){
        return AggregateFunctions.NONE;
    }

}
