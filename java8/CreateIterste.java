package java8;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CreateIterste {
    public static void main(String[] args) {
        Stream.iterate(1,n->n=+1)
                .filter(n->n%2 !=0)
                .skip(5)
                .limit(5)
                .forEach(System.out::println);

        IntStream.rangeClosed(1,10)
                .map(n->n*n)
                .forEach(System.out::println);

        Stream.of(1,2,3)
                .flatMap(n->Stream.of(n,n+1))
                .filter(n ->n<3)
                .forEach(System.out::println);

        int sum= Arrays.asList(1,2,3,4,5,6,7,8,9)
                .stream()
                .reduce(0,Integer::sum);
        System.out.println(sum);

        DoubleSummaryStatistics stats =new DoubleSummaryStatistics();
        stats.accept(100.0);
        stats.accept(101.0);
        stats.accept(102.0);
        stats.accept(103.0);
        stats.accept(104.0);
        stats.accept(105.0);
        stats.accept(106.0);
        stats.accept(107.0);
        stats.accept(108.0);

        long count =stats.getCount();
        double summary =stats.getSum();
        double min =stats.getMin();
        double max =stats.getMax();
        double avg =stats.getAverage();

        System.out.printf("count=%d,sum=%.2f,min=%.2f,max=%.2f,average=%.2f",count,summary,min,max,avg);
    }
}
