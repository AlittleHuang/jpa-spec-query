package com.github.data.query.specification;

public interface AttrExpression<T> extends Attribute<T> {

    Object[] EMPTY_ARGS = {};

    default Object[] getArgs(){
        return EMPTY_ARGS;
    }

    default AttrExpression<T> getSubexpression() {
        return null;
    }

    default Function getFunction(){
        return Function.NONE;
    }

    enum Function {
        NONE,
        ABS,
        SUM,
        PROD,
        DIFF,
        QUOT,
        MOD,
        SQRT,
        CONCAT,
        SUBSTRING,
        TRIM,
        LOWER,
        UPPER,
        LENGTH,
        LOCATE,
        COALESCE,
        NULLIF
    }

}
