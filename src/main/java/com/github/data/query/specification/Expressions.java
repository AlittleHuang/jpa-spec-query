package com.github.data.query.specification;

public interface Expressions<T> extends Attribute<T> {

    default Object[] getArgs(){
        return null;
    }

    default Type getType(){
        return Type.NONE;
    }

    enum Type {//表达式
        NONE,
        ABS,
        SUM,//和
        PROD,//积
        DIFF,//差
        QUOT,//商
        MOD,//模
        SQRT,//开方

        CONCAT,
        SUBSTRING,
        TRIM,
        LOWER,
        UPPER,
        LENGTH,
        /**
         * Create expression to locate the position of one string
         * within another, returning position of first character
         * if found.
         * The first position in a string is denoted by 1.  If the
         * string to be located is not found, 0 is returned.
         */
        LOCATE,
        /**
         * Create an expression that returns null if all its arguments evaluate to null,
         * and the value of the first non-null argument otherwise.
         */
        COALESCE,
        /**
         * Create an expression that tests whether its argument are
         * equal, returning null if they are and the value of the
         * first expression if they are not.
         */
        NULLIF
    }

}
