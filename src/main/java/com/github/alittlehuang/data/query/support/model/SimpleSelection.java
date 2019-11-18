package com.github.alittlehuang.data.query.support.model;

import com.github.alittlehuang.data.query.specification.AggregateFunctions;
import com.github.alittlehuang.data.query.specification.Selection;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleSelection<T> extends ExpressionModel<T> implements Selection<T>, Serializable {
    protected AggregateFunctions aggregateFunctions;

    @Override
    public AggregateFunctions getAggregateFunctions() {
        return null;
    }
}
