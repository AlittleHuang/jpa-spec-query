package com.github.data.query.specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@FunctionalInterface
public interface Path<T, R> {

    @SuppressWarnings("UnusedReturnValue")
    R apply(T t);

    static <T, R> Path<T, R> of(Path<T, R> getters){
        return getters;
    }

    default <V, U extends Path<? super R, ? extends V>> Path<T, V> to(U after) {
        Path<T, R> previous = this;//上一个
        return new Path<T, V>() {
            List<Path> list = new ArrayList<>();

            {
                list.add(previous);
                list.add(after);
            }

            @Override
            public V apply(T t) {
                return null;
            }

            @Override
            public List<Path> list() {
                return list;
            }

            @Override
            public <X, Y extends Path<? super V, ? extends X>> Path<T, X> to(Y after) {
                list.add(after);
                //noinspection unchecked
                return (Path<T, X>) this;
            }

        };
    }

    default List<Path> list() {
        return Collections.singletonList(this);
    }

}
