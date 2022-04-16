package com.hobi.proxyaspect.model;

import javax.validation.constraints.Min;

public class SayHelloModel {
    @Min(message = "min value validation",value = 18)
    private int age;
    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
