package com.github.alittlehuang.test.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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

    @Version
    private Long optlock;

    public User(String username, String password, String secondpwd) {
        this.username = username;
        this.password = password;
        this.secondpwd = secondpwd;
    }

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "pid" )
    private User puser;

    @OneToMany
    @JoinColumn( name = "pid" )
    private List<User> children;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", secondpwd='" + secondpwd + '\'' +
                '}';
    }
}
