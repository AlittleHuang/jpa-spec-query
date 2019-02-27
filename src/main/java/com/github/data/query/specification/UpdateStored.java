package com.github.data.query.specification;

public interface UpdateStored<T> {

    <S extends T> S update(S entity);

    void update();

}
