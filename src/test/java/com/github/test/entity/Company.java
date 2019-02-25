package com.github.test.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "company")
public class Company {

    @Id
    private Integer id;
    private String name;
    private String addr;

}
