package com.github.test;

import com.github.data.query.specification.Getter;
import com.github.jpa.repostory.TypeRepostory;
import com.github.test.entity.Company;
import com.github.test.entity.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManager;
import java.util.List;

public class Test {

    public static ApplicationContext appCtx = new ClassPathXmlApplicationContext("config/applicationContext.xml");
    public static EntityManager entityManager = appCtx.getBean(EntityManager.class);

    public static void main(String[] args) {

        TypeRepostory<User> repostory = new TypeRepostory<>(User.class, entityManager);

        //select by id
        User selectById = repostory.query()
                .andEq(User::getId, 1)
                .getSingleResult();

        // fetch:
        // select user inner join company
        User fetch = repostory.query()
                .andEq(User::getId, 1)
                .fetch(User::getCompany)
                .getSingleResult();

        // System.out.println(selectById.getCompany());//no Session
        System.out.println(fetch.getCompany());//OK

        // select by name and age
        User luna = repostory.query()
                .andEq(User::getName, "Luna")
                .andEq(User::getAge, 18)
                .getSingleResult();


        // select user by company name
        Getter<User, String> companyName = Getter.of(User::getCompany).to(Company::getName);
        List<User> list = repostory.query()
                .andEq(companyName, "Microsoft")
                .getResultList();

        Getter<User, String> getName = User::getName;
        repostory.query()
                .andEqual(companyName, User::getName)
                .getResultList();
    }
}
