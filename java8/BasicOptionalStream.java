package java8;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class BasicOptionalStream {
    public static void main(String[] args) {
        OptionalInt maxodd= IntStream.of(10,20,30,40,43,55,61)
                .filter(n->n%2==1)
                .max();
       /* if(maxodd.isPresent()){
            System.out.println(maxodd.getAsInt());
        }else {
            System.out.println("not exists");
        }*/
       System.out.println(maxodd.orElse(-1));
    }

}
