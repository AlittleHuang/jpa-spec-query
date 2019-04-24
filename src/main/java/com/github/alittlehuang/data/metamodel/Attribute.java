package com.github.alittlehuang.data.metamodel;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author ALittleHuang
 */
public interface Attribute<X, Y> {

     <T extends Annotation> T getAnnotation(Class<T> annotationClass);

     void setValue(X entity, Y value);

     Y getValue(X entity);

     String initColumnName();

     String getFieldName();

     Class<?> getEntityType();

     Field getField();

     ManyToOne getManyToOne();

     OneToMany getOneToMany();

     Column getColumn();

     Version getVersion();

     JoinColumn getJoinColumn();

     ManyToMany getManyToMany();

     OneToOne getOneToOne();

     boolean isEntityType();

     String getColumnName();

     boolean isCollection();

     Class<Y> getJavaType();

     Method getSetter();

     Method getGetter();

}
