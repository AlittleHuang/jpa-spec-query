package com.github.alittlehuang.test;

import com.github.alittlehuang.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

//    //↓↓↓↓ test ↓↓↓↓
//    ApplicationContext appCtx = new ClassPathXmlApplicationContext("config/applicationContext.xml");
//    UserRepository userRepository = appCtx.getBean(UserRepository.class);
//    static void main(String[] args) {
//        SpecBuilder<User> spec = new SpecBuilder<User>()
//                .andEq(User::getName, "Luna")
//                .andEq(User::getAge, 18);
//        List<User> all = userRepository.findAll(spec);
//    }

}
