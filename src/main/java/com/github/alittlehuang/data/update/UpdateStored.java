package com.github.alittlehuang.data.update;

public interface UpdateStored<T> {

    /**
     * update by id
     *
     * @param entity entity instance
     * @return
     */
    int update(T entity);

    /**
     * insert to databases
     *
     * @param entity entity instance
     * @return the instance inserted
     */
    T insert(T entity);

    int[] update(Iterable<T> entities);

    <X extends Iterable<? extends T>> X insert(X entities);

}
