package com.example.demo;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.stereotype.Service;
import reactor.core.observability.micrometer.Micrometer;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;
import java.util.function.Supplier;

@Service
class GreetingService {

    private final Supplier<Long> latency = () -> new Random().nextLong(500);

    private final ObservationRegistry registry;

    GreetingService(ObservationRegistry registry) {
        this.registry = registry;
    }

    public Mono<Greeting> greeting(String name) {
        var lat = latency.get();
        return Mono
                .just(new Greeting(name))
                .delayElement(Duration.ofMillis(lat))
                .name("greeting.call")
                .tag("latency", lat > 250 ? "high" : "low")
                .tap(Micrometer.observation(registry));
    }
}