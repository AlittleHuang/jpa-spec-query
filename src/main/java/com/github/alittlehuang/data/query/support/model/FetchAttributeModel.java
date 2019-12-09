package com.github.alittlehuang.data.query.support.model;

import com.github.alittlehuang.data.query.specification.AttributePath;
import com.github.alittlehuang.data.query.specification.FetchAttribute;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.criteria.JoinType;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class FetchAttributeModel<T> extends AttributePathModel<T> implements FetchAttribute<T>, Serializable {
    protected JoinType joinType;

    public FetchAttributeModel() {
    }

    public FetchAttributeModel(AttributePath<T> expression) {
        super(expression);
    }

    public FetchAttributeModel(String[] names) {
        super(names);
    }

    public static <T> FetchAttributeModel<T> convertFetch(FetchAttribute<T> fetch) {
        FetchAttributeModel<T> result = new FetchAttributeModel<>(fetch);
        result.setJoinType(fetch.getJoinType());
        return result;
    }

    public static <T> List<FetchAttributeModel<T>> convertFetch(List<? extends FetchAttribute<T>> fetch) {
        return fetch.stream().map(FetchAttributeModel::convertFetch).collect(Collectors.toList());
    }
}
