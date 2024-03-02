package com.mvc.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.server.mvc.GatewayServerMvcAutoConfiguration;

@SpringBootApplication(exclude = GatewayServerMvcAutoConfiguration.class)
public class NativeMvcGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(NativeMvcGatewayApplication.class, args);
	}

}
