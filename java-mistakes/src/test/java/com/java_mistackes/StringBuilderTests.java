package com.java_mistackes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringBuilderTests {
  @Test
  void constructorIssue() {
    StringBuilder sb = new StringBuilder('[');
    sb.append("hello");
    sb.append(']');
    Assertions.assertEquals("hello]", sb.toString());

    sb = new StringBuilder("[");
    sb.append("hello");
    sb.append(']');
    Assertions.assertEquals("[hello]", sb.toString());
  }
}
