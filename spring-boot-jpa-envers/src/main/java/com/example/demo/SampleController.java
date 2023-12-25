package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
  @Autowired private SampleRepo sampleRepo;

  @GetMapping("/test")
  public String test() {
    sampleRepo.findLastChangeRevision(1L);
    return "ok";
  }
}
