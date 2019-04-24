package com.github.alittlehuang.data.metamodel;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author ALittleHuang
 */
public interface EntityInformation<T, ID> {

    Attribute getAttribute(String name);

    Attribute getAttributeByGetter(Method method);

    Attribute getAttributeByColumnName(String name);

    Class<T> getJavaType();

    Attribute<T, ID> getIdAttribute();

    Attribute<T, ? extends Number> getVersionAttribute();

    boolean hasVersion();

    List<Attribute<T, ?>> getAllAttributes();

    List<Attribute<T, ?>> getBasicAttributes();

    List<Attribute<T, ?>> getBasicUpdatableAttributes();

    List<Attribute<T, ?>> getBasicInsertableAttributes();

    List<Attribute<T, ?>> getManyToOneAttributes();

    List<Attribute<T, ?>> getOneToManyAttributes();

    List<Attribute<T, ?>> getManyToManyAttributes();

    List<Attribute<T, ?>> getOneToOneAttributes();

    String getTableName();
}
