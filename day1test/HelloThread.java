package day1test;

public class HelloThread extends Thread {
    public void run(){
        System.out.println("hello from a thread!");
    }

    public static void main(String[] args) {
        (new HelloThread()).start();
    }
}
