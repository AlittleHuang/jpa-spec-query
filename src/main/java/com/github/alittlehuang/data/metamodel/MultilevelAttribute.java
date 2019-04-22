package com.github.alittlehuang.data.metamodel;

import com.github.alittlehuang.data.query.specification.Expressions;
import lombok.Getter;

/**
 * @author ALittleHuang
 */
public class MultilevelAttribute<T, R> implements Expressions<T, R> {

    @Getter
    private final MultilevelAttribute<T, ?> parent;
    @Getter
    private final int depth;
    @Getter
    private final Attribute<?, R> attribute;

    public static <A, B, C> MultilevelAttribute<A, C> getInstance(MultilevelAttribute<A, B> parent, Attribute<B, C> attribute) {
        return new MultilevelAttribute<>(parent, attribute);
    }

    public static <A, B> MultilevelAttribute<A, B> getInstance(Attribute<A, B> attribute) {
        return new MultilevelAttribute<>(attribute);
    }

    private MultilevelAttribute(MultilevelAttribute<T, ?> parent, Attribute<?, R> attribute) {
        this.parent = parent;
        this.attribute = attribute;
        this.depth = parent.depth + 1;
    }

    private MultilevelAttribute(Attribute<T, R> attribute) {
        this.parent = null;
        this.attribute = attribute;
        this.depth = 0;
    }

    public <S> MultilevelAttribute<T, S> get(Attribute<R, S> attribute) {
        return new MultilevelAttribute<>(this, attribute);
    }

    @Override
    public R apply(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getNames() {
        String[] names = new String[depth + 1];
        MultilevelAttribute<T, ?> cursor = this;
        for ( int i = names.length - 1; i >= 0; i-- ) {
            assert cursor != null;
            names[i] = cursor.attribute.getFieldName();
            cursor = cursor.parent;
        }
        return names;
    }
}
