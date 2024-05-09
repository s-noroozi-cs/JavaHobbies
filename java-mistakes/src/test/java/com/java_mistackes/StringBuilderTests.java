package com.java_mistackes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class StringBuilderTests {
  @Test
  void constructorIssue() {
    StringBuilder sb = new StringBuilder('[');
    sb.append("hello");
    sb.append(']');
    assertEquals("hello]", sb.toString());

    sb = new StringBuilder("[");
    sb.append("hello");
    sb.append(']');
    assertEquals("[hello]", sb.toString());

    sb = new StringBuilder(); // default capacity is 16
    assertEquals(16, sb.capacity());

    sb = new StringBuilder('a'); // ascii code 'a' is 97
    assertEquals(97, sb.capacity());
  }
}
