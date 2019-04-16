package com.github.test;

import com.github.alittlehuang.test.entity.User;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class Main {

    public static void main(String[] args) throws NoSuchFieldException {
//        ApplicationContext appCtx = new ClassPathXmlApplicationContext(
//                "config/applicationContext.xml");
//        EntityManager entityManager = appCtx.getBean(EntityManager.class);
//        Root<User> root = entityManager.getCriteriaBuilder().createQuery().from(User.class);
//
//        EntityType<User> model = root.getModel();
//        System.out.println(model);

        Field users = User2.class.getDeclaredField("users");

        Type genericType = users.getGenericType();
        System.out.println(genericType);
        if (genericType instanceof ParameterizedType) {
            for (Type argument : ((ParameterizedType) genericType).getActualTypeArguments()) {
                System.out.println(argument);
            }
        }

    }


    class User2 {
        private Integer id;
        private String name;
        private Integer age;

        List<User> users;
    }

}
