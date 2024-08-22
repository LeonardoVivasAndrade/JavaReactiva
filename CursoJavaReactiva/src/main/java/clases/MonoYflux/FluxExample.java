package clases.MonoYflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootApplication
public class FluxExample {

    public static void main(String[] args) {
        SpringApplication.run(FluxExample.class, args);

        //Ejemplo 2 - StepVerifier
        Flux<String> stringFlux = Flux.just("Hello", "Baeldung");
        StepVerifier.create(stringFlux)
                .expectNext("Hello")
                .expectNext("Baeldung")
                .expectComplete()
                .verify();

        //Ejemplo 2 - Suscripcion al mono
        Flux<String> fluxx = Flux.just("Hola", "Mundo","!");
        // Suscribirse al Flux y procesar los elementos emitidos
        fluxx.subscribe(
                element -> System.out.println("Elemento recibido: " + element),
                error -> System.err.println("Error: " + error.getMessage()),
                () -> System.out.println("Secuencia completada")
        );

        //Ejemplo 3 - Varios sucribers
        Flux<String> flux = Flux.just("60000", "25000","50000");
        flux.subscribe(
                element -> {
                    String result = Integer.parseInt(element) > 50000 ? " Se aplica dto 10%" :
                            Integer.parseInt(element) < 50000 ? " Se aplica dto 5%" : " No se aplica descuento";
                    System.out.println("Subscriber 1: Para: " + element + result);
                },
                error -> System.err.println("Error: " + error.getMessage()),
                () -> System.out.println("Secuencia completada")
        );
        flux.subscribe(
                element -> {
                    System.out.println("Subcriber 2: Elemento duplicado: " +  Integer.parseInt(element) * 2);
                },
                error -> System.err.println("Error: " + error.getMessage()),
                () -> System.out.println("Secuencia completada")
        );
        flux.subscribe(
                element -> System.out.println("Subscriber 3: Elemento dividido en 2: " + Integer.parseInt(element) / 2),
                error -> System.err.println("Error: " + error.getMessage()),
                () -> System.out.println("Secuencia completada")
        );

    }
}
