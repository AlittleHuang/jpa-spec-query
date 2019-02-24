package com.github.jpa.spec.util;

import com.github.jpa.spec.Getters;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JpaHelper {

    private final static Map<Class, String> map = new HashMap<>();

    public static <T> String getPropertyNameFromGetter(Class<T> type, Getters<T, ?> getters) {

        Class key = getters.getClass();

        String name = map.get(key);
        if (name != null) {
            return name;
        } else {
            synchronized (map) {

                name = map.get(key);
                if (name != null) {
                    return name;
                }

                name = Proxy.getPropertyName(type, getters);
                map.put(key, name);

                return name;

            }
        }

    }

    private static class Proxy implements MethodInterceptor {

        private static Map<Class<?>, Object> instanceMap = new ConcurrentHashMap<>();
        private static Proxy proxy = new Proxy();

        private static <T, U> String getGetterName(Class<T> type, Getters<T, U> getters) {
            T target = proxy.getProxyInstance(type);
            try {
                getters.apply(target);
            } catch (Exception e) {
                return e.getMessage();
            }
            throw new RuntimeException();
        }

        private static <T> String getPropertyName(Class<T> type, Getters<T, ?> getters) {
            String getterName = getGetterName(type, getters);
            boolean check = getterName != null && getterName.length() > 3 && getterName.startsWith("get");
            Assert.state(check, "the function is not getters");
            StringBuilder builder = new StringBuilder(getterName.substring(3));
            if (builder.length() == 1) {
                return builder.toString().toLowerCase();
            }
            if (builder.charAt(1) >= 'A' && builder.charAt(1) <= 'Z') {
                return builder.toString();
            }
            builder.setCharAt(0, Character.toLowerCase(builder.charAt(0)));
            return builder.toString();
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
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            throw new Exception(method.getName());
        }

    }



}
