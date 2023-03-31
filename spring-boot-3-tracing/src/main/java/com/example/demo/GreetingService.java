package com.example.demo;

import io.micrometer.observation.ObservationRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.observability.micrometer.Micrometer;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
@Slf4j
class GreetingService {
    private final Supplier<Long> latency = () -> new Random().nextLong(500);
    private final ObservationRegistry registry;

    public Mono<Greeting> greeting(String name) {
        var lat = latency.get();
        log.info("greeting with latency <{}>", lat);

        return Mono
                .just(new Greeting(name))
                .delayElement(Duration.ofMillis(lat))
                .name("greeting.call")
                .tag("latency", lat > 250 ? "high" : "low")
                .tap(Micrometer.observation(registry));
    }
}