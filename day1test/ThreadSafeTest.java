package day1test;

public class ThreadSafeTest implements Runnable {
    int num =10;
    @Override
    public void run() {
        while (true){
            if(num>0){
                try{
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("ticket"+num--);

            }
        }

    }

    public static void main(String[] args) {
        ThreadSafeTest t =new ThreadSafeTest();
        Thread ta =new Thread(t);
        Thread tb =new Thread(t);
        Thread tc =new Thread(t);
        Thread td =new Thread(t);
        ta.start();
        tb.start();
        tc.start();
        td.start();
    }
}
