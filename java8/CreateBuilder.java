package java8;

import java.util.stream.Stream;

public class CreateBuilder {
    public static void main(String[] args) {
        Stream.<String >builder().add("a").add("b").add("c").add("d").build().forEach(System.out::println);



    }
}
