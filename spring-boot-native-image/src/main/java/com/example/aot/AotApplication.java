package com.example.aot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AotApplication {

    public static void main(String[] args) {
        SpringApplication.run(AotApplication.class, args);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/sayHello/{name}")
    public ResponseEntity<String> sayHello(@PathVariable("name") String name) {
        return ResponseEntity.ok("Hello, %s".formatted(name));
    }
}
