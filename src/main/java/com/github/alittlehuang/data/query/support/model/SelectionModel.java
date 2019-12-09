package com.github.alittlehuang.data.query.support.model;

import com.github.alittlehuang.data.query.specification.AggregateFunctions;
import com.github.alittlehuang.data.query.specification.Selection;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class SelectionModel<T> extends ExpressionModel<T> implements Selection<T>, Serializable {
    protected AggregateFunctions aggregateFunctions = AggregateFunctions.NONE;

    public SelectionModel() {
    }

    public SelectionModel(AggregateFunctions aggregateFunctions) {
        this.aggregateFunctions = aggregateFunctions;
    }

    public SelectionModel(String[] names) {
        super(names);
    }

    public SelectionModel(Selection<T> selectionModel, Class<? extends T> javaType) {
        super(selectionModel);
        this.aggregateFunctions = selectionModel.getAggregateFunctions();
    }

    public static <T> SelectionModel<T> convertSelection(Selection<T> selection,
                                                         Class<? extends T> javaType) {

        if (selection.getClass() == SelectionModel.class) {
            return (SelectionModel<T>) selection;
        }

        return new SelectionModel<>(selection, javaType);
    }

    public static <T> List<SelectionModel<T>> convertSelection(List<? extends Selection<T>> selection,
                                                               Class<? extends T> javaType) {
        if (selection == null || selection.isEmpty()) {
            return Collections.emptyList();
        }
        return selection.stream().map(i -> convertSelection(i, javaType)).collect(Collectors.toList());
    }

}
