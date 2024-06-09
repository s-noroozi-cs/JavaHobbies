package com.java_mistackes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringCharacter {

  @Test
  void simpleString() {
    String hello = "hello";
    Assertions.assertEquals(5, hello.length());
  }

  @Test
  void SpecialCharacterString() {
    String hello = "hello \ud83d\udc4b\ud83c\udfff";//hello 👋🏿
    Assertions.assertNotEquals(7, hello.length());
  }
}
