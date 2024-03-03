package com.feign.nativefeign;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

  private final HttpBinClient httpBinClient;

  public SampleController(HttpBinClient httpBinClient) {
    this.httpBinClient = httpBinClient;
  }

  @GetMapping("/uuid")
  String uuid() {
    return httpBinClient.uuid();
  }
}
