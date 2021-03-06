package pf.herve.gamerv;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author chahd
 */
//https://blog.ippon.fr/2014/03/18/java-8-interfaces-fonctionnelles/
public class TestDoubleColon {

    public class Name {

        private String firstName;
        private String lastName;

        public Name(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

    }

    public class NameParser<T> {

        /*public T parse(String name, Creator creator) {
            String[] tokens = name.split(" ");
            String firstName = tokens[0];
            String lastName = tokens[1];
            return (T) creator.create(firstName, lastName);
        }*/
        
        public T parse(String name, Creator creator) {
            String[] tokens = name.split(" ");
            String firstName = tokens[0];
            String lastName = tokens[1];
            return (T) creator.create(firstName, lastName);
        }
    }
    @FunctionalInterface
    public interface Creator<T> {

        T create(String firstName, String lastName);
    }
    
    public <T> void doSomething(Supplier<T> function, Consumer<T> onSuccess, Consumer<Exception> onError) {
        try {
            T res = function.get();
            onSuccess.accept(res);
        } catch (Exception ex) {
            onError.accept(ex);
        }
     }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestDoubleColon programm = new TestDoubleColon();
        programm.start();
    }

    public void start() {
        NameParser<Name> parser = new NameParser();
        
        Name res = parser.parse("Eric Clapton", new Creator<Name>() {
            @Override
            public Name create(String firstName, String lastName) {
                return new Name(firstName, lastName);
            }
        });
        
        System.out.println(res.getLastName()+" "+res.getFirstName());
        
        Name res1 = parser.parse("John Lennon", Name::new);
        System.out.println(res1.getLastName()+" "+res1.getFirstName());
        Consumer<String> afficher = (param) -> System.out.println(param);
    
        doSomething(
        () -> 42,
        System.out::println,
        ex -> System.err.println("Error: " + ex.getMessage()));

        //In Java 8, Consumer is a functional interface; it takes an argument and returns nothing.
        Consumer<String> print = x -> System.out.println(x);
        print.accept("java");   // java
        /*
        4. Functions
        The most simple and general case of a lambda is a functional interface with a method that receives one value and returns another. This function of a single argument is represented by the Function interface which is parameterized by the types of its argument and a return value:
        public interface Function<T, R> { … }*/

        Map<String, Integer> nameMap = new HashMap<>();
        //nameMap.put("John",2);
        Integer value = nameMap.computeIfAbsent("John", s -> s.length());
        System.out.println(value);
    }

}
