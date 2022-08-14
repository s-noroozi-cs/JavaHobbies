package com.hobi.reactive.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

    @GetMapping
    Mono<String> sayHello() {
        return Mono.just("Hello World!");
    }

    @GetMapping("/{by-name}")
    Mono<String> sayHello(@PathVariable("by-name") String name){
        return Mono.just("Hello " + name);
    }
}
