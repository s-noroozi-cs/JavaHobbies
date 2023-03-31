package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@Slf4j
class GreetingController {
    private final GreetingService service;

    @GetMapping("/hello/{name}")
    public Mono<Greeting> greeting(@PathVariable("name") String name) {
        log.info("Got a request {}",name);
        return service.greeting(name);
    }
}