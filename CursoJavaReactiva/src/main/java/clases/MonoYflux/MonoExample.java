package clases.MonoYflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootApplication
public class MonoExample {

    public static void main(String[] args) {
        SpringApplication.run(MonoExample.class, args);

        //Ejemplo 2 - StepVerifier
        Mono<String> helloMono = Mono.just("Hello");
        StepVerifier.create(helloMono)
                .expectNext("Hello")
                .expectComplete()
                .verify();

        //Ejemplo 2 - Suscripcion al mono
        Mono<String> mono = Mono.just("Hola, Mundo!");
        // Suscribirse al Mono y procesar el elemento emitido
        mono.subscribe(
                element -> System.out.println("Elemento recibido: " + element),
                error -> System.err.println("Error: " + error.getMessage()),
                () -> System.out.println("Secuencia completada")
        );

        //Ejemplo 3 - Varios Suscriptores al mono
        Mono<String> mono2 = Mono.just("Hola, Mundo!");
        // Suscribirse al Mono y procesar el elemento emitido
        mono2.subscribe(
                element -> System.out.println("Elemento reverse: " + new StringBuilder(element).reverse()),
                error -> System.err.println("Error: " + error.getMessage()),
                () -> System.out.println("Secuencia completada")
        );
        mono2.subscribe(
                element -> System.out.println("Elemento replace: " + new StringBuilder(element).replace(0,4,"AAAA")),
                error -> System.err.println("Error: " + error.getMessage()),
                () -> System.out.println("Secuencia completada")
        );
        mono2.subscribe(
                element -> System.out.println("Elemento elimina primeros 4 char: " +  new StringBuilder(element).delete(0,4)),
                error -> System.err.println("Error: " + error.getMessage()),
                () -> System.out.println("Secuencia completada")
        );
    }
}
