package com.github.data.query.support;

import com.github.data.query.specification.ConditionalOperator;
import com.github.data.query.specification.Path;
import com.github.data.query.specification.WhereClauseBuilder;

import javax.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.Collection;

import static com.github.data.query.specification.ConditionalOperator.*;
import static javax.persistence.criteria.Predicate.BooleanOperator.AND;
import static javax.persistence.criteria.Predicate.BooleanOperator.OR;

public abstract class AbstractWhereClauseBuilder<T, THIS extends WhereClauseBuilder<T, THIS>>
        extends WhereClauseItem
        implements WhereClauseBuilder<T, THIS> {

    private static final boolean NOT = true;
    protected final WhereClauseItem root;

    public AbstractWhereClauseBuilder(SimpleFieldPath path, WhereClauseItem root) {
        super(path);
        this.root = root;
    }

    protected abstract THIS createSub(SimpleFieldPath<T> paths);

    protected THIS self(){
        //noinspection unchecked
        return (THIS) this;
    }

    private AbstractWhereClauseBuilder<T, THIS> sub(SimpleFieldPath<T> paths){
        //noinspection unchecked
        return (AbstractWhereClauseBuilder) createSub(paths);
    }

    public AbstractWhereClauseBuilder() {
        super(null);
        this.root = this;
    }

    private THIS add(SimpleFieldPath<T> paths,
                     Object value,
                     Predicate.BooleanOperator operator,
                     boolean negate,
                     ConditionalOperator conditionalOperator) {
        AbstractWhereClauseBuilder<T, THIS> item = sub(paths);
        item.value = value;
        item.booleanOperator = operator;
        item.conditionalOperator = conditionalOperator;
        item.negate = negate;
        assert compoundItems != null;
        compoundItems.add(item);
        return self();
    }

    private THIS add(Path<T, ?> paths,
                     Object value,
                     Predicate.BooleanOperator operator,
                     boolean negate,
                     ConditionalOperator conditionalOperator) {
        SimpleFieldPath<T> path = new SimpleFieldPath<>(paths);
        return add(path, value, operator, negate, conditionalOperator);
    }

    private THIS add(String paths,
                     Object value,
                     Predicate.BooleanOperator operator,
                     boolean negate,
                     ConditionalOperator conditionalOperator) {
        SimpleFieldPath<T> path = new SimpleFieldPath<>(paths);
        return add(path, value, operator, negate, conditionalOperator);
    }

    private THIS add(Path<T, ?> paths,
                     Object value,
                     Predicate.BooleanOperator operator,
                     ConditionalOperator conditionalOperator) {
        return add(paths, value, operator, false, conditionalOperator);
    }

    private THIS add(String paths,
                     Object value,
                     Predicate.BooleanOperator operator,
                     ConditionalOperator conditionalOperator) {
        return add(paths, value, operator, false, conditionalOperator);
    }

    public THIS and() {
        AbstractWhereClauseBuilder<T, THIS> sub = sub(null);
        assert compoundItems != null;
        compoundItems.add(sub);
        return sub.self();
    }

    public THIS or() {
        AbstractWhereClauseBuilder<T, THIS> sub = sub(null);
        sub.booleanOperator = OR;
        assert compoundItems != null;
        compoundItems.add(sub);
        return sub.self();
    }

    @Override
    public THIS andEqual(String name, Object value) {
        return add(name, value, AND, EQUAL);
    }

    @Override
    public THIS orEqual(String name, Object value) {
        return add(name, value, OR, EQUAL);
    }

    @Override
    public THIS andNotEqual(String name, Object value) {
        return add(name, value, AND, NOT, EQUAL);
    }

    @Override
    public THIS orNotEqual(String name, Object value) {
        return add(name, value, OR, NOT, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS andBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), AND, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS orBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), OR, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS andNotBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), AND, NOT, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS orNotBetween(String name, Y value0, Y value1) {
        return add(name, Arrays.asList(value0, value1), OR, NOT, EQUAL);
    }

    @Override
    public THIS andIsNull(String name) {
        return add(name, null, AND, IS_NULL);
    }

    @Override
    public THIS andIsNotNull(String name) {
        return add(name, null, AND, NOT, IS_NULL);
    }

    @Override
    public THIS orIsNotNull(String name) {
        return add(name, null, OR, NOT, IS_NULL);
    }

    @Override
    public THIS orIsNull(String name) {
        return add(name, null, OR, IS_NULL);
    }

    @Override
    public THIS andIsNull(Path<T, ?> name) {
        return add(name, null, AND, IS_NULL);
    }

    @Override
    public THIS andIsNotNull(Path<T, ?> name) {
        return add(name, null, AND, NOT, IS_NULL);
    }

    @Override
    public THIS orIsNotNull(Path<T, ?> name) {
        return add(name, null, OR, NOT, IS_NULL);
    }

    @Override
    public THIS orIsNull(Path<T, ?> name) {
        return add(name, null, OR, IS_NULL);
    }

    @Override
    public THIS andLike(String name, String value) {
        return add(name, value, AND, LIKE);
    }

    @Override
    public THIS andNotLike(String name, String value) {
        return add(name, value, AND, NOT, LIKE);
    }

    @Override
    public THIS orLike(String name, String value) {
        return add(name, value, OR, LIKE);
    }

    @Override
    public THIS orNotLike(String name, String value) {
        return add(name, value, OR, NOT, LIKE);
    }

    @Override
    public <X> THIS andIn(String name, Collection<X> value) {
        return add(name, value, AND, IN);
    }

    @SafeVarargs
    @Override
    public final <X> THIS andIn(String name, X... value) {
        return andIn(name, Arrays.asList(value));
    }

    @Override
    public <X> THIS andNotIn(String name, Collection<X> value) {
        return add(name, value, AND, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <X> THIS andNotIn(String name, X... value) {
        return andNotIn(name, Arrays.asList(value));
    }

    @Override
    public <X> THIS orIn(String name, Collection<X> value) {
        return add(name, value, OR, IN);
    }

    @SafeVarargs
    @Override
    public final <X> THIS orIn(String name, X... value) {
        return orIn(name, Arrays.asList(value));
    }

    @Override
    public <X> THIS orNotIn(String name, Collection<X> value) {
        return add(name, value, AND, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <X> THIS orNotIn(String name, X... value) {
        return orNotIn(name, Arrays.asList(value));
    }

    @Override
    public <U, G extends Path<T, ? super U>> THIS andEqual(G path, G other) {
        return add(path, new SimpleFieldPath<>(other), AND, EQUAL);
    }

    @Override
    public <U, G extends Path<T, ? super U>> THIS orEqual(G path, G other) {
        return add(path, new SimpleFieldPath<>(other), OR, EQUAL);
    }

    @Override
    public <U, G extends Path<T, ? super U>> THIS andNotEqual(G path, G other) {
        return add(path, new SimpleFieldPath<>(other), AND, NOT, EQUAL);
    }

    @Override
    public <U, G extends Path<T, ? super U>> THIS orNotEqual(G path, G other) {
        return add(path, new SimpleFieldPath<>(other), OR, NOT, EQUAL);
    }

    @Override
    public <U, F extends Path<T, ? super U>> THIS andEqual(F getter, U value) {
        return add(getter, value, AND, EQUAL);
    }

    @Override
    public <U, F extends Path<T, ? super U>> THIS orEqual(F getter, U value) {
        return add(getter, value, OR, EQUAL);
    }

    @Override
    public <U, F extends Path<T, ? super U>> THIS andNotEqual(F getter, U value) {
        return add(getter, value, AND, NOT, EQUAL);
    }

    @Override
    public <U, F extends Path<T, ? super U>> THIS orNotEqual(F getter, U value) {
        return add(getter, value, OR, NOT, EQUAL);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS andGe(String name, Y value) {
        return add(name, value, AND, GE);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS andGe(F getter, U value) {
        return add(getter, value, AND, GE);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS orGe(String name, Y value) {
        return add(name, value, OR, GE);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS orGe(F getter, U value) {
        return add(getter, value, OR, GE);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS andLe(String name, Y value) {
        return add(name, value, AND, LT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS andLe(F getter, U value) {
        return add(getter, value, AND, LE);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS orLe(String name, Y value) {
        return add(name, value, OR, LT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS orLe(F getter, U value) {
        return add(getter, value, OR, LE);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS andGt(String name, Y value) {
        return add(name, value, AND, GT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS andGt(F getter, U value) {
        return add(getter, value, AND, GT);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS orGt(String name, Y value) {
        return add(name, value, OR, GT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS orGt(F getter, U value) {
        return add(getter, value, OR, GT);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS andLt(String name, Y value) {
        return add(name, value, AND, LT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS andLt(F getter, U value) {
        return add(getter, value, AND, LT);
    }

    @Override
    public <Y extends Comparable<? super Y>> THIS orLt(String name, Y value) {
        return add(name, value, OR, LT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS orLt(F getter, U value) {
        return add(getter, value, OR, LT);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS andBetween(F getter, U value, U otherValue) {
        return add(getter, Arrays.asList(value, otherValue), AND, BETWEEN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS orBetween(F getter, U value, U otherValue) {
        return add(getter, Arrays.asList(value, otherValue), OR, BETWEEN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS andNotBetween(F getter, U value, U otherValue) {
        return add(getter, Arrays.asList(value, otherValue), AND, NOT, BETWEEN);
    }

    @Override
    public <U extends Comparable<? super U>, F extends Path<T, ? super U>> THIS orNotBetween(F getter, U value, U otherValue) {
        return add(getter, Arrays.asList(value, otherValue), OR, NOT, BETWEEN);
    }

    @Override
    public THIS andIsNull(String name, Boolean isNull) {
        if (isNull)
            return add(name, null, AND, IS_NULL);
        else {
            return add(name, null, AND, NOT, IS_NULL);
        }
    }

    @Override
    public THIS orIsNull(String name, Boolean isNull) {
        if (isNull)
            return add(name, null, OR, IS_NULL);
        else {
            return add(name, null, OR, NOT, IS_NULL);
        }
    }

    @Override
    public THIS andLike(Path<T, String> getters, String value) {
        return add(getters, value, AND, LIKE);
    }

    @Override
    public THIS andNotLike(Path<T, String> getters, String value) {
        return add(getters, value, AND, NOT, LIKE);
    }

    @Override
    public THIS orLike(Path<T, String> getters, String value) {
        return add(getters, value, OR, LIKE);
    }

    @Override
    public THIS orNotLike(Path<T, String> getters, String value) {
        return add(getters, value, OR, NOT, LIKE);
    }

    @Override
    public <U, F extends Path<T, ? super U>> THIS andIn(Path<T, U> getters, Collection<U> value) {
        return add(getters, value, AND, IN);
    }

    @SafeVarargs
    @Override
    public final <U, F extends Path<T, ? super U>> THIS andIn(Path<T, U> getters, U... value) {
        return andIn(getters, Arrays.asList(value));
    }

    @Override
    public <U, F extends Path<T, ? super U>> THIS andNotIn(Path<T, U> getters, Collection<U> value) {
        return add(getters, value, AND, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <U, F extends Path<T, ? super U>> THIS andNotIn(Path<T, U> getters, U... value) {
        return andNotIn(getters, Arrays.asList(value));
    }

    @Override
    public <U, F extends Path<T, ? super U>> THIS orIn(Path<T, U> getters, Collection<U> value) {
        return add(getters, value, OR, IN);
    }

    @SafeVarargs
    @Override
    public final <U, F extends Path<T, ? super U>> THIS orIn(Path<T, U> getters, U... value) {
        return orIn(getters, Arrays.asList(value));
    }

    @Override
    public <U, F extends Path<T, ? super U>> THIS orNotIn(Path<T, U> getters, Collection<U> value) {
        return add(getters, value, OR, NOT, IN);
    }

    @SafeVarargs
    @Override
    public final <U, F extends Path<T, ? super U>> THIS orNotIn(Path<T, U> getters, U... value) {
        return orNotIn(getters, Arrays.asList(value));
    }
}
