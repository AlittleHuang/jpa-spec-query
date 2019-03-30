package com.github.data.query.support;

import com.github.data.query.specification.Attribute;
import com.github.data.query.specification.Expressions;
import com.github.data.query.specification.Getter;

public class ExpressionBuilder {

    private ExpressionBuilder() {
    }


    public static <T, R extends Number> Getter<T, R> abs(Getter<T, R> getter) {
        return new Exp<>(getter, Expressions.Type.ABS);
    }

    public static <T, R extends Number> Getter<T, R> sum(Getter<T, R> getter, Getter<T, R> other) {
        return new Exp<>(getter, Expressions.Type.SUM, other);
    }

    public static <T, R extends Number> Getter<T, R> prod(Getter<T, R> getter, Getter<T, R> other) {
        return new Exp<>(getter, Expressions.Type.PROD, other);
    }

    public static <T, R extends Number> Getter<T, R> diff(Getter<T, R> getter, Getter<T, R> other) {
        return new Exp<>(getter, Expressions.Type.DIFF, other);
    }

    // QUOT,//商
    public static <T, R extends Number> Getter<T, R> quot(Getter<T, R> getter, Getter<T, R> other) {
        return new Exp<>(getter, Expressions.Type.QUOT, other);
    }

    // MOD,//模
    public static <T, R extends Number> Getter<T, R> mod(Getter<T, R> getter, Getter<T, R> other) {
        return new Exp<>(getter, Expressions.Type.MOD, other);
    }

    // SQRT,//开方
    public static <T, R extends Number> Getter<T, R> sqrt(Getter<T, R> getter) {
        return new Exp<>(getter, Expressions.Type.SQRT);
    }

    // CONCAT,
    public static <T> Getter<T, String> concat(Getter<T, String> getter, Getter<T, String> other) {
        return new Exp<>(getter, Expressions.Type.CONCAT, other);
    }

    // SUBSTRING,
    public static <T> Getter<T, String> substring(Getter<T, String> getter, int from, int length) {
        return new Exp<>(getter, Expressions.Type.SUBSTRING, from, length);
    }

    // TRIM,
    public static <T> Getter<T, String> trim(Getter<T, String> getter) {
        return new Exp<>(getter, Expressions.Type.TRIM);
    }

    // LOWER,
    public static <T> Getter<T, String> lower(Getter<T, String> getter) {
        return new Exp<>(getter, Expressions.Type.LOWER);
    }

    // UPPER,
    public static <T> Getter<T, String> upper(Getter<T, String> getter) {
        return new Exp<>(getter, Expressions.Type.UPPER);
    }

    // LENGTH,
    public static <T> Getter<T, String> length(Getter<T, String> getter) {
        return new Exp<>(getter, Expressions.Type.LENGTH);
    }

    // LOCATE,
    public static <T> Getter<T, String> locate(Getter<T, String> getter, String pattern) {
        return new Exp<>(getter, Expressions.Type.LOCATE, pattern);
    }

    // COALESCE,
    public static <X, Y> Getter<X, Y> coalesce(Getter<X, Y> getter, Y y) {
        return new Exp<>(getter, Expressions.Type.COALESCE, y);
    }

    public static <X, Y> Getter<X, Y> coalesce(Getter<X, Y> getter, Getter<X, Y> y) {
        return new Exp<>(getter, Expressions.Type.COALESCE, y);
    }

    // NULLIF
    public static <T, Y> Getter<T, Y> nullif(Getter<T, Y> getter, Y val) {
        return new Exp<>(getter, Expressions.Type.NULLIF, val);
    }

    // NULLIF
    public static <T, Y> Getter<T, Y> nullif(Getter<T, Y> getter, Getter<T, Y> val) {
        return new Exp<>(getter, Expressions.Type.NULLIF, val);
    }

    private static class Exp<T, R> extends SimpleExpressions<T> implements Getter<T, R> {

        public Exp(Attribute<T> attribute, Type type, Object... args) {
            super(attribute, type, args);
        }

        @Override
        public R apply(T t) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <V, U extends Getter<? super R, ? extends V>> Getter<T, V> to(U after) {
            throw new UnsupportedOperationException();
        }
    }
}
