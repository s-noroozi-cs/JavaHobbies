package com.example.gateway;

import jakarta.annotation.PostConstruct;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    @Autowired
    private ConfigurableBeanFactory context;
    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    public GroupedOpenApi gatewayGroup() {
        for (String name : discoveryClient.getServices()) {
            GroupedOpenApi bean = GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build();
            context.registerSingleton("groupedOpenApi_" + name, bean);
        }

        return GroupedOpenApi.builder().pathsToMatch("/**").group("Gateway").build();
    }


    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
