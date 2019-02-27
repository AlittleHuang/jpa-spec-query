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

    default String[] getNames(Class<? extends T> type) {
        return Util.getAttrNames(type, this);
    }

    default String[] getNames() {
        return getNames(null);
    }

    class Util {

        private final static Map<Class, Method> map = new HashMap<>();

        public static <T> String[] getAttrNames(Class<? extends T> type, Getter<T, ?> getters) {
            Class cls = type == null ? Object.class : type;
            List<Getter<?, ?>> list = getters.list();
            String[] strings = new String[list.size()];
            int i = 0;
            for (Getter getter : list) {
                //noinspection unchecked
                Method method = getMethod(cls, getter, type == null);
                cls = method.getReturnType();
                strings[i++] = toAttrName(method.getName());
            }
            return strings;
        }

        public static <T> Method getMethod(Class<T> type, Getter<T, ?> getters, boolean cast) {
            Class key = getters.getClass();
            Method method = null;
            if (!map.containsKey(key)) {
                synchronized (map) {
                    method = map.get(key);
                    if (method == null) {
                        method = Proxy.getMethod(type, getters, cast);
                        map.put(key, method);
                    }
                }
            }
            if (method == null) {
                method = map.get(key);
                Assert.notNull(method, "The function is not getter of " + type.getName());
            }
            return method;
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

            private static <T> Method getMethod(Class<T> type, Getter<T, ?> getters, boolean cast) {
                T target = proxy.getProxyInstance(type);
                try {
                    getters.apply(target);
                } catch (Exception e) {
                    if (e.getClass() == MethodInfo.class)
                        //noinspection ConstantConditions
                        return ((Proxy.MethodInfo) e).getMethod();
                    if (cast && e.getClass() == ClassCastException.class) {
                        String message = e.getMessage();
                        int i = message.lastIndexOf(' ');
                        try {
                            //noinspection unchecked
                            type = (Class<T>) Class.forName(message.substring(i + 1));
                            return getMethod(type, getters, false);
                        } catch (ClassNotFoundException e1) {
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
