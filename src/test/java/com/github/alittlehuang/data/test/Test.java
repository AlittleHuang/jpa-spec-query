package com.github.alittlehuang.data.test;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.github.data.query.specification.AggregateFunctions;
import com.github.data.query.support.Expressions;
import com.github.jpa.repostory.TypeRepository;
import com.github.test.entity.User;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

public class Test {

    static TypeRepository<User> repository;

    static {
        ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
        LoggerContext context = (LoggerContext) iLoggerFactory;
        context.getLogger("ROOT").setLevel(Level.ERROR);
        context.getLogger("org.hibernate.SQL").setLevel(Level.DEBUG);
        ApplicationContext appCtx = new ClassPathXmlApplicationContext("config/applicationContext.xml");
        EntityManager entityManager = appCtx.getBean(EntityManager.class);
        repository = new TypeRepository<>(User.class, entityManager);

    }

    public static void main(String[] args) {
        test();
        test0();
        test1();
        test2();
        testAbstractCriteriaBuilder();
    }

    public static void test() {

        repository.query()
                .eq(Expressions.abs(User::getId), 1)
                .eq(Expressions.sum(User::getId, 1.1), 1)
                .eq(Expressions.sum(User::getId, User::getId), 1)
                .eq(Expressions.diff(User::getId, 1.1), 1)
                .eq(Expressions.diff(User::getId, User::getId), 1)
                .eq(Expressions.prod(User::getId, 1.1), 1)
                .eq(Expressions.prod(User::getId, User::getId), 1)
                .eq(Expressions.quot(User::getId, 1.1), 1)
                .eq(Expressions.quot(User::getId, User::getId), 1)
                .eq(Expressions.mod(User::getId, 1), 1)
                .eq(Expressions.mod(User::getId, User::getId), 1)
                .eq(Expressions.sqrt(User::getId), 1)
                .eq(Expressions.concat(User::getPassword, User::getUsername), "99")
                .eq(Expressions.concat(User::getPassword, "User::getUsername"), "99")
                .eq(Expressions.substring(User::getPassword, 1), "99")
                .eq(Expressions.substring(User::getPassword, 1, 2), "99")
                .eq(Expressions.trim(User::getPassword), "99")
                .eq(Expressions.trim(User::getPassword, 'x'), "99")
                .eq(Expressions.trimTrailing(User::getPassword), "99")
                .eq(Expressions.trimTrailing(User::getPassword, 'x'), "99")
                .eq(Expressions.trimLeading(User::getPassword), "99")
                .eq(Expressions.trimLeading(User::getPassword, 'x'), "99")
                .eq(Expressions.lower(User::getPassword), "99")
                .eq(Expressions.upper(User::getPassword), "99")
                .eq(Expressions.length(User::getPassword), 1)
                .eq(Expressions.locate(User::getPassword, "33"), 1)
                .eq(Expressions.locate(User::getPassword, User::getUsername), 1)
                .eq(Expressions.coalesceVal(User::getPassword, "1"), "2")
                .eq(Expressions.coalesce(User::getPassword, User::getPassword), "2")
                .eq(Expressions.nullif(User::getPassword, User::getPassword), "2")
                .eq(Expressions.nullifVal(User::getPassword, "33"), "2")


                .addOrdersDesc(Expressions.trimLeading(User::getUsername, 'x'))
                .addSelect(Expressions.sqrt(User::getId))
                .setMaxResult(1)
                .getSingleObject();

    }


