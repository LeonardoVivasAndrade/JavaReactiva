package others.clases.MonoYflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

import java.util.Random;

@SpringBootApplication
public class MonoExampleFromSupplier {

    public static void main(String[] args) {
        SpringApplication.run(MonoExampleFromSupplier.class, args);

        Mono<Integer> mono = Mono.fromSupplier(() -> {
                    int randomValue = new Random().nextInt(100) + 1;
                    return randomValue;
                });

        System.out.println("Subscribers con mono fromSupplier y dato entero random");
        mono.subscribe(System.out::println);
        mono.subscribe(System.out::println);
    }
}
