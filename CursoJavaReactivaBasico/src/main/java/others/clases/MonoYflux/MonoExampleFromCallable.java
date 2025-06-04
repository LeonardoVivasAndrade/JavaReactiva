package others.clases.MonoYflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class MonoExampleFromCallable {

    public static void main(String[] args) {
        SpringApplication.run(MonoExampleFromCallable.class, args);

        Mono<String> mono = Mono.fromCallable(() -> saludar());

        System.out.println("Subscribers con mono fromCallable y dato texto saludo");
        mono.subscribe(System.out::println);
        mono.subscribe(System.out::println);
    }

    private static String saludar() throws InterruptedException {
        Thread.sleep(1000);
        return "Hola mundo, la hora actual es: " + System.currentTimeMillis();

    }
}
