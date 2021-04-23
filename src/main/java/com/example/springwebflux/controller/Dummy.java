package com.example.springwebflux.controller;

import com.example.springwebflux.service.DummyService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@RestController
@RequestMapping("/api")
public class Dummy {

    private final DummyService dummyService;

    public Dummy(DummyService dummyService) {
        this.dummyService = dummyService;
    }

    @GetMapping("/numbers")
    public Flux<Integer> numbers(@RequestParam(required = false) String operation) {
        if (operation == null)
            return this.dummyService.tenToZero();
        switch (operation) {
            case "divide":
                return this.dummyService.tenToZero()
                        .map(i -> 100 / i)
                        .onErrorReturn(ArithmeticException.class, 0);
            case "multiply":
                return this.dummyService.tenToZero()
                        .map(i -> 100 * i)
                        .take(4);
            default:
                throw new UnsupportedOperationException("Unsupported");
        }
    }

    @GetMapping(value = "/names", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> names() {
        return this.dummyService.reandomizedNames();
    }

    @Bean
    public HandlerFunction<ServerResponse> names1Handler() {
        return request -> {
            final Flux<Integer> integerFlux = this.dummyService.tenToZero();
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(integerFlux, Integer.class);
        };
    }

    @Bean
    public RouterFunction<ServerResponse> namesRouter(HandlerFunction<ServerResponse> names1Handler) {
        return RouterFunctions.route().GET(
                "/api/names1",
                accept(MediaType.TEXT_PLAIN),
                names1Handler
        ).build();
    }
}
