package com.hobi.proxyaspect.rest;

import com.hobi.proxyaspect.annotation.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class MyRestController {

    @GetMapping
    @Authorization
    public ResponseEntity<String> generateUUID(){
        return ResponseEntity.ok(UUID.randomUUID().toString());
    }
}
