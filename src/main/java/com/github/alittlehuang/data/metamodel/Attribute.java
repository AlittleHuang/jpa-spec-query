package com.github.alittlehuang.data.metamodel;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;

/**
 * @author ALittleHuang
 */
public class Attribute<X, Y> {

    public static final String FIX = "`";

    private final Field field;
    private final Method getter;
    private final Method setter;
    private final Class<X> entityType;
    private final ManyToOne manyToOne;
    private final OneToMany oneToMany;
    private final JoinColumn joinColumn;
    private final Version version;
    private final Column column;
    private final ManyToMany manyToMany;
    private final OneToOne oneToOne;
    private final String columnName;
    private final Class<Y> javaType;
    private final boolean collection;

    Attribute(Field field, Method getter, Method setter, Class<X> entityType) {
        this.field = field;
        this.getter = getter;
        this.setter = setter;
        this.entityType = entityType;
        this.manyToOne = getAnnotation(ManyToOne.class);
        this.oneToMany = getAnnotation(OneToMany.class);
        this.joinColumn = getAnnotation(JoinColumn.class);
        this.version = getAnnotation(Version.class);
        this.column = getAnnotation(Column.class);
        this.manyToMany = getAnnotation(ManyToMany.class);
        this.oneToOne = getAnnotation(OneToOne.class);
        this.columnName = initColumnName();
        this.collection = Iterable.class.isAssignableFrom(field.getType());
        this.javaType = initJavaType();
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        if (field != null) {
            T annotation = field.getAnnotation(annotationClass);
            if (annotation != null) {
                return annotation;
            }
        }
        if (getter != null) {
            return getter.getAnnotation(annotationClass);
        }
        return null;
    }

    public void setValue(Object entity, Object value) {
        boolean accessible = field.isAccessible();
        try {
            if (setter != null) {
                setter.invoke(entity, value);
            } else {
                if (!accessible) {
                    field.setAccessible(true);
                }
                field.set(entity, value);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        } finally {
            field.setAccessible(accessible);
        }
    }

    public Object getValue(Object entity) {
        boolean accessible = field.isAccessible();
        try {
            if (getter != null) {
                return getter.invoke(entity);
            } else {
                if (!accessible) {
                    field.setAccessible(true);
                }
                return field.get(entity);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            field.setAccessible(accessible);
        }
        throw new RuntimeException();
    }

    public String initColumnName() {
        Column column = getAnnotation(Column.class);
        String columnName;
        if (column != null && column.name().length() != 0) {
            columnName = column.name();
        } else {
            columnName = field.getName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
        }
        if ( columnName.startsWith(FIX) && columnName.endsWith(FIX) ) {
            columnName = columnName.substring(1, columnName.length() - 1);
        }
        return columnName;
    }

    public String getFieldName() {
        return field.getName();
    }

    public Class<Y> initJavaType() {
        Class javaType = null;
        Class fieldType = field.getType();
        if ( collection ) {
            Type genericType = field.getGenericType();
            if ( genericType instanceof ParameterizedType ) {
                ParameterizedType parameterizedType = (ParameterizedType) genericType;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if ( actualTypeArguments.length == 1 ) {
                    Type actualTypeArgument = actualTypeArguments[0];
                    if ( actualTypeArgument instanceof Class ) {
                        javaType = (Class) actualTypeArgument;
                    }
                }
            }
        } else {
            javaType = fieldType;
        }
        if ( javaType == null ) {
            throw new RuntimeException("field " + field + " unspecified type in " + entityType);
        }
        //noinspection unchecked
        return javaType;
    }

    public Class<?> getEntityType() {
        return entityType;
    }

    public Field getField() {
        return field;
    }

    public ManyToOne getManyToOne() {
        return manyToOne;
    }

    public OneToMany getOneToMany() {
        return oneToMany;
    }

    public Column getColumn() {
        return column;
    }

    public Version getVersion() {
        return version;
    }

    public JoinColumn getJoinColumn() {
        return joinColumn;
    }

    public ManyToMany getManyToMany() {
        return manyToMany;
    }

    public OneToOne getOneToOne() {
        return oneToOne;
    }

    public boolean isEntityType() {
        return getJavaType().getAnnotation(Entity.class) != null;
    }

    public String getColumnName() {
        return columnName;
    }

    public boolean isCollection() {
        return collection;
    }

    public Class<Y> getJavaType() {
        return javaType;
    }

    public Method getSetter() {
        return setter;
    }

    public Method getGetter() {
        return getter;
    }

}
