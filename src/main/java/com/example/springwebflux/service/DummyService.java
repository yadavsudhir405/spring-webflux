package com.example.springwebflux.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;

@Service
public class DummyService {
    public Flux<Integer> tenToZero() {
        return Flux.range(0,11)
                .map(i -> 10- i);
    }

    public Flux<String> reandomizedNames() {
        return Flux.fromIterable(Arrays.asList("Foo", "Bar","Jazz"))
                .delayElements(Duration.ofSeconds(1));
    }
}
