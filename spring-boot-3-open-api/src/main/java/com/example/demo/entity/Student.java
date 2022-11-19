package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

//import javax.persistence.Entity;
//import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Student {
    @Id
    private Long id;
    private String name;
    private Integer age;
}
