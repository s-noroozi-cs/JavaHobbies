package com.java_mistackes;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConcurrentModification {
  @Test
  void for_each_add_new_item() {
    List<String> list = new ArrayList<>(List.of("a", "b", "c"));

    Assertions.assertThrowsExactly(
        ConcurrentModificationException.class,
        () -> {
          for (String item : list) if (item.equals("b")) list.add("x");
        });
  }

  @Test
  void iterator_add_new_item() {
    List<String> list = new ArrayList<>(List.of("a", "b", "c"));

    Assertions.assertThrowsExactly(
        ConcurrentModificationException.class,
        () -> {
          Iterator<String> iterator = list.iterator();
          while (iterator.hasNext()) if (iterator.next().equals("b")) list.add("x");
        });
  }

  @Test
  void for_each_remove_existing_item() {
      List<String> list = new ArrayList<>(List.of("a", "b", "c"));
    List<String> traverseItems = new ArrayList<>();
    for (String item : list) {
      traverseItems.add(item);
      if (item.equals("b")) {
        list.remove("a");
      }
    }
    Assertions.assertNotEquals("a,b,c", traverseItems.stream().collect(Collectors.joining(",")));
  }

    @Test
    void iterator_remove_existing_item() {
        List<String> list = new ArrayList<>(List.of("a", "b", "c"));
        List<String> traverseItems = new ArrayList<>();
        Iterator<String> iterator = list.listIterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            traverseItems.add(item);
            if (item.equals("b")) {
                list.remove("a");
            }
        }
        Assertions.assertNotEquals("a,b,c", traverseItems.stream().collect(Collectors.joining(",")));
    }
}
