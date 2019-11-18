package com.github.alittlehuang.data.query.support.model;

import com.github.alittlehuang.data.query.specification.AggregateFunctions;
import com.github.alittlehuang.data.query.specification.Selection;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class SelectionModel<T> extends ExpressionModel<T> implements Selection<T>, Serializable {
    protected AggregateFunctions aggregateFunctions;

    public SelectionModel() {
    }

    public SelectionModel(AggregateFunctions aggregateFunctions) {
        this.aggregateFunctions = aggregateFunctions;
    }

    public SelectionModel(Selection<T> selectionModel, Class<? extends T> javaType) {
        super(selectionModel, javaType);
        this.aggregateFunctions = selectionModel.getAggregateFunctions();
    }
}
