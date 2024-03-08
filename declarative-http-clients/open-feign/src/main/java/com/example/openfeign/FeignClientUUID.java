package com.example.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "uuid-service")
public interface FeignClientUUID {
    @GetMapping("/uuid")
    String uuidGenerator();
}
