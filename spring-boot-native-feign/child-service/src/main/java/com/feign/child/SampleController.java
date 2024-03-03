package com.feign.child;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class SampleController {

  @GetMapping("/uuid")
  String uuid() {
    return UUID.randomUUID().toString();
  }
}
