package com.example.webfluxintro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HolaController {

    @GetMapping("/hola")
    public Mono<String> saludar() {
        return Mono.just("¡Hola Mundo con WebFlux!");
    }

    @GetMapping("/hola-reactor")
    public Mono<String> saludarReactor() {
        return Mono.just("¡Saludos desde el mundo reactivo de Project Reactor!");
    }
}
