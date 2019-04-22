package com.github.alittlehuang.data.query.specification;


import com.github.alittlehuang.data.util.Assert;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ALittleHuang
 */
public class GetterMethodUtil {

    private final static Map<Class, Method> CLASS_METHOD_MAP = new HashMap<>();
    public static final char CHAR_A = 'A';
    public static final char CHAR_Z = 'Z';

    static <T> String[] getAttrNames(Class<? extends T> type, Expressions<T, ?> getters) {
        Class cls = type == null ? Object.class : type;
        List<Expressions<?, ?>> list = getters.list();
        String[] strings = new String[list.size()];
        int i = 0;
        for ( Expressions expression : list ) {
            //noinspection unchecked
            Method method = getMethod(cls, expression, type == null);
            cls = method.getReturnType();
            strings[i++] = toAttrName(method.getName());
        }
        return strings;
    }

    private static Method getMethod(Expressions<?, ?> getters) {
        //noinspection unchecked
        return getMethod(Object.class, (Expressions<Object, ?>) getters, true);
    }

    private static <T> Method getMethod(Class<T> type, Expressions<T, ?> getters, boolean cast) {
        Class key = getters.getClass();
        Method method = null;
        if ( !CLASS_METHOD_MAP.containsKey(key) ) {
            synchronized ( CLASS_METHOD_MAP ) {
                method = CLASS_METHOD_MAP.get(key);
                if ( method == null ) {
                    method = Proxy.getMethod(type, getters, cast);
                    CLASS_METHOD_MAP.put(key, method);
                }
            }
        }
        if ( method == null ) {
            method = CLASS_METHOD_MAP.get(key);
            Objects.requireNonNull(method,"The function is not getter of " + type.getName());
        }
        return method;
    }

    public static String toAttrName(String getterName) {
        boolean check = getterName != null && getterName.length() > 3 && getterName.startsWith("get");
        Assert.state(check, "the function is not getters");
        StringBuilder builder = new StringBuilder(getterName.substring(3));
        if ( builder.length() == 1 ) {
            return builder.toString().toLowerCase();
        }
        if ( builder.charAt(1) >= CHAR_A && builder.charAt(1) <= CHAR_Z ) {
            return builder.toString();
        }
        builder.setCharAt(0, Character.toLowerCase(builder.charAt(0)));
        return builder.toString();
    }

    private static class Proxy implements MethodInterceptor {

        private static Map<Class<?>, Object> instanceMap = new ConcurrentHashMap<>();
        private static Proxy proxy = new Proxy();

        private static <T> Method getMethod(Class<T> type, Expressions<T, ?> getters, boolean cast) {
            T target = proxy.getProxyInstance(type);
            try {
                getters.apply(target);
            } catch ( Exception e ) {
                if ( e.getClass() == MethodInfoException.class ) {
                    //noinspection ConstantConditions
                    return ( (MethodInfoException) e ).getMethod();
                }
                if ( cast && e.getClass() == ClassCastException.class ) {
                    String message = e.getMessage();
                    int i = message.lastIndexOf(' ');
                    try {
                        //noinspection unchecked
                        type = (Class<T>) Class.forName(message.substring(i + 1));
                        return getMethod(type, getters, false);
                    } catch ( ClassNotFoundException e1 ) {
                        throw new RuntimeException(e1);
                    }
                }
            }
            return null;
        }

        private <T> T getProxyInstance(Class<T> type) {
            //noinspection unchecked
            return (T) instanceMap.computeIfAbsent(type, it -> {
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(type);
                enhancer.setCallback(this);
                return enhancer.create();
            });
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)
                throws Throwable {
            throw new MethodInfoException(method);
        }

        private static class MethodInfoException extends Exception {
            @lombok.Getter
            final Method method;

            public MethodInfoException(Method method) {
                this.method = method;
            }
        }

    }


}
