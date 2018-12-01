package folkandjoin;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

class QuickSort{
    private static int partition(Long[] numbers,int low,int high){
        Long soldier =numbers[low];
        while (low<high){
            while (low<high){
                if(numbers[high]<soldier){
                    numbers[low]=numbers[high];
                    break;
                }
                high--;
            }
            while (low<high){
                if(numbers[low]>soldier){
                    numbers[high]=numbers[low];
                    break;
                }
                low++;
            }

        }
        numbers[low]=soldier;
        return low;
    }

    public static void asort(Long [] numbers, int low,int high) {
        if(low<high){
            int solder =partition(numbers,low,high);
                asort(numbers,low,solder-1);
                asort(numbers,solder+1,high);
            }
        }

    }
    class ParalleQuickSort extends RecursiveAction{
    private int low;
    private int high;
    private  Long[]numbers;
    private int threadhold =30;


    ParalleQuickSort(int threadhold,  Long[] numbers) {
            this.threadhold = threadhold;
            this.numbers = numbers;
            this.high = numbers.length-1;
            this.low=low;
        }
        private  ParalleQuickSort(int low,int high,Long[]numbers){
            this.low=low;
            this.high=high;
            this.numbers=numbers;
        }

        private static int partition(Long[] numbers,int low,int high) {
            Long soldier = numbers[low];
            while (low < high) {
                while (low < high) {
                    if (numbers[high] < soldier) {
                        numbers[low] = numbers[high];
                        break;
                    }
                    high--;
                }
                while (low < high) {
                    if (numbers[low] > soldier) {
                        numbers[high] = numbers[low];
                        break;
                    }
                    low++;
                }

            }
            numbers[low] = soldier;
            return low;
        }


        @Override
        protected void compute() {

            if(high-low<threadhold){
                QuickSort.asort(numbers,low,high);
            }else{
                int soldier = partition(numbers,low,high);
                ParalleQuickSort left =new ParalleQuickSort(low,soldier-1,numbers);
                ParalleQuickSort right=new ParalleQuickSort(soldier+1,high,numbers);
                invokeAll(left,right);
            }


        }
    }

public class MainSort {
    private static Long[] generateRamdomNumbers(int n) {
        Random random = new Random();
        Long[] numbers = new Long[n];
        for (int i = 0; i < n; i++) {
            Long num = random.nextLong();
            if (num < 0) {
                num = Math.abs(num);
            }
            numbers[i] = num;
        }
        return numbers;
    }

    public static void main(String[] args) {
        final int THRESHOLD=36200;
        final int RUN_TIMS=10;
        final int SORT_NUM=10000000;
        Long StartTime;
        for(int  i=0;i<RUN_TIMS+1;i++){
            System.out.println("The"+i+"run");
            System.out.println("Generating"+SORT_NUM+"numbers");
            Long[] qsNumbers =generateRamdomNumbers(SORT_NUM);
            StartTime =System.currentTimeMillis();
            QuickSort.asort(qsNumbers,0,qsNumbers.length-1);
            Long qsRunTime =System.currentTimeMillis()-StartTime;
            System.out.println(""+qsRunTime +"ms");

            Long[] pqsNumbers=generateRamdomNumbers(SORT_NUM);
            System.out.println("Excuting parallel QuickSort");
            ForkJoinPool pool=new ForkJoinPool();
            ParalleQuickSort paralleQuickSort =new ParalleQuickSort(THRESHOLD,pqsNumbers);
            StartTime =System.currentTimeMillis();
            pool.execute(paralleQuickSort);
            while (!paralleQuickSort.isDone()){
           }
           Long pqsRunTime =System.currentTimeMillis()-StartTime;
            System.out.println("Total use:"+pqsRunTime+"ms\n");
        }
    }
}

