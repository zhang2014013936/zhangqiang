package java8;

import java.util.function.Consumer;

public class BasicfunctionalInteraface {
    public static void main(String[] args) {
      /*  Consumer<String >consumer =new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };*/
        Consumer<String > consumer =System.out::println;
        Consumer<String > consumer1=System.out::println;
        consumer.andThen(consumer1).accept("bloody java8");

    }
}
