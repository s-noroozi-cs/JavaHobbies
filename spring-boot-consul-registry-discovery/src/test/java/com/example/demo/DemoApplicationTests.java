package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${spring.application.name}")
    private String appName;

    @Test
    void contextLoads() throws Exception {
        List<ServiceInstance> instances = null;
        do {
            Thread.sleep(100);
            System.out.println("check if service registered ...");
            instances = discoveryClient.getInstances(appName);
        } while (instances.size() == 0);
        Assertions.assertNotNull(instances);
        Assertions.assertTrue(instances.size() > 0);

    }

}
