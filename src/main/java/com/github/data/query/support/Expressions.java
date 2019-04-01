package com.github.data.query.support;

import com.github.data.query.specification.AttrExpression;
import com.github.data.query.specification.Selection;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@FunctionalInterface
public interface Expressions<T, R> extends Selection<T>, AttrExpression<T> {

    @SuppressWarnings( "UnusedReturnValue" )
    R apply(T t);

    static <T, R> Expressions<T, R> of(Expressions<T, R> getters) {
        return getters;
    }

    default <V, U extends Expressions<? super R, ? extends V>> Expressions<T, V> to(U expression) {
        Expressions<T, R> previous = this;
        return new Expressions<T, V>() {
            List<Expressions<?, ?>> list = new ArrayList<>();

            {
                list.add(previous);
                list.add(expression);
            }

            @Override
            public V apply(T t) {
                return null;
            }

            @Override
            public List<Expressions<?, ?>> list() {
                return list;
            }

            @Override
            public <X, Y extends Expressions<? super V, ? extends X>> Expressions<T, X> to(Y expression) {
                list.add(expression);
                //noinspection unchecked
                return (Expressions<T, X>) this;
            }

        };
    }

    default List<Expressions<?, ?>> list() {
        return Collections.singletonList(this);
    }

    default String[] getNames(Class<? extends T> type) {
        return MethodInfoUtil.getAttrNames(type, this);
    }

    default String[] getNames() {
        return getNames(null);
    }

    static <T, R extends Number> Expressions<T, R> abs(Expressions<T, R> expression) {
        return new SimpleExpression<>(expression, Function.ABS);
    }

    static <T, R extends Number> Expressions<T, R> sum(Expressions<T, R> expression, Expressions<T, R> other) {
        return new SimpleExpression<>(expression, Function.SUM, other);
    }

    static <T, R extends Number> Expressions<T, R> sum(Expressions<T, R> expression, R other) {
        return new SimpleExpression<>(expression, Function.SUM, other);
    }

    static <T, R extends Number> Expressions<T, R> prod(Expressions<T, R> expression, Expressions<T, R> other) {
        return new SimpleExpression<>(expression, Function.PROD, other);
    }

    static <T, R extends Number> Expressions<T, R> prod(Expressions<T, R> expression, R other) {
        return new SimpleExpression<>(expression, Function.PROD, other);
    }

    static <T, R extends Number> Expressions<T, R> diff(Expressions<T, R> expression, Expressions<T, R> other) {
        return new SimpleExpression<>(expression, Function.DIFF, other);
    }

    static <T, R extends Number> Expressions<T, R> diff(Expressions<T, R> expression, R other) {
        return new SimpleExpression<>(expression, Function.DIFF, other);
    }

    static <T, R extends Number> Expressions<T, R> quot(Expressions<T, R> expression, R other) {
        return new SimpleExpression<>(expression, Function.QUOT, other);
    }

    static <T, R extends Number> Expressions<T, R> quot(Expressions<T, R> expression, Expressions<T, R> other) {
        return new SimpleExpression<>(expression, Function.QUOT, other);
    }

    static <T> Expressions<T, Integer> mod(Expressions<T, Integer> expression, Expressions<T, Integer> other) {
        return new SimpleExpression<>(expression, Function.MOD, other);
    }

    static <T> Expressions<T, Integer> mod(Expressions<T, Integer> expression, Integer other) {
        return new SimpleExpression<>(expression, Function.MOD, other);
    }

    static <T, R extends Number> Expressions<T, R> sqrt(Expressions<T, R> expression) {
        return new SimpleExpression<>(expression, Function.SQRT);
    }

    static <T> Expressions<T, String> concat(Expressions<T, String> expression, Expressions<T, String> other) {
        return new SimpleExpression<>(expression, Function.CONCAT, other);
    }

    static <T> Expressions<T, String> concat(Expressions<T, String> expression, String other) {
        return new SimpleExpression<>(expression, Function.CONCAT, other);
    }

    /**
     * Create an expression for substring extraction.
     * Extracts a substring of given length starting at the
     * specified position.
     * First position is 1.
     */
    static <T> Expressions<T, String> substring(Expressions<T, String> expression, int from, int length) {
        return new SimpleExpression<>(expression, Function.SUBSTRING, from, length);
    }


