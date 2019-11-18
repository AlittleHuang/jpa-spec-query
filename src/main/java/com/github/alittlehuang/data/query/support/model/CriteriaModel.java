package com.github.alittlehuang.data.query.support.model;

import com.github.alittlehuang.data.query.specification.Criteria;
import com.github.alittlehuang.data.query.specification.WhereClause;
import lombok.Data;

import javax.persistence.LockModeType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CriteriaModel<T> implements Criteria<T>, Serializable {

    protected final WhereClauseModel<T> whereClause;
    protected final List<SelectionModel<T>> selections = new ArrayList<>();
    protected final List<ExpressionModel<T>> groupings = new ArrayList<>();
    protected final List<OrdersModel<T>> orders = new ArrayList<>();
    protected final List<FetchAttributeModel<T>> fetchAttributes = new ArrayList<>();
    private final Class<T> javaType;
    private LockModeType lockModeType;
    private Long offset;
    private Long maxResults;

    public CriteriaModel(WhereClause<T> whereClause, Class<T> type) {
        this.whereClause = WhereClauseModel.convert(whereClause, type);
        this.javaType = type;
    }
}
