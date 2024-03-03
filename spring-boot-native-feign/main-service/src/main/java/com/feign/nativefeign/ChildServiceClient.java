package com.feign.nativefeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "child-service")
public interface ChildServiceClient {
  @GetMapping("/uuid")
  String uuid();
}
