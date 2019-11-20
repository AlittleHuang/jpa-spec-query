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

    public FetchAttributeModel(AttributePath<T> expression, Class<? extends T> javaType) {
        super(expression, javaType);
    }

    public static <T> FetchAttributeModel<T> convertFetch(FetchAttribute<T> fetch, Class<? extends T> javaType) {
        FetchAttributeModel<T> result = new FetchAttributeModel<>(fetch, javaType);
        result.setJoinType(fetch.getJoinType());
        return result;
    }

    public static <T> List<FetchAttributeModel<T>> convertFetch(List<? extends FetchAttribute<T>> fetch, Class<? extends T> javaType) {
        return fetch.stream().map(i -> convertFetch(i, javaType))
                .collect(Collectors.toList());
    }
}
