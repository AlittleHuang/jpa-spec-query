package com.github.alittlehuang.data.query.support.model;

import com.github.alittlehuang.data.query.specification.AttributePath;
import com.github.alittlehuang.data.query.specification.FetchAttribute;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.criteria.JoinType;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class FetchAttributeModel<T> extends AttributePathModel<T> implements FetchAttribute<T>, Serializable {
    protected JoinType joinType;

    public FetchAttributeModel() {
    }

    public FetchAttributeModel(AttributePath<T> expression, Class<? extends T> javaType) {
        super(expression, javaType);
    }
}