    /**
     * Create an expression for substring extraction.
     * Extracts a substring of given length starting at the
     * specified position.
     * First position is 1.
     */
    static <T> Expressions<T, String> substring(Expressions<T, String> expression, int from) {
        return new SimpleExpression<>(expression, Function.SUBSTRING, from);
    }

    /**
     * Create expression to trim blanks from both ends of
     * a string.
     */
    static <T> Expressions<T, String> trim(Expressions<T, String> expression) {
        return new SimpleExpression<>(expression, Function.TRIM, CriteriaBuilder.Trimspec.BOTH);
    }

    /**
     * Trim from leading end
     */
    static <T> Expressions<T, String> trimLeading(Expressions<T, String> expression) {
        return new SimpleExpression<>(expression, Function.TRIM, CriteriaBuilder.Trimspec.LEADING);
    }

    /**
     * Trim from trailing end.
     */
    static <T> Expressions<T, String> trimTrailing(Expressions<T, String> expression) {
        return new SimpleExpression<>(expression, Function.TRIM, CriteriaBuilder.Trimspec.TRAILING);
    }


    /**
     * Create expression to trim blanks from both ends of
     * a string.
     */
    static <T> Expressions<T, String> trim(Expressions<T, String> expression, char beTrimmed) {
        return new SimpleExpression<>(expression, Function.TRIM, CriteriaBuilder.Trimspec.BOTH, beTrimmed);
    }

    /**
     * Trim from leading end
     */
    static <T> Expressions<T, String> trimLeading(Expressions<T, String> expression, char beTrimmed) {
        return new SimpleExpression<>(expression, Function.TRIM, CriteriaBuilder.Trimspec.LEADING, beTrimmed);
    }

    /**
     * Trim from trailing end.
     */
    static <T> Expressions<T, String> trimTrailing(Expressions<T, String> expression, char beTrimmed) {
        return new SimpleExpression<>(expression, Function.TRIM, CriteriaBuilder.Trimspec.TRAILING, beTrimmed);
    }

    static <T> Expressions<T, String> lower(Expressions<T, String> expression) {
        return new SimpleExpression<>(expression, Function.LOWER);
    }

    static <T> Expressions<T, String> upper(Expressions<T, String> expression) {
        return new SimpleExpression<>(expression, Function.UPPER);
    }

    static <T> Expressions<T, String> length(Expressions<T, String> expression) {
        return new SimpleExpression<>(expression, Function.LENGTH);
    }

    /**
     * Create expression to locate the position of one string
     * within another, returning position of first character
     * if found.
     * The first position in a string is denoted by 1.  If the
     * string to be located is not found, 0 is returned.
     */
    static <T> Expressions<T, String> locate(Expressions<T, String> expression, String pattern) {
        return new SimpleExpression<>(expression, Function.LOCATE, pattern);
    }

    /**
     * Create an expression that returns null if all its arguments evaluate to null,
     * and the value of the first non-null argument otherwise.
     */
    static <X, Y> Expressions<X, Y> coalesce(Expressions<X, Y> expression, Y y) {
        return new SimpleExpression<>(expression, Function.COALESCE, y);
    }

    /**
     * Create an expression that returns null if all its arguments evaluate to null,
     * and the value of the first non-null argument otherwise.
     */
    static <X, Y> Expressions<X, Y> coalesce(Expressions<X, Y> expression, Expressions<X, Y> y) {
        return new SimpleExpression<>(expression, Function.COALESCE, y);
    }

    /**
     * Create an expression that tests whether its argument are
     * equal, returning null if they are and the value of the
     * first expression if they are not.
     */
    static <T, Y> Expressions<T, Y> nullif(Expressions<T, Y> expression, Y val) {
        return new SimpleExpression<>(expression, Function.NULLIF, val);
    }

    /**
     * Create an expression that tests whether its argument are
     * equal, returning null if they are and the value of the
     * first expression if they are not.
     */
    static <T, Y> Expressions<T, Y> nullif(Expressions<T, Y> expression, Expressions<T, Y> val) {
        return new SimpleExpression<>(expression, Function.NULLIF, val);
    }

}
