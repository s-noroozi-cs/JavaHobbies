package com.java_mistackes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringCharacter {

    @Test
    void simpleString(){
        String hello = "hello";
        Assertions.assertEquals(5,hello.length());
    }

    @Test
    void SpecialCharacterString(){
    String hello = "hello ⚽";
    char[] chars = hello.toCharArray();
    System.out.println(hello);
        Assertions.assertEquals(7,hello.length());
    }

}
