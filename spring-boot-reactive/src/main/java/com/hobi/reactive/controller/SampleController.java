package com.hobi.reactive.controller;

import com.hobi.reactive.model.CalcRequestModel;
import com.hobi.reactive.model.CalcResponseModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class SampleController {

    @GetMapping("/say-hello")
    Mono<String> sayHello(@RequestParam(value = "name",required = false) String name) {
        String result = "Hello " + (name == null ? "World!" : name);
        return Mono.just(result);
    }

    @PostMapping(value = "/calc"
            ,produces = MediaType.APPLICATION_JSON_VALUE
            ,consumes = MediaType.APPLICATION_JSON_VALUE)
    Mono<CalcResponseModel> calc(@RequestBody CalcRequestModel request){
        return Mono.just(new CalcResponseModel());
    }
}
