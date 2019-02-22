package com.github.jpa.spec;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.criteria.Predicate.BooleanOperator;

@Data
@Accessors(chain = true, fluent = true)
public class ClauseItem<T> {//查询条件

    private String[] paths;
    private T value;
    private BooleanOperator operator = BooleanOperator.AND;
    private ConditionalOperator conditionalOperator;//条件运算符
    private boolean negate = false;//取反

    public ClauseItem path(String... paths) {
        this.paths = paths;
        return this;
    }

    public ClauseItem path(String paths) {
        this.paths = paths.split("\\.");
        return this;
    }

    public static class PathToPath extends ClauseItem<String[]> {
        PathToPath otherPaths(String... otherPaths) {
            value(otherPaths);
            return this;
        }
    }

}
