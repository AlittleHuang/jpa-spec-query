package com.github.alittlehuang.data.metamodel;

import com.github.alittlehuang.data.util.Assert;
import lombok.Getter;

import javax.persistence.*;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ALittleHuang
 */
public class EntityInformation<T, ID> {

    private static final Map<Class<?>, EntityInformation<?, ?>> MAP = new ConcurrentHashMap<>();
    private static final String FIX = "`";

    @Getter
    private final Class<T> javaType;
    @Getter
    private final Attribute<T, ID> idAttribute;
    @Getter
    private final Attribute<T, ? extends Number> versionAttribute;
    @Getter
    private final List<Attribute<T, ?>> allAttributes;
    @Getter
    private final List<Attribute<T, ?>> basicAttributes;
    @Getter
    private final List<Attribute<T, ?>> basicUpdatableAttributes;
    @Getter
    private final List<Attribute<T, ?>> basicInsertableAttributes;
    @Getter
    private final List<Attribute<T, ?>> manyToOneAttributes;
    @Getter
    private final List<Attribute<T, ?>> oneToManyAttributes;
    @Getter
    private final List<Attribute<T, ?>> manyToManyAttributes;
    @Getter
    private final List<Attribute<T, ?>> oneToOneAttributes;
    /**
     * k->field name, v->attribute
     */
    private final Map<String, Attribute> nameMap;
    /**
     * k->column name, v->attribute
     */
    private final Map<String, Attribute> columnNameMap;

    private final Map<Method, Attribute> getterMap;

    @Getter
    private final String tableName;

    private EntityInformation(Class<T> javaType) {
        this.javaType = javaType;
        this.allAttributes = initAttributes(javaType);
        this.idAttribute = initIdAttribute();
        this.versionAttribute = initVersionAttribute();
        this.tableName = initTableName();
        List<Attribute<T, ?>> basicAttributes = new ArrayList<>();
        List<Attribute<T, ?>> manyToOneAttributes = new ArrayList<>();
        List<Attribute<T, ?>> oneToManyAttributes = new ArrayList<>();
        List<Attribute<T, ?>> manyToManyAttributes = new ArrayList<>();
        List<Attribute<T, ?>> oneToOneAttributes = new ArrayList<>();
        for ( Attribute<T, ?> attribute : this.allAttributes ) {
            Entity entity = attribute.getJavaType().getAnnotation(Entity.class);
            if ( entity == null ) {
                basicAttributes.add(attribute);
            } else if ( attribute.getManyToOne() != null ) {
                manyToOneAttributes.add(attribute);
            } else if ( attribute.getOneToMany() != null ) {
                oneToManyAttributes.add(attribute);
            } else if ( attribute.getManyToMany() != null ) {
                manyToManyAttributes.add(attribute);
            } else if ( attribute.getOneToOne() != null ) {
                oneToOneAttributes.add(attribute);
            }
        }

        this.basicAttributes = Collections.unmodifiableList(basicAttributes);
        this.basicUpdatableAttributes = Collections.unmodifiableList(
                this.basicAttributes.stream().filter(it -> it.getColumn() == null || it.getColumn().updatable()).collect(Collectors.toList())
        );
        this.basicInsertableAttributes = Collections.unmodifiableList(
                this.basicAttributes.stream().filter(it -> it.getColumn() == null || it.getColumn().insertable()).collect(Collectors.toList())
        );
        this.manyToOneAttributes = Collections.unmodifiableList(manyToOneAttributes);
        this.oneToManyAttributes = Collections.unmodifiableList(oneToManyAttributes);
        this.manyToManyAttributes = Collections.unmodifiableList(manyToManyAttributes);
        this.oneToOneAttributes = Collections.unmodifiableList(oneToOneAttributes);

        nameMap = Collections.unmodifiableMap(
                allAttributes.stream().collect(Collectors.toMap(Attribute::getFieldName, Function.identity())));
        columnNameMap = Collections.unmodifiableMap(
                allAttributes.stream().collect(Collectors.toMap(Attribute::getColumnName, Function.identity())));
        getterMap = Collections.unmodifiableMap(allAttributes
                .stream().filter(it -> it.getGetter() != null)
                .collect(Collectors.toMap(Attribute::getGetter, Function.identity()))
        );
    }

    public static <X, Y> EntityInformation<X, Y> getInstance(Class<X> clazz) {
        Objects.requireNonNull(clazz.getAnnotation(Entity.class), "the class must be an entity");
        //noinspection unchecked
        return (EntityInformation<X, Y>) MAP.computeIfAbsent(clazz, EntityInformation::new);
    }

    private List<Attribute<T, ?>> initAttributes(Class<T> javaType) {
        List<Attribute<T,?>> attributes = new ArrayList<>();
        Field[] fields = javaType.getDeclaredFields();
        Map<Field, Method> readerMap = new HashMap<>();
        Map<Field, Method> writeMap = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(javaType);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                Field field = getDeclaredField(javaType, descriptor.getName());
                if (field == null) {
                    continue;
                }
                readerMap.put(field, descriptor.getReadMethod());
                writeMap.put(field, descriptor.getWriteMethod());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (Field field : fields) {
            if (!readerMap.containsKey(field)) {
                readerMap.put(field, null);
            }
            if (!writeMap.containsKey(field)) {
                writeMap.put(field, null);
            }
        }

        for (Field field : writeMap.keySet()) {
            Attribute<T,?> attribute = new Attribute<>(field, readerMap.get(field), writeMap.get(field), javaType);
            if ( attribute.getAnnotation(Transient.class) == null
                    && !Modifier.isStatic(field.getModifiers()) ) {
                attributes.add(attribute);
            }
        }
        return Collections.unmodifiableList(attributes);
    }

    private Attribute<T, ID> initIdAttribute() {
        for ( Attribute<T, ?> attribute : allAttributes ) {
            if (attribute.getAnnotation(Id.class) != null) {
                //noinspection unchecked
                return (Attribute<T, ID>) attribute;
            }
        }
        throw new RuntimeException("entity " + javaType + " has no id attribute");
    }

    private Attribute<T, ? extends Number> initVersionAttribute() {
        for (Attribute<T, ?> attribute : allAttributes) {
            if (attribute.getAnnotation(Version.class) != null) {
                Class<?> type = attribute.getJavaType();
                Assert.state(type == Integer.class || type == Long.class,
                        "version attribute type must be integer or long");
                //noinspection unchecked
                return (Attribute<T, ? extends Number>) attribute;
            }
        }
        throw new RuntimeException("entity " + javaType + " has no id attribute");
    }

    private String initTableName() {
        Entity entity = javaType.getAnnotation(Entity.class);
        if (entity != null && entity.name().length() > 0) {
            return entity.name();
        }
        Table table = javaType.getAnnotation(Table.class);
        if (table != null && table.name().length() > 0) {
            return table.name();
        }
        String tableName = javaType.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
        if ( tableName.startsWith(FIX) && tableName.endsWith(FIX) ) {
            tableName = tableName.substring(1, tableName.length() - 1);
        }
        return tableName;
    }

    private static Field getDeclaredField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                return getDeclaredField(superclass, name);
            }
        }
        return null;
    }

    public Attribute getAttribute(String name) {
        return nameMap.get(name);
    }

    public Attribute getAttributeByGetter(Method method) {
        return getterMap.get(method);
    }

    public Attribute getAttributeByColumnName(String name) {
        return columnNameMap.get(name);
    }
}
