package com.example.springwebflux.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class WebClientExample {
    private final WebClient swapiClinet =
            WebClient.create("https://apps.yadavsudhir.com");

    @GetMapping("peoples")
    private Mono<Object> findCharacters() {
        return swapiClinet
                .get()
                .uri("/rest/api/v1/profile")
                .retrieve()
                .bodyToMono(Object.class);

    }
}
