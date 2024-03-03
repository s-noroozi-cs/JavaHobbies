package com.feign.nativefeign;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@LoadBalancerClient(value = "child-service")
public class LoadBalanceConfig {}
