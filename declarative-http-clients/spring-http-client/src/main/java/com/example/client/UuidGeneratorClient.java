package com.example.client;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

public interface UuidGeneratorClient {
  @GetExchange("/uuid")
  Mono<String> generateUUID();
}
