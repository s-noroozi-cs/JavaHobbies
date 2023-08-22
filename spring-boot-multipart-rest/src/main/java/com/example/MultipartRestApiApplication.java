package com.example;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MultipartRestApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(MultipartRestApiApplication.class, args);
  }

  @PostMapping("/upload")
  public ResponseEntity upload(HttpServletRequest request) {
    return ResponseEntity.ok("");
  }
}