    public static void test1() {

        repository.query()
                .eq(User::getId, 1)
                .andEq(User::getId, 1)
                .notEq(User::getId, 1)
                .andNotEq(User::getId, 1)
                .notEqual(User::getId, User::getId)
                .andNotEqual(User::getId, User::getId)
                .equal(User::getId, User::getId)
                .andEqual(User::getId, User::getId)
                .gt(User::getId, 1)
                .andGt(User::getId, 1)
                .greaterThan(User::getId, User::getId)
                .andGreaterThan(User::getId, User::getId)
                .lt(User::getId, 1)
                .andLt(User::getId, 1)
                .lessThan(User::getId, User::getId)
                .andLessThan(User::getId, User::getId)
                .ge(User::getId, 1)
                .andGe(User::getId, 1)
                .greaterThanOrEqual(User::getId, User::getId)
                .andGreaterThanOrEqual(User::getId, User::getId)
                .le(User::getId, 1)
                .andLe(User::getId, 1)
                .lessThanOrEqual(User::getId, User::getId)
                .andLessThanOrEqual(User::getId, User::getId)
                .between(User::getId, 1, 2)
                .andBetween(User::getId, 1, 2)
                .notBetween(User::getId, 1, 2)
                .andNotBetween(User::getId, 1, 2)
                .in(User::getId, 1, 2, 3)
                .andIn(User::getId, 1, 2, 3)
                .notIn(User::getId, 1, 2, 3)
                .andNotIn(User::getId, 1, 2, 3)
                .like(User::getUsername, "aaa")
                .andLike(User::getUsername, "aaa")
                .notLike(User::getUsername, "aaa")
                .andNotLike(User::getUsername, "aaa")
                .isNotNull(User::getUsername)
                .andIsNotNull(User::getUsername)
                .isNull(User::getUsername)
                .andIsNull(User::getUsername)
                .orEq(User::getId, 1)
                .orNotEq(User::getId, 1)
                .orNotEqual(User::getId, User::getId)
                .orEqual(User::getId, User::getId)
                .orGt(User::getId, 1)
                .orGreaterThan(User::getId, User::getId)
                .orLt(User::getId, 1)
                .orLessThan(User::getId, User::getId)
                .orGe(User::getId, 1)
                .orGreaterThanOrEqual(User::getId, User::getId)
                .orLe(User::getId, 1)
                .orLessThanOrEqual(User::getId, User::getId)
                .orBetween(User::getId, 1, 2)
                .orNotBetween(User::getId, 1, 2)
                .orIn(User::getId, 1, 2, 3)
                .orNotIn(User::getId, 1, 2, 3)
                .orLike(User::getUsername, "aaa")
                .orNotLike(User::getUsername, "aaa")
                .orIsNotNull(User::getUsername)
                .orIsNull(User::getUsername)
                .setMaxResult(1)
                .getSingleResult();

    }

    public static void test0() {

        repository.query()
                .andLe("id", 1)
                .isNull(User::getUsername, true)
                .isNull(User::getUsername, false)
                .isNull("username", true)
                .isNull("username", false)
                .andIsNull(User::getUsername, true)
                .andIsNull(User::getUsername, false)
                .andIsNull("username", true)
                .andIsNull("username", false)
                .orIsNull(User::getUsername, true)
                .orIsNull(User::getUsername, false)
                .orIsNull("username", true)
                .orIsNull("username", false)
                .getObjectList();

    }


    public static void test2() {

        repository.query()
                .eq("id", 1)
                .andEq("id", 1)
                .notEq("id", 1)
                .andNotEq("id", 1)
                .gt("id", 1)
                .andGt("id", 1)
                .lt("id", 1)
                .andLt("id", 1)
                .ge("id", 1)
                .andGe("id", 1)
                .le("id", 1)
                .andLe("id", 1)
                .between("id", 1, 2)
                .andBetween("id", 1, 2)
                .notBetween("id", 1, 2)
                .andNotBetween("id", 1, 2)
                .in("id", 1, 2, 3)
                .andIn("id", 1, 2, 3)
                .notIn("id", 1, 2, 3)
                .andNotIn("id", 1, 2, 3)
                .like("username", "aaa")
                .andLike("username", "aaa")
                .notLike("username", "aaa")
                .andNotLike("username", "aaa")
                .isNotNull("username")
                .andIsNotNull("username")
                .isNull("username")
                .andIsNull("username")
                .orEq("id", 1)
                .orNotEq("id", 1)
                .orGt("id", 1)
                .orLt("id", 1)
                .orGe("id", 1)
                .orLe("id", 1)
                .orBetween("id", 1, 2)
                .orNotBetween("id", 1, 2)
                .orIn("id", 1, 2, 3)
                .orNotIn("id", 1, 2, 3)
                .orLike("username", "aaa")
                .orNotLike("username", "aaa")
                .orIsNotNull("username")
                .orIsNull("username")
                .getObjectList();

    }

    static void testAbstractCriteriaBuilder() {
        repository.query()
                .addSelect("id")
                .addSelect(User::getPassword)
                .addSelect(User::getId, AggregateFunctions.SUM)
                .addSelect(User::getId, AggregateFunctions.AVG)
                .addSelect(User::getId, AggregateFunctions.MAX)
                .addSelect(User::getId, AggregateFunctions.MAX)
                .addSelect(User::getId, AggregateFunctions.MIN)
                .addSelect(User::getId, AggregateFunctions.COUNT)
                .addSelect(User::getId, AggregateFunctions.NONE)
                .addGroupings("id", "username")
                .addGroupings(User::getPassword)
                .addOrdersAsc("id", "username")
                .addOrdersAsc(User::getPassword)
                .addOrdersDesc("id", "username")
                .addOrdersDesc(User::getPassword)

                .setOffset(5)
                .setMaxResult(3)
                .setLockModeType(LockModeType.WRITE)
                .getObjectList();


//        repository.query()
//                .fetch("company2")
//                .fetch(User::getCompany)
//                .fetch(User::getCompany3, JoinType.INNER)
//                .setPageable(2, 5).getResultList();

    }

}


//Criterion