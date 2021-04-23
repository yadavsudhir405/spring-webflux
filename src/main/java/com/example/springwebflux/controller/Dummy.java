package com.example.springwebflux.controller;

import com.example.springwebflux.service.DummyService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
}
