package java8;

import java.util.Optional;

public class BasicOptional {
    public static void main(String[] args) {
        Optional<String > empty =Optional.empty();
        System.out.println(empty);

        String myname=null;
        Optional<String >str =Optional.ofNullable(myname);
        System.out.println(str.orElse("zhangqiang"));
    }
}
