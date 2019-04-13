package com.github.alittlehuang.data.metamodel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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

public class EntityInformation<T, ID> {

    private static final Map<Class<?>, EntityInformation<?, ?>> MAP = new ConcurrentHashMap<>();

    private Class<T> javaType;
    private final Attribute idAttribute;
    private final List<Attribute> allAttributes;
    private final List<Attribute> basicAttributes;
    private final List<Attribute> manyToOneAttributes;
    private final List<Attribute> oneToManyAttributes;
    private final List<Attribute> manyToManyAttributes;
    private final List<Attribute> oneToOneAttributes;
    private final Map<String, Attribute> nameMap;
    private final String tableName;

    private EntityInformation(Class<T> javaType) {
        this.allAttributes = initAttributes(javaType);
        this.idAttribute = initIdAttribute();
        this.tableName = initTableName();
        List<Attribute> basicAttributes = new ArrayList<>();
        List<Attribute> manyToOneAttributes = new ArrayList<>();
        List<Attribute> oneToManyAttributes = new ArrayList<>();
        List<Attribute> manyToManyAttributes = new ArrayList<>();
        List<Attribute> oneToOneAttributes = new ArrayList<>();
        for ( Attribute attribute : this.allAttributes ) {
            Entity entity = attribute.getFieldType().getAnnotation(Entity.class);
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
        this.manyToOneAttributes = Collections.unmodifiableList(manyToOneAttributes);
        this.oneToManyAttributes = Collections.unmodifiableList(oneToManyAttributes);
        this.manyToManyAttributes = Collections.unmodifiableList(manyToManyAttributes);
        this.oneToOneAttributes = Collections.unmodifiableList(oneToOneAttributes);

        nameMap = allAttributes.stream().collect(Collectors.toMap(Attribute::getFieldName, Function.identity()));
    }

    public static <X, Y> EntityInformation<X, Y> getInstance(Class<X> clazz) {
        Objects.requireNonNull(clazz.getAnnotation(Entity.class), "the class must be a entity");
        //noinspection unchecked
        return (EntityInformation<X, Y>) MAP.computeIfAbsent(clazz, EntityInformation::new);
    }

    public String getTableName() {
        return tableName;
    }

    public List<? extends Attribute> getAllAttributes() {
        return allAttributes;
    }

    public Class<T> getJavaType() {
        return javaType;
    }

    public Attribute getIdAttribute() {
        return idAttribute;
    }

    private List<Attribute> initAttributes(Class<T> javaType) {
        List<Attribute> attributes = new ArrayList<>();
        this.javaType = javaType;
        Field[] fields = javaType.getDeclaredFields();
        Map<Field, Method> readerMap = new HashMap<>();
        Map<Field, Method> writeMap = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(javaType);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                Field field = getDeclaredField(javaType, descriptor.getName());
                if (field == null) continue;
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
            Attribute attribute = new Attribute(field, readerMap.get(field), writeMap.get(field), javaType);
            if ( attribute.getAnnotation(Transient.class) == null
                    && !Modifier.isStatic(field.getModifiers()) ) {
                attributes.add(attribute);
            }
        }
        return Collections.unmodifiableList(attributes);
    }

    private Attribute initIdAttribute() {
        for ( Attribute attribute : allAttributes ) {
            if (attribute.getAnnotation(Id.class) != null) {
                return attribute;
            }
        }
        return null;
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
        if ( tableName.startsWith("`") && tableName.endsWith("`") ) {
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

    public List<Attribute> getBasicAttributes() {
        return basicAttributes;
    }

    public List<Attribute> getOneToManyAttributes() {
        return oneToManyAttributes;
    }

    public List<Attribute> getOneToOneAttributes() {
        return oneToOneAttributes;
    }

    public List<Attribute> getManyToManyAttributes() {
        return manyToManyAttributes;
    }

    public List<Attribute> getManyToOneAttributes() {
        return manyToOneAttributes;
    }

}
