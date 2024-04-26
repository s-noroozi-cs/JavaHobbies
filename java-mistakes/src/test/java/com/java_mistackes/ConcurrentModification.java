package com.java_mistackes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class ConcurrentModification {
  @Test
  void addingNewItemInForEachLoop() {
    List<String> list = new ArrayList<>(List.of("a", "b", "c"));

    assertThrowsExactly(
        ConcurrentModificationException.class,
        () -> {
          for (String item : list) if (item.equals("b")) list.add("x");
        });
  }

  @Test
  void addingNewItemInIteratorLoop() {
    List<String> list = new ArrayList<>(List.of("a", "b", "c"));

    assertThrowsExactly(
        ConcurrentModificationException.class,
        () -> {
          Iterator<String> iterator = list.iterator();
          while (iterator.hasNext()) if (iterator.next().equals("b")) list.add("x");
        });
  }

  @Test
  void removingItemInForEachLoop() {
      List<String> list = new ArrayList<>(List.of("a", "b", "c"));
    List<String> traverseItems = new ArrayList<>();
    for (String item : list) {
      traverseItems.add(item);
      if (item.equals("b")) {
        list.remove("a");
      }
    }
    assertNotEquals("a,b,c", traverseItems.stream().collect(Collectors.joining(",")));
  }

  @Test
  void removingItemInIteratorLoop() {
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
        assertNotEquals("a,b,c", traverseItems.stream().collect(Collectors.joining(",")));
    }

  @Test
  void addingNewItemWithOutConcurrencyModificationException() {
    List<String> list = new ArrayList<>(List.of("a", "b", "c"));
    for (String item : new ArrayList<>(list)) if (item.equals("b")) list.add("x");
    assertTrue(list.contains("x"));

    list = new CopyOnWriteArrayList<>(List.of("a", "b", "c"));
    for (String item : list) if (item.equals("b")) list.add("x");
    assertTrue(list.contains("x"));
  }

  @Test
  void removingItemWithOutAnyIssue() {
    List<String> list = new ArrayList<>(List.of("a", "b", "c"));
    List<String> traverseItems = new ArrayList<>();

    for (String item : new ArrayList<>(list)) {
      traverseItems.add(item);
      if (item.equals("b")) list.add("x");
    }
    assertEquals("a,b,c", traverseItems.stream().collect(Collectors.joining(",")));

    list = new CopyOnWriteArrayList<>(List.of("a", "b", "c"));
    traverseItems.clear();
    for (String item : list) {
      traverseItems.add(item);
      if (item.equals("b")) list.add("x");
    }
    assertEquals("a,b,c", traverseItems.stream().collect(Collectors.joining(",")));
  }
}
