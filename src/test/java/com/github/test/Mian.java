package com.github.test;

import com.github.data.query.specification.Getter;
import com.github.test.entity.User;

import java.util.Arrays;

public class Mian {

//    public static void main(String[] args) {
//
//        EntityManager em = Test.entityManager;
//        TransactionalService service = Test.appCtx.getBean(TransactionalService.class);
//
//        service.required(() -> {
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaUpdate<User> update = cb.
//                    createCriteriaUpdate(User.class);
//
//            // set the root class
//            Root e = update.from(User.class);
//
//            // set update and where clause
//            update.set("name", "777");
//            update.where(cb.equal(e.get("id"), 1));
//
//            // perform update
//            em.createQuery(update).executeUpdate();
//        });
//
//    }

    public static void main(String[] args) {
        Integer x = null;
        switch ( x ){
            default:
                System.out.println(x);
        }
    }

}
