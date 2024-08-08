package clases.funciones_puras;

import java.util.List;

public class PureFunctionExample {

    public static int sum(List<Integer> numbers){
        int total = 0;
        for (int num : numbers) {
            total += num;
        }
        return total;
    }

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1,2,3,4,5);
        int resutl = sum(numbers);
        System.out.println("Sum: " + resutl);
    }
}
