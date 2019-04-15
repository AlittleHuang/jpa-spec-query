package com.github.alittlehuang.data.test;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.github.alittlehuang.data.jpa.repostory.TypeRepository;
import com.github.alittlehuang.data.query.specification.AggregateFunctions;
import com.github.alittlehuang.data.query.specification.Expressions;
import com.github.alittlehuang.data.query.specification.Query;
import com.github.alittlehuang.test.entity.User;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.criteria.JoinType;
import javax.sql.DataSource;
import java.util.List;

public class Test {

    static TypeRepository<User> repository;
    static ApplicationContext appCtx;
    static DataSource dataSource;
    static {
        ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
        LoggerContext context = (LoggerContext) iLoggerFactory;
        context.getLogger("ROOT").setLevel(Level.ERROR);
        context.getLogger("org.hibernate.SQL").setLevel(Level.DEBUG);
        context.getLogger("com.github.alittlehuang").setLevel(Level.DEBUG);
        ApplicationContext appCtx = new ClassPathXmlApplicationContext("config/applicationContext.xml");
        dataSource = appCtx.getBean(DataSource.class);
        Test.appCtx = appCtx;
        EntityManager entityManager = appCtx.getBean(EntityManager.class);
        repository = new TypeRepository<>(User.class, entityManager);

    }

    public static void main(String[] args) {
        getQuery()
                .notEq(Expressions.function("LOG10", User::getId),1)
                .notEq(Expressions.function("LOG", User::getId),2)
                .getPage();
        test();
        test0();
        test1();
        test2();
        testAbstractCriteriaBuilder();
//        TransactionalService transactional = appCtx.getBean(TransactionalService.class);
//        transactional.required(Test::testLock);
//        System.out.println(transactional.getClass());
        Expressions<User, Integer> getPassword = Expressions.abs(User::getId);
        getQuery().addSelect(getPassword).addGroupings(getPassword)
                .addOrdersAsc(User::getId)
                .setMaxResult(10)
                .getObjectList();

        List<User> resultList = getQuery().fetch(User::getChildren).getResultList();
        System.out.println(resultList);
    }

    public static void test() {

        getQuery()
                .eq(Expressions.abs(User::getId), 1)
                .eq(Expressions.sum(User::getId, 1.1), 1)
                .and()
                .eq(Expressions.sum(User::getId, User::getId), 1)
                .eq(Expressions.diff(User::getId, 1.1), 1)
                .or()
                .eq(Expressions.diff(User::getId, User::getId), 1)
                .eq(Expressions.prod(User::getId, 1.1), 1)
                .eq(Expressions.prod(User::getId, User::getId), 1)
                .eq(Expressions.quot(User::getId, 1.1), 1)
                .and()
                .or()
                .and()
                .eq(Expressions.quot(User::getId, User::getId), 1)
                .eq(Expressions.mod(User::getId, 1), 1)
                .or()
                .eq(Expressions.mod(User::getId, User::getId), 1)
                .eq(Expressions.sqrt(User::getId), 1)
                .or()
                .eq(Expressions.concat(User::getPassword, User::getUsername), "99")
                .eq(Expressions.concat(User::getPassword, "pwd"), "99")
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

                .and(getQuery().eq(User::getId,996).getWhereClause())
                .or(getQuery().eq(User::getId, 996).getWhereClause())
                .and(getQuery().getWhereClause())
                .and(getQuery().getWhereClause())

                .addOrdersDesc(Expressions.trimLeading(User::getUsername, 'x'))
                .addSelect(Expressions.sqrt(User::getId))
                .addSelect(User::getId)
                .setMaxResult(1)
                .getSingleObject();

    }


    public static void test1() {

        getQuery()
                .eq(User::getId, 1)
                .andEq(User::getId, 1)
                .notEq(User::getId, 1)
                .andNotEq(User::getId, 1)
                .notEqual(User::getId, User::getId)
                .orNotGreaterThanOrEqual(User::getId, User::getId)
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
                .andGreaterThanOrEqual(User::getId, User::getId)
                .orNotLessThanOrEqual(User::getId, User::getId)
                .orNotGreaterThan(User::getId, User::getId)
                .orNotLessThan(User::getId, User::getId)
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
                .getPage();

    }

    public static void test0() {

        getQuery()
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

        getQuery()
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
        getQuery()
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
                //.setLockModeType(LockModeType.WRITE)
                .getObjectList();


        getQuery()
                .fetch(User::getPuser)
                .fetch("puser.puser", JoinType.INNER)
                .setPageable(2, 5).getResultList();

        getQuery()
                .fetch(User::getPuser, JoinType.INNER)
                .fetch("puser.puser", "puser")
                .setPageable(2, 5).getResultList();

    }

    private static void testLock(){
        Query<User> userQuery = getQuery().setLockModeType(LockModeType.OPTIMISTIC);
        userQuery.getResultList();
        getQuery().setLockModeType(LockModeType.OPTIMISTIC_FORCE_INCREMENT).getResultList();
        getQuery().setLockModeType(LockModeType.PESSIMISTIC_READ).getResultList();//lock in share mode
        getQuery().setLockModeType(LockModeType.PESSIMISTIC_WRITE).getResultList();//for update
        getQuery().setLockModeType(LockModeType.PESSIMISTIC_FORCE_INCREMENT).getResultList();//for update
    }

    private static Query<User> getQuery() {
//        JdbcQueryStored<User> stored = new JdbcQueryStored<>(new JdbcQueryStoredConfig(dataSource), User.class);
//        return new QueryImpl<>(stored);
        return repository.query();
    }

}


//Criterion