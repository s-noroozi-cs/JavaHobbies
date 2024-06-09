package com.java.design_pattern.strategy.load_balancer;

import java.util.Map;
import java.util.Optional;

public interface SelectionStrategy {
  Optional<ServerInstance> select(Map<String, ServerInstance> instances);
}
