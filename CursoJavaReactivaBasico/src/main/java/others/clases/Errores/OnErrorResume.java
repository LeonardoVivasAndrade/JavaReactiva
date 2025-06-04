package others.clases.Errores;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class OnErrorResume {
    public static void main(String[] args) {
        //Si hay un error, se sigue emitiendo datos
        Mono<Integer> source = Mono.just("error")
                .map(Integer::parseInt)
                .onErrorResume(error -> {
                    System.out.println("Error occurred: " + error.getMessage());
                    return Mono.just(0); // Proporcionar un valor alternativo en caso de error
                });

        source.subscribe(System.out::println);

        Flux<String> errorFlux = Flux.error(new RuntimeException("Simulated error"));

        //Consultar
//        .onErrorStop()
//        .onErrorComplete();

    }
}
