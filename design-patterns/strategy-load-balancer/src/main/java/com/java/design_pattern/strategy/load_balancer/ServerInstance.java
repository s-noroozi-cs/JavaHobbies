package com.java.design_pattern.strategy.load_balancer;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ServerInstance {
  private String id;
  private String name;
  private String address;
  private long lastSuccessPingTime;
}
