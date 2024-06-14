package com.java.design_pattern.strategy.load_balancer.selection_impl;

import com.java.design_pattern.strategy.load_balancer.SelectionStrategy;
import com.java.design_pattern.strategy.load_balancer.ServerInstance;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinSelectionStrategy implements SelectionStrategy {
  private final AtomicInteger counter = new AtomicInteger(0);

  @Override
  public Optional<ServerInstance> select(Map<String, ServerInstance> instances) {
    if (instances == null || instances.size() == 0) return Optional.empty();
    List<String> listOfAddresses = instances.keySet().stream().toList();
    int index = counter.getAndIncrement() % listOfAddresses.size();
    return Optional.of(instances.get(listOfAddresses.get(index)));
  }
}
