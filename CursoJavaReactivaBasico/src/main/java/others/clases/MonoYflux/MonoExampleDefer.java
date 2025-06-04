package others.clases.MonoYflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Random;

@SpringBootApplication
public class MonoExampleDefer {

    public static void main(String[] args) {
        SpringApplication.run(MonoExampleDefer.class, args);

        // Crear un Mono que emite el valor actual del sistema cuando se suscribe
        Mono<String> deferMono = Mono.defer(() -> Mono.just(getCurrentTime()))
                .subscribeOn(Schedulers.parallel());

        // Suscribirse al Mono y procesar el valor emitido
        deferMono.subscribe(System.out::println);

        // Suscribirse nuevamente al Mono y procesar el valor emitido
        deferMono.subscribe(System.out::println);


        // Ejemplo defer con random
        Mono<Integer> deferMono2 = Mono.defer(() -> Mono.just(new Random().nextInt(100)+1))
                .subscribeOn(Schedulers.parallel());

        System.out.println("Subscribers con mono defer y dato entero random");
        deferMono2.subscribe(System.out::println);
        deferMono2.subscribe(System.out::println);
    }

    private static String getCurrentTime() {
        return "Hora actual: " + System.currentTimeMillis();
    }
}
