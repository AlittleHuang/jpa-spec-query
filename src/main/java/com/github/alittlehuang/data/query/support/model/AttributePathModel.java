package com.github.alittlehuang.data.query.support.model;

import com.github.alittlehuang.data.query.specification.AttributePath;
import lombok.Data;

import java.io.Serializable;

@Data
public class AttributePathModel<T> implements AttributePath<T>, Serializable {

    protected String[] names;

    public AttributePathModel() {
    }

    public AttributePathModel(AttributePath<T> expression, Class<? extends T> javaType) {
        names = expression.getNames(javaType);
    }

    @Override
    public String[] getNames(Class<? extends T> type) {
        return names;
    }

}
