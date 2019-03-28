package com.github.test;

import com.github.jpa.support.SpecBuilder;
import com.github.test.entity.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {


    //↓↓↓↓ test ↓↓↓↓
    ApplicationContext appCtx = new ClassPathXmlApplicationContext("config/applicationContext.xml");
    UserRepository userRepository = appCtx.getBean(UserRepository.class);
    static void main(String[] args) {
        SpecBuilder<User> spec = new SpecBuilder<User>()
                .andEq(User::getName, "Luna")
                .andEq(User::getAge, 18);
        List<User> all = userRepository.findAll(spec);
    }

}
