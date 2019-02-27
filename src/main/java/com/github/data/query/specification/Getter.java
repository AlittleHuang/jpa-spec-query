package com.github.data.query.specification;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@FunctionalInterface
public interface Getter<T, R> extends Attribute<T> {

    @SuppressWarnings("UnusedReturnValue")
    R apply(T t);

    static <T, R> Getter<T, R> of(Getter<T, R> getters) {
        return getters;
    }

    default <V, U extends Getter<? super R, ? extends V>> Getter<T, V> to(U after) {
        Getter<T, R> previous = this;//上一个
        return new Getter<T, V>() {
            List<Getter<?, ?>> list = new ArrayList<>();

            {
                list.add(previous);
                list.add(after);
            }

            @Override
            public V apply(T t) {
                return null;
            }

            @Override
            public List<Getter<?, ?>> list() {
                return list;
            }

            @Override
            public <X, Y extends Getter<? super V, ? extends X>> Getter<T, X> to(Y after) {
                list.add(after);
                //noinspection unchecked
                return (Getter<T, X>) this;
            }

        };
    }

    default List<Getter<?, ?>> list() {
        return Collections.singletonList(this);
    }

    default String[] getNames(Class<T> type) {
        return Util.getAttrNames(type, this);
    }

    default String[] getNames() {
        //noinspection unchecked
        return Util.getAttrNames((Class<T>) Object.class, this);
    }

    class Util {

        private final static Map<Class, Method> map = new HashMap<>();

        public static <T> String[] getAttrNames(Class<T> type, Getter<T, ?> getters) {
            List<Getter<?, ?>> list = getters.list();
            String[] strings = new String[list.size()];
            int i = 0;
            Class cls = type;
            for (Getter getter : list) {
                //noinspection unchecked
                Method method = getMethod(cls, getter);
                cls = method.getReturnType();
                strings[i++] = toAttrName(method.getName());
            }
            return strings;
        }

        public static <T> Method getMethod(Class<T> type, Getter<T, ?> getters) {
            Class key = getters.getClass();
            Method name = map.get(key);
            if (name == null) {
                synchronized (map) {
                    name = map.get(key);
                    if (name == null) {
                        name = Proxy.getMethod(type, getters);
                        map.put(key, name);
                    }
                }
            }
            return name;
        }

        private static String toAttrName(String getterName) {
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

        private static class Proxy implements MethodInterceptor {

            private static Map<Class<?>, Object> instanceMap = new ConcurrentHashMap<>();
            private static Proxy proxy = new Proxy();

            private static <T> Method getMethod(Class<T> type, Getter<T, ?> getters) {
                T target = proxy.getProxyInstance(type);
                try {
                    getters.apply(target);
                } catch (Exception e) {
                    if (e.getClass() == MethodInfo.class)
                        //noinspection ConstantConditions
                        return ((Proxy.MethodInfo) e).getMethod();
                    if (e.getClass() == ClassCastException.class) {
                        String message = e.getMessage();
                        int i = message.lastIndexOf(' ');
                        try {
                            //noinspection unchecked
                            type = (Class<T>) Class.forName(message.substring(i + 1));
                            return getMethod(type, getters);
                        } catch (ClassNotFoundException e1) {
                            throw new RuntimeException(e1);
                        }
                    }
                    throw new RuntimeException(e);
                }
                throw new RuntimeException();
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
                throw new MethodInfo(method);
            }

            private static class MethodInfo extends Exception {
                @lombok.Getter
                final Method method;

                public MethodInfo(Method method) {
                    this.method = method;
                }
            }

        }


    }

}
