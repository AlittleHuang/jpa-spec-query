package com.github.test;

import com.github.jpa.support.SpecBuilder;
import com.github.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserRepostory extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {


    //↓↓↓↓ test ↓↓↓↓

    UserRepostory userRepostory = Test.appCtx.getBean(UserRepostory.class);
    static void main(String[] args) {
        SpecBuilder<User> spec = new SpecBuilder<User>().andEqual(User::getId, 1);
        List<User> all = userRepostory.findAll(spec);
    }

}
