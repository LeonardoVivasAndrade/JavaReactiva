package com.example.webfluxintro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

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

    @GetMapping("/saludo/{nombre}")

    public Mono<String> saludoPersonalizado(@PathVariable String nombre) {
        return Mono.just(nombre)
                .map(String::toUpperCase) // Transforma el nombre a mayúsculas
                .map(nombreEnMayusculas -> "¡Hola, " + nombreEnMayusculas + "! BIENVENIDO A WEBFLUX.");
//        return Mono.just("¡Hola, " + nombre + "! Bienvenido a WebFlux.");
    }

    @GetMapping("/mensaje")
    public Mono<String> enviarMensaje(@RequestParam(defaultValue = "Invitado") String para,
                                      @RequestParam(required = false) String de) {
        String remitente = (de != null) ? de : "Sistema";
        return Mono.just("Mensaje para: " + para + ". De: " + remitente);
    }

    @GetMapping("/numeros")
    public Flux<Integer> obtenerNumeros() {
        return Flux.just(1, 2, 3, 4, 5)
                .delayElements(Duration.ofSeconds(1)) // Emite un elemento cada segundo
                .log(); // Añade logging para ver qué sucede en el flujo
    }
}
