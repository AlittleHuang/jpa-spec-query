package com.github.jpa.spec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface Getters<T, R> {

    R apply(T t);

    static <T, R> Getters<T, R> path(Getters<T, R> getters){
        return getters;
    }

    default <V, U extends Getters<? super R, ? extends V>> Getters<T, V> to(U after) {
        Getters<T, R> previous = this;//上一个
        return new Getters<T, V>() {
            List<Getters> list = new ArrayList<>();

            {
                list.add(previous);
                list.add(after);
            }

            @Override
            public V apply(T t) {
                return null;
            }

            @Override
            public List<Getters> list() {
                return list;
            }

            @Override
            public <X, Y extends Getters<? super V, ? extends X>> Getters<T, X> to(Y after) {
                list.add(after);
                //noinspection unchecked
                return (Getters<T, X>) this;
            }

        };
    }

    default List<Getters> list() {
        return Collections.singletonList(this);
    }

}
