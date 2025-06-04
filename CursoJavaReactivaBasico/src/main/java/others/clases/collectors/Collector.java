package others.clases.collectors;

import others.actividades.actividad1.Persona;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Collector {
    public static void main(String[] args) {
        Persona angel = new Persona("Angel","Vivas");
        Persona leonardo = new Persona("Leonardo","Andrade");
        Persona laksmi = new Persona("Laksmi","Salazar");
        List<Persona> people = Arrays.asList(angel, leonardo, laksmi);

        // Accumulate names into a List
        List<String> list = people.stream().map(Persona::getNombre).collect(Collectors.toList());
        list.forEach(System.out::println);

        // Accumulate names into a TreeSet
//        Set<String> set = people.stream().map(Person::getName).collect(Collectors.toCollection(TreeSet::new));
//
//        // Convert elements to strings and concatenate them, separated by commas
//        String joined = things.stream()
//                .map(Object::toString)
//                .collect(Collectors.joining(", "));
//
//        // Compute sum of salaries of employee
//        int total = employees.stream()
//                .collect(Collectors.summingInt(Employee::getSalary)));
//
//        // Group employees by department
//        Map<Department, List<Employee>> byDept
//                = employees.stream()
//                .collect(Collectors.groupingBy(Employee::getDepartment));
    }
}
