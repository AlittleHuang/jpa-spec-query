package com.github.alittlehuang.data.jdbc.sql;

import com.github.alittlehuang.data.metamodel.Attribute;

/**
 * @author ALittleHuang
 */
public class SelectedAttribute {

    private Attribute attribute;
    private SelectedAttribute parent;

    public SelectedAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public SelectedAttribute(Attribute attribute, SelectedAttribute parent) {
        this.attribute = attribute;
        this.parent = parent;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public SelectedAttribute getParent() {
        return parent;
    }

    public Attribute getParentAttribute() {
        return parent == null ? null : parent.getAttribute();
    }
}
