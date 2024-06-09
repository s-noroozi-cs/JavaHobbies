package com.java.design_pattern.strategy.load_balancer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class InstanceLoadBalancerTests {
    @Test
    void check_invalid_capacity() {
        Executable task = () -> new LoadBalancer(null, -10);
        Assertions.assertThrowsExactly(IllegalArgumentException.class, task);

        task = () -> new LoadBalancer(null, 0);
        Assertions.assertThrowsExactly(IllegalArgumentException.class, task);
    }

    @Test
    void check_invalid_selection_strategy() {
        Executable task = () -> new LoadBalancer(null, 10);
        Assertions.assertThrowsExactly(IllegalArgumentException.class, task);
    }
}
