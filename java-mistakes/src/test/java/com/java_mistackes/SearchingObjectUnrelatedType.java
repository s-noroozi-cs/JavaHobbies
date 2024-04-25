package com.java_mistackes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.junit.jupiter.api.Test;

public class SearchingObjectUnrelatedType {

  @Test
  void searchUnrelatedType() {
    Short one = 1, two = 2;
    Set<Short> numbers = Set.of(one, two);

    assertTrue(numbers.contains(one));
    assertFalse(numbers.contains(2)); // care about boxed number
    assertTrue(numbers.contains((short) 2));

    assertTrue(one instanceof Short);
    Number sumOfTwoShortValue = one + two; // care about boxed number
    assertTrue(sumOfTwoShortValue instanceof Integer);
  }
}
