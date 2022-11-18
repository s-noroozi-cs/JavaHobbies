package com.example.demo;

import com.ecwid.consul.v1.ConsulRawClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.consul.ConsulAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.function.Supplier;

@SpringBootApplication
@EnableDiscoveryClient
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public Supplier<Long> numberProducer() {
        Class c = ConsulAutoConfiguration.class;
        return () -> System.currentTimeMillis();
    }

    @Bean
    public Supplier<ConsulRawClient.Builder> consulRawClientBuilderSupplier() {
        return ConsulAutoConfiguration.createConsulRawClientBuilder();
    }

}
