# Quick start

### database

```sql

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for company
-- ----------------------------
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company`  (
  `id` int(11) NOT NULL,
  `addr` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of company
-- ----------------------------
INSERT INTO `company` VALUES (1, 'ShangHai', 'Microsoft');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL,
  `age` int(11) NULL DEFAULT NULL,
  `company_id` int(11) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK2yuxsfrkkrnkn5emoobcnnc3r`(`company_id`) USING BTREE,
  CONSTRAINT `FK2yuxsfrkkrnkn5emoobcnnc3r` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 18, 1, 'Luna');

SET FOREIGN_KEY_CHECKS = 1;
```

### applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context" xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:jpa="http://www.springframework.org/schema/data/jpa"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd http://www.springframework.org/schema/data/jpa http://www.springframework.org/schema/data/jpa/spring-jpa.xsd">

    <context:component-scan base-package="com.github"/>

    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource" destroy-method="close">
        <property name="driverClass" value="com.mysql.jdbc.Driver"/>
        <property name="jdbcUrl" value="jdbc:mysql:///test"/>
        <property name="user" value="root"/>
        <property name="password" value="root"/>
    </bean>


    <bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="jpaVendorAdapter">
            <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter"/>
        </property>
        <property name="packagesToScan" value="com.github"/>
        <property name="jpaProperties">
            <props>
                <prop key="hibernate.dialect.storage_engine">innodb</prop>
                <prop key="hibernate.ejb.naming_strategy">org.hibernate.cfg.ImprovedNamingStrategy</prop>
                <prop key="hibernate.show_sql">false</prop>
                <prop key="hibernate.format_sql">true</prop>
                <prop key="hibernate.hbm2ddl.auto">update</prop>
                <prop key="hibernate.enable_lazy_load_no_trans">update</prop>
            </props>
        </property>
    </bean>

    <bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
        <property name="entityManagerFactory" ref="entityManagerFactory"/>
    </bean>

    <jpa:repositories base-package="com.github" repository-impl-postfix="Impl"
                      entity-manager-factory-ref="entityManagerFactory" transaction-manager-ref="transactionManager"/>
    <tx:annotation-driven/>
</beans>
```

### Company.class
```java
@Data
@Entity
@Table(name = "company")
public class Company {

    @Id
    private Integer id;
    private String name;
    private String addr;

}
```

### User.class

```java
@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    private Integer id;
    private String name;
    private Integer age;

    @Column(name = "company_id")
    private Integer companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", updatable = false, insertable = false)
    private Company company;


}
```

### demo

```java
public class Test {

    public static void main(String[] args) {

        ApplicationContext appCtx = new ClassPathXmlApplicationContext("config/applicationContext.xml");
        EntityManager entityManager = appCtx.getBean(EntityManager.class);

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
```
