package com.github.test;

import com.github.data.query.specification.Path;
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
                .andEqual(User::getId, 1)
                .getSingleResult();

        // fetch:
        // select user inner join company
        User fetch = repostory.query()
                .andEqual(User::getId, 1)
                .addFetchs(User::getCompany)
                .getSingleResult();

        // System.out.println(selectById.getCompany());//no Session
        System.out.println(fetch.getCompany());//OK

        // select by name and age
        User luna = repostory.query()
                .andEqual(User::getName, "Luna")
                .andEqual(User::getAge, 18)
                .getSingleResult();


        // select user by company name
        List<User> list = repostory.query()
                .andEqual(Path.of(User::getCompany).to(Company::getName), "Microsoft")
                .getResultList();
    }
}
