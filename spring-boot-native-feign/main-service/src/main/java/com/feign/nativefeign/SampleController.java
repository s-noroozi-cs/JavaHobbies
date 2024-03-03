package com.feign.nativefeign;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

  private final ChildServiceClient httpBinClient;

  public SampleController(ChildServiceClient httpBinClient) {
    this.httpBinClient = httpBinClient;
  }

  @GetMapping("/uuid")
  String uuid() {
    return httpBinClient.uuid();
  }
}
