package clases.collectors;

import java.util.Arrays;
import java.util.List;

public class Sorter {
    public static void main(String[] args) {
        List<String> nombres = Arrays.asList("Juan", "Ana", "Carlos", "María");

        nombres.stream()
                .sorted() // Ordena en orden natural (alfabético en este caso)
                .forEach(System.out::println);


    }
}
