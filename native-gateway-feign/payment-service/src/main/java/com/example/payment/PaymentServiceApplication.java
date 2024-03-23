package com.example.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class PaymentServiceApplication {

  @GetMapping("/payments/{id}")
  public ResponseEntity getPayment(@PathVariable("id") String id) {
    java.util.Map payment = new HashMap();
    payment.put("id", id);
    payment.put("createTime", LocalDateTime.now().toString());
    payment.put("amount", BigDecimal.valueOf(100));
    payment.put("traceId", UUID.randomUUID().toString());
    return ResponseEntity.ok(payment);
  }

  public static void main(String[] args) {
    SpringApplication.run(PaymentServiceApplication.class, args);
  }
}
