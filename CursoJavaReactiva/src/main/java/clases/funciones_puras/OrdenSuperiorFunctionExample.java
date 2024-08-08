package clases.funciones_puras;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OrdenSuperiorFunctionExample {

    public static <T, R> List<R> aplicarFuncionALaLista(List<T> lista, Function<T, R> funcion){
        List<R> resultado = new ArrayList<>();
        for (T elemento : lista) {
            resultado.add(funcion.apply(elemento));
        }
        return resultado;
    }

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1,2,3,4,5);

        //Usar una funcion lamda como parámetro
        List<Integer> cuadrados = aplicarFuncionALaLista(numbers, x -> x * x);
        System.out.println("Resultado: " + cuadrados);
    }
}
