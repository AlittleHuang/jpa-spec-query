package com.github.alittlehuang.data.metamodel.support;

import com.github.alittlehuang.data.metamodel.Attribute;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;

/**
 * @author ALittleHuang
 */
public class AttributeImpl<X, Y> implements Attribute<X, Y> {

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

    public AttributeImpl(Field field, Method getter, Method setter, Class<X> entityType) {
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

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        if ( field != null ) {
            T annotation = field.getAnnotation(annotationClass);
            if ( annotation != null ) {
                return annotation;
            }
        }
        if ( getter != null ) {
            return getter.getAnnotation(annotationClass);
        }
        return null;
    }

    @Override
    public void setValue(X entity, Y value) {
        boolean accessible = field.isAccessible();
        try {
            if ( setter != null ) {
                setter.invoke(entity, value);
            } else {
                if ( !accessible ) {
                    field.setAccessible(true);
                }
                field.set(entity, value);
            }
        } catch ( IllegalAccessException | InvocationTargetException e ) {
            throw new RuntimeException(e);
        } finally {
            field.setAccessible(accessible);
        }
    }

    @Override
    public Y getValue(X entity) {
        boolean accessible = field.isAccessible();
        try {
            Object result;
            if ( getter != null ) {
                result = getter.invoke(entity);
            } else {
                if ( !accessible ) {
                    field.setAccessible(true);
                }
                result = field.get(entity);
            }
            //noinspection unchecked
            return (Y) result;
        } catch ( IllegalAccessException | InvocationTargetException e ) {
            e.printStackTrace();
        } finally {
            field.setAccessible(accessible);
        }
        throw new RuntimeException();
    }

    @Override
    public String initColumnName() {
        Column column = getAnnotation(Column.class);
        String columnName;
        if ( column != null && column.name().length() != 0 ) {
            columnName = column.name();
        } else {
            columnName = field.getName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
        }
        if ( columnName.startsWith(FIX) && columnName.endsWith(FIX) ) {
            columnName = columnName.substring(1, columnName.length() - 1);
        }
        return columnName;
    }

    @Override
    public String getFieldName() {
        return field.getName();
    }

    private Class<Y> initJavaType() {
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

    @Override
    public Class<?> getEntityType() {
        return entityType;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public ManyToOne getManyToOne() {
        return manyToOne;
    }

    @Override
    public OneToMany getOneToMany() {
        return oneToMany;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    @Override
    public Version getVersion() {
        return version;
    }

    @Override
    public JoinColumn getJoinColumn() {
        return joinColumn;
    }

    @Override
    public ManyToMany getManyToMany() {
        return manyToMany;
    }

    @Override
    public OneToOne getOneToOne() {
        return oneToOne;
    }

    @Override
    public boolean isEntityType() {
        return getJavaType().getAnnotation(Entity.class) != null;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public boolean isCollection() {
        return collection;
    }

    @Override
    public Class<Y> getJavaType() {
        return javaType;
    }

    @Override
    public Method getSetter() {
        return setter;
    }

    @Override
    public Method getGetter() {
        return getter;
    }

}
