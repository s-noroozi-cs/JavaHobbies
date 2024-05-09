package com.java_mistackes;

import org.junit.jupiter.api.Test;

public class StringBuilderTests {
  @Test
  void x() {
    StringBuilder sb = new StringBuilder('[');
    sb.append("hello");
    sb.append(']');
    System.out.println(sb.toString());
  }
}
