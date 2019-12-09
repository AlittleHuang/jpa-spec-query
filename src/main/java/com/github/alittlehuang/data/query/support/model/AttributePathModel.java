package com.github.alittlehuang.data.query.support.model;

import com.github.alittlehuang.data.query.specification.AttributePath;
import lombok.Data;

import java.io.Serializable;

@Data
public class AttributePathModel<T> implements AttributePath<T>, Serializable {

    protected String[] names;

    public AttributePathModel() {
    }

    public AttributePathModel(String[] names) {
        this.names = names;
    }

    public AttributePathModel(AttributePath<T> expression) {
        names = expression.getNames();
    }

    @Override
    public String[] getNames() {
        return names;
    }

}
