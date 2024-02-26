package com.example.gateway.config;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

public class DynamicBeanDefinitionRegistrar implements BeanDefinitionRegistryPostProcessor {
  private final List<String> serviceNames;

  public DynamicBeanDefinitionRegistrar(Environment environment) {
    serviceNames =
        Binder.get(environment)
            .bind("dynamic.route.service.names", Bindable.listOf(String.class))
            .orElseGet(() -> Collections.EMPTY_LIST);
  }

  private Supplier<RouterFunction<ServerResponse>> makeRouteSupplier(String serviceName) {
    return () ->
        route(serviceName)
            .GET("/" + serviceName + "/**", http())
            .before(rewritePath("/" + serviceName + "/(?<segment>.*)", "/${segment}"))
            .filter(lb(serviceName))
            .build();
  }

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
      throws BeansException {
    serviceNames.forEach(
        serviceName -> {
          GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
          beanDefinition.setBeanClass(RouterFunction.class);
          beanDefinition.setInstanceSupplier(makeRouteSupplier(serviceName));
          registry.registerBeanDefinition(serviceName + "RouterFunction", beanDefinition);
        });
  }
}
