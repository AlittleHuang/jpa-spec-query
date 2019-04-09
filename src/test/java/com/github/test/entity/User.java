package com.github.test.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 用户
 *
 * @author HuangChengwei
 */
@Getter
@Setter
@NoArgsConstructor

@Cacheable
@Entity( name = "user" )
public class User implements Serializable {

    /**
     * id
     */
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer id;

    /**
     * 用户名
     */
    @Column( unique = true )
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 二级密码
     */
    private String secondpwd;

    public User(String username, String password, String secondpwd) {
        this.username = username;
        this.password = password;
        this.secondpwd = secondpwd;
    }
}
