package com.github.test.entity;

import lombok.Data;

import javax.persistence.*;

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
