package others.clases.Errores;

import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class OnErrorReturn {
    public static void main(String[] args) {
        /*
        public Mono<ServerResponse> handleWithErrorReturn(ServerRequest request) {
            return sayHello(request)
                    .onErrorReturn("Hello Stranger")
                    .flatMap(s -> ServerResponse.ok()
                            .contentType(MediaType.TEXT_PLAIN)
                            .bodyValue(s));
        }
        */

        Flux<String> flux = Flux.just("Juan", "Pedro", "", "Maria", "Carlos")
                .map(value -> {
                    if (value.equals("")) {
                        throw new RuntimeException("Error por nombre vacío");
                    }
                    return value;
                })
                .doOnError(e -> {
                    System.out.println("Error generado: " + e.getMessage());
                })
                .onErrorResume(e -> {
                    return Mono.just("No identificado");
                })
                .onErrorReturn("Error general");

        flux.subscribe(
                value -> System.out.println("Valor: " + value),
                error -> System.out.println("Error: " + error),
                () -> System.out.println("Secuencia completada")
        );

    }
}
