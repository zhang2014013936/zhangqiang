package day1test;

public class HelloRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("hello1");
    }

    public static void main(String[] args) {
        (new  Thread(new HelloThread())).start();
    }
}
