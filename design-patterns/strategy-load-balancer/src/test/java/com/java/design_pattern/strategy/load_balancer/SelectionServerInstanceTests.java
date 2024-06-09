package com.java.design_pattern.strategy.load_balancer;

import com.java.design_pattern.strategy.load_balancer.selection_impl.RandomSelectionStrategy;
import com.java.design_pattern.strategy.load_balancer.selection_impl.RoundRobinSelectionStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SelectionServerInstanceTests {
  final int CAPACITY = 10;
  final String X_ADR = "x";
  final String Y_ADR = "y";
  final String Z_ADR = "z";

  LoadBalancer get_load_balancer(SelectionStrategy selectionStrategy) {
    LoadBalancer lb = new LoadBalancer(selectionStrategy, CAPACITY);
    lb.register(ServerInstance.builder().address(X_ADR).build());
    lb.register(ServerInstance.builder().address(Y_ADR).build());
    lb.register(ServerInstance.builder().address(Z_ADR).build());
    return lb;
  }

  @Test
  void check_random_server_instance() {
    LoadBalancer lb = get_load_balancer(new RandomSelectionStrategy());
    int counterX = 0, counterY = 0, counterZ = 0;
    for (int i = 0; i < 100; i++) {
      switch (lb.get().get().getAddress()) {
        case "x":
          counterX++;
          break;
        case "y":
          counterY++;
          break;
        case "z":
          counterZ++;
          break;
        default:
          throw new IllegalStateException("there is invalid server instance.");
      }
    }
    Assertions.assertTrue(counterX > 15);
    Assertions.assertTrue(counterY > 15);
    Assertions.assertTrue(counterZ > 15);
  }

  @Test
  void check_round_robin_server_instance() {
    LoadBalancer lb = get_load_balancer(new RoundRobinSelectionStrategy());
    int counterX = 0, counterY = 0, counterZ = 0;
    final int SELECT_COUNT = 99;
    for (int i = 0; i < SELECT_COUNT; i++) {
      switch (lb.get().get().getAddress()) {
        case "x":
          counterX++;
          break;
        case "y":
          counterY++;
          break;
        case "z":
          counterZ++;
          break;
        default:
          throw new IllegalStateException("there is invalid server instance.");
      }
    }
    Assertions.assertEquals(SELECT_COUNT / 3, counterX);
    Assertions.assertEquals(SELECT_COUNT / 3, counterX);
    Assertions.assertEquals(SELECT_COUNT / 3, counterX);
    }
}
