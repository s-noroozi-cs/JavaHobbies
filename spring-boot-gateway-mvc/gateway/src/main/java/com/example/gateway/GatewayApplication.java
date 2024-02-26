package com.example.gateway;

import com.example.gateway.config.DynamicBeanDefinitionRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class GatewayApplication {

  @Bean
  public static DynamicBeanDefinitionRegistrar dynamicBeanDefinitionRegistrar(
      Environment environment) {
    return new DynamicBeanDefinitionRegistrar(environment);
  }

  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
    }

}
