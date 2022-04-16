package com.hobi.proxyaspect.rest;

import com.hobi.proxyaspect.annotation.Authorization;
import com.hobi.proxyaspect.model.SayHelloModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class MyRestController {

    @GetMapping(value = "/uuid")
    @Authorization
    public ResponseEntity<String> generateUUID(){
        return ResponseEntity.ok(UUID.randomUUID().toString());
    }

    @PostMapping(value = "/sayHello")
    @Authorization
    public ResponseEntity<String> sayHello(@Validated @RequestBody SayHelloModel model){
        String respText =String.format("Hello %s, Your age is %d",model.getName(),model.getAge());
        return ResponseEntity.ok(respText);
    }
}
