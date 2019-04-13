package com.github.alittlehuang.data.query.specification;

/**
 * @author ALittleHuang
 */
public interface Expression<T> extends AttributePath {

    Object[] EMPTY_ARGS = {};

    default Object[] getArgs(){
        return EMPTY_ARGS;
    }

    default Expression<T> getSubexpression() {
        return null;
    }

    default Function getFunction(){
        return Function.NONE;
    }

    default String getFunctionName(){
        return null;
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
        NULLIF,
        CUSTOMIZE,
    }

}
