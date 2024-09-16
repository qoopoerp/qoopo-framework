package net.qoopo.qoopoframework.models;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.Setter;

/**
 * Clase que representa un nodo de una estructura de arbol.
 * 
 * La idea es poder generar una estructura de datos que represente cualquier
 * entidad y sus campos y que s epueda agrupar por cualquier campo
 * 
 * Esta clase esta pendiente de terminar y funcionar
 */
@Getter
@Setter
public class Nodo {

    private Map<Object, Object> values;

    public static void main(String[] args) {
        Stream<Person> people = Stream.of(new Person("Paul", 24, 20000),
                new Person("Mark", 30, 30000),
                new Person("Will", 28, 28000),
                new Person("William", 28, 28000));
        Map<Integer, List<Person>> peopleByAge = people
                .collect(Collectors.groupingBy(p -> p.age, Collectors.mapping((Person p) -> p, Collectors.toList())));
        System.out.println(peopleByAge);

        Map<Object, Map<Object, Long>> multipleFieldsMap = people
                .collect(Collectors.groupingBy(c -> c.age, Collectors.groupingBy(p -> p.name, Collectors.counting())));

        // printing the count based on the designation and gender.
        System.out.println("Group by on multiple properties" + multipleFieldsMap);
    }

    static class Person {

        private String name;
        private int age;
        private long salary;

        Person(String name, int age, long salary) {
            this.name = name;
            this.age = age;
            this.salary = salary;
        }

        @Override
        public String toString() {
            return String.format("Person{name='%s', age=%d, salary=%d}", name, age, salary);
        }
    }
}
