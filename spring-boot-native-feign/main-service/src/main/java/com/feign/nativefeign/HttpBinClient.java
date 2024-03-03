package com.feign.nativefeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "http-bin-feign-client", url = "http://httpbin.org")
public interface HttpBinClient {
  @GetMapping("/uuid")
  String uuid();
}
