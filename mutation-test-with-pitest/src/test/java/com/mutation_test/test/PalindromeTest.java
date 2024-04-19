package com.mutation_test.test;

import static org.junit.jupiter.api.Assertions.*;

import com.mutation_test.code.Palindrome;
import org.junit.jupiter.api.Test;

class PalindromeTest {
  @Test
  public void whenPalindromeThenAccept() {
    Palindrome palindromeTester = new Palindrome();
    assertTrue(palindromeTester.isPalindrome("noon"));
  }
}
