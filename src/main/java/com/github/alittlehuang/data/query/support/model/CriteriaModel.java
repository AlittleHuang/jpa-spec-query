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
    private final Class<T> javaType;
    private LockModeType lockModeType;
    private Long offset;
    private Long maxResults;

    protected final List<SelectionModel<T>> selections = new ArrayList<>();
    protected final List<ExpressionModel<T>> groupings = new ArrayList<>();
    protected final List<OrdersModel<T>> orders = new ArrayList<>();
    protected final List<FetchAttributeModel<T>> fetchAttributes = new ArrayList<>();

    public CriteriaModel(WhereClause<T> whereClause, Class<T> type) {
        this.whereClause = WhereClauseModel.convert(whereClause, type);
        this.javaType = type;
    }

    public static <T> CriteriaModel<T> convert(Criteria<T> criteria) {

        if (criteria.getClass() == CriteriaModel.class) {
            return (CriteriaModel<T>) criteria;
        }

        Class<T> javaType = criteria.getJavaType();
        CriteriaModel<T> result = new CriteriaModel<>(criteria.getWhereClause(), javaType);

        result.getSelections().addAll(SelectionModel.convertSelection(criteria.getSelections(), javaType));
        result.getGroupings().addAll(ExpressionModel.convertExpression(criteria.getGroupings()));
        result.getOrders().addAll(OrdersModel.convertOrders(criteria.getOrders()));
        result.getFetchAttributes().addAll(FetchAttributeModel.convertFetch(criteria.getFetchAttributes()));
        result.setOffset(criteria.getOffset());
        result.setMaxResults(criteria.getMaxResults());
        result.setLockModeType(criteria.getLockModeType());

        return result;
    }

}
