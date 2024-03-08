package com.example.client;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@LoadBalancerClient(name = "uuid-service")
public class ServiceClientConfig {

    @LoadBalanced
    @Bean
    WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    UuidGeneratorClient uuidGeneratorClient(WebClient.Builder builder) throws Exception {
        HttpServiceProxyFactory httpServiceProxyFactory =
                HttpServiceProxyFactory.builder()
                        .exchangeAdapter(
                                WebClientAdapter.create(builder.baseUrl("http://uuid-service").build()))
                        .build();
        return httpServiceProxyFactory.createClient(UuidGeneratorClient.class);
    }
}
