package com.example.uuid;

import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class UuidServiceApplication {

  @GetMapping("/uuid")
  public String uuidGenerator() {
    return UUID.randomUUID().toString();
  }

	public static void main(String[] args) {
		SpringApplication.run(UuidServiceApplication.class, args);
	}

}
