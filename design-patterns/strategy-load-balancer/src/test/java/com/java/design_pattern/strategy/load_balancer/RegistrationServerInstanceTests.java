package com.java.design_pattern.strategy.load_balancer;

import com.java.design_pattern.strategy.load_balancer.selection_impl.RandomSelectionStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RegistrationServerInstanceTests {

  LoadBalancer get_load_balancer_with_zero_capacity() {
    return new LoadBalancer(new RandomSelectionStrategy(), 0);
  }

  @Test
  void check_null_server_instance() {
    Assertions.assertThrowsExactly(
        IllegalArgumentException.class,
        () -> get_load_balancer_with_zero_capacity().register(null));
  }

  @Test
  void check_null_address() {
    Assertions.assertThrowsExactly(
        IllegalArgumentException.class,
        () -> get_load_balancer_with_zero_capacity().register(ServerInstance.builder().build()));
  }

  @Test
  void check_empty_address() {
    Assertions.assertThrowsExactly(
        IllegalArgumentException.class,
        () ->
            get_load_balancer_with_zero_capacity()
                .register(ServerInstance.builder().address("").build()));

    Assertions.assertThrowsExactly(
        IllegalArgumentException.class,
        () ->
            get_load_balancer_with_zero_capacity()
                .register(ServerInstance.builder().address("   ").build()));
  }

  @Test
  void check_zero_capacity() {
    Assertions.assertThrowsExactly(
            IllegalArgumentException.class,
            () ->
                    get_load_balancer_with_zero_capacity()
                            .register(ServerInstance.builder().address("it's ok").build()));
  }
}
