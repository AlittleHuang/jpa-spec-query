## database

```sql
/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : 65001

 Date: 21/02/2019 15:48:47
*/

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

## example
[https://github.com/AlittleHuang/demo-jpa-spec-query](https://github.com/AlittleHuang/demo-jpa-spec-query);
### dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.github</groupId>
        <artifactId>jpa-spec-query</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private EntityManager entityManager;


    @Test
    public void demo() {
        TypeRepostory<User> repostory = new TypeRepostory<>(User.class, entityManager);

        //select by id
        User selectById = repostory.getCriteria()
                .andEqual(User::getId, 1)
                .getOne();


        // fetch:
        // select user inner join company
        User fetch = repostory.getCriteria()
                .andEqual(User::getId, 1)
                .fetch(User::getCompany)
                .getOne();

        // System.out.println(selectById.getCompany());//no Session
        System.out.println(fetch.getCompany());//OK

        // select by name and age
        User luna = repostory.getCriteria()
                .andEqual(User::getName, "Luna")
                .andEqual(User::getAge, 18)
                .getOne();

    }

}
```