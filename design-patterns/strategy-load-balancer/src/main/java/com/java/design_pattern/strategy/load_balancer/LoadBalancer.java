package com.java.design_pattern.strategy.load_balancer;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class LoadBalancer {
  private final SelectionStrategy selectionStrategy;
  private final int capacity;
  private final Map<String, ServerInstance> instances = new ConcurrentHashMap<>();

  public LoadBalancer(SelectionStrategy selectionStrategy, int capacity) {
    if (selectionStrategy == null)
      throw new IllegalArgumentException("Selection strategy is mandatory.");

    if (capacity <= 0) throw new IllegalArgumentException("Capacity is not valid.");

    this.selectionStrategy = selectionStrategy;
    this.capacity = capacity;
  }

  public void register(ServerInstance instance) {
    if (instance == null) throw new IllegalArgumentException("null instance");

    if (instance.getAddress() == null) throw new IllegalArgumentException("null address");

    if (instance.getAddress().trim().isEmpty()) throw new IllegalArgumentException("empty address");

    if (instances.size() >= capacity) {
      throw new IllegalCallerException("Capacity Exceeded");
    }
    instances.put(instance.getAddress(), instance);
  }

  public Optional<ServerInstance> get() {
    return selectionStrategy.select(instances);
  }
}
