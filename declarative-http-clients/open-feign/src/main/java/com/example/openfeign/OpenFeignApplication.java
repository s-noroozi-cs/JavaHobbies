package com.example.openfeign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@RestController
public class OpenFeignApplication {
  @Autowired FeignClientUUID feignClientUUID;

  @GetMapping("open-feign/uuid")
  public String openFeignUUID() {
    return feignClientUUID.uuidGenerator();
  }

	public static void main(String[] args) {
		SpringApplication.run(OpenFeignApplication.class, args);
	}

}
