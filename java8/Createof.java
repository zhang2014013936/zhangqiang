package java8;

import java.util.Arrays;
import java.util.stream.Stream;

public class Createof {
    public static void main(String[] args) {
        Stream<String> stream = Stream.of("zhangqiang");
        stream.forEach(s -> System.out.println(s + " "));
        stream.forEach(System.out::println);
        Stream<String> stream1 = Stream.of("zhangqiang","t","r","t");
        stream1.forEach(s->System.out.println("<<"+s+">>"));

        String[] names={"a","b","c","d"};
        Stream<String >stream2 =Stream.of(names);
        stream2.forEach(System.out::println);

        Stream<String > stream3 = Arrays.asList(names).stream();
        stream3.forEach(System.out::println);


    }
}
