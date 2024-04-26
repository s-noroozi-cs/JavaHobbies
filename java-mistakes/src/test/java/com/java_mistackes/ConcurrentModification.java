package com.java_mistackes;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConcurrentModification {
  @Test
  void avoidConcurrentModification() {
    List<String> letterList = new ArrayList<>(List.of("a", "b", "c"));

    Assertions.assertThrowsExactly(
        ConcurrentModificationException.class,
        () -> {
          for (String letter : letterList)
              if (letter.equals("b"))
                  letterList.add("x");
        });

    Assertions.assertThrowsExactly(
        ConcurrentModificationException.class,
        () -> {
          Iterator<String> iterator = letterList.iterator();
          while (iterator.hasNext())
              if (iterator.next().equals("b"))
                  letterList.add("x");
        });

    for (int i = 0; i < letterList.size(); i++)
      if (letterList.get(i).equals("b"))
          letterList.add("x");

      List<String> list = new ArrayList<>(List.of("a", "b", "c"));
      for (String s : list) {
          System.out.println(s);
          if (s.equals("b")) {
              list.remove("a");
          }
      }
  }
}
