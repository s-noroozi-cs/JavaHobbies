package com.example;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@SpringBootApplication
@RestController
public class MultipartRestApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(MultipartRestApiApplication.class, args);
  }

  @PostMapping(value = "/upload")
  public ResponseEntity upload(
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam(value = "text", required = false) String Text,
      HttpServletRequest request) {
    return ResponseEntity.ok("");
  }
}
