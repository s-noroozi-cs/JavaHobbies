package com.mvc.gateway;

import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.path;
import static org.springframework.web.servlet.function.RouterFunctions.route;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.server.mvc.GatewayServerMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@SpringBootApplication(exclude = GatewayServerMvcAutoConfiguration.class)
public class NativeMvcGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(NativeMvcGatewayApplication.class, args);
  }

  @Bean
  public RouterFunction<ServerResponse> getRoute() {
    return route().route(path("/uuid"), http("http://httpbin.org/uuid")).build();
  }
}
