package com.example.aot;

import java.time.LocalDateTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AotApplication {

  public static void main(String[] args) {
    SpringApplication.run(AotApplication.class, args);
  }

  @RequestMapping(method = RequestMethod.GET, path = "/date")
  public ResponseEntity<String> showServerDate() {
    return ResponseEntity.ok(LocalDateTime.now().toString());
  }
}
