package com.example.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class SpringHttpClientApplication {
  @Autowired UuidGeneratorClient uuidGeneratorClient;

  @GetMapping("web-client/uuid")
  public Mono<String> uuid() {
    return uuidGeneratorClient.generateUUID();
  }

	public static void main(String[] args) {
		SpringApplication.run(SpringHttpClientApplication.class, args);
	}

}
