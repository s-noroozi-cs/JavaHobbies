package com.example.asyncservlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;

@SpringBootApplication
public class AsyncServletApplication {



	public static void main(String[] args) {
		SpringApplication.run(AsyncServletApplication.class, args);
	}

}
