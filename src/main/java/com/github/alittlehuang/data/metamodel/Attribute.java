package com.github.alittlehuang.data.metamodel;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author ALittleHuang
 */
public class Attribute {

    public static final String FIX = "`";

    private final Field field;
    private final Method getter;
    private final Method setter;
    private final Class<?> entityType;
    private final ManyToOne manyToOne;
    private final OneToMany oneToMany;
    private final JoinColumn joinColumn;
    private final Version version;
    private final Column column;
    private final ManyToMany manyToMany;
    private final OneToOne oneToOne;
    private final String columnName;

    public Attribute(Field field, Method getter, Method setter, Class<?> entityType) {
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

    public Class<Object> getFieldType() {
        //noinspection unchecked
        return (Class<Object>) field.getType();
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
        return getFieldType().getAnnotation(Entity.class) != null;
    }

    public String getColumnName() {
        return columnName;
    }
}
