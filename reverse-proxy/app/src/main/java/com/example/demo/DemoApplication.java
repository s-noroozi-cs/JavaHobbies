package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		System.setProperty("javax.net.debug", "ssl,handshake");

		SpringApplication.run(DemoApplication.class, args);
	}

}
