//package com.github.test;
//
//import com.github.data.query.support.Expressions;
//import com.github.jpa.repostory.TypeRepository;
//import com.github.test.entity.Company;
//import com.github.test.entity.User;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//
//public class Test {
//
//
//    public static void main(String[] args) {
//        ApplicationContext appCtx = new ClassPathXmlApplicationContext("config/applicationContext.xml");
//        EntityManager entityManager = appCtx.getBean(EntityManager.class);
//
//        TypeRepository<User> repostory = new TypeRepository<>(User.class, entityManager);
//
//        //select by id
//        User selectById = repostory.query()
//                .andEq(User::getId, 1)
//                .getSingleResult();
//
//        // fetch:
//        // select user inner join company
//        User fetch = repostory.query()
//                .andEq(User::getId, 1)
//                .fetch(User::getCompany)
//                .getSingleResult();
//
//        // System.out.println(selectById.getCompany());//no Session
//        System.out.println(fetch.getCompany());//OK
//
//        // select by name and age
//        User luna = repostory.query()
//                .andEq(User::getName, "Luna")
//                .andEq(User::getAge, 18)
//                .getSingleResult();
//
//
//        // select user by company name
//        Expressions<User, String> companyName = Expressions.of(User::getCompany).to(Company::getName);
//        List<User> list = repostory.query()
//                .andEq(companyName, "Microsoft")
//                .getResultList();
//
//        Expressions<User, String> getName = User::getName;
//        repostory.query()
//                .andEqual(companyName, User::getName)
//                .getResultList();
//
//        repostory.query().eq(Expressions.coalesceVal(User::getName, ""), "");
//        repostory.query().eq(Expressions.nullifVal(User::getName, ""), "");
//        repostory.query().eq(Expressions.nullif(User::getName, User::getName), "");
//        repostory.query().eq(Expressions.sum(User::getId, User::getId), 1);
//
//        repostory.query()
//                .andGreaterThan(User::getName, User::getName)
//                .equal(User::getName, User::getAge)
//                .getResultList();
//    }
//}
