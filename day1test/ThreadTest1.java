package day1test;

public class ThreadTest1 implements Runnable {
    int num =10;
    @Override
    public void run() {
        while (true){
            synchronized (""){
                if(num>0){
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("tickets"+ --num);

                }
            }
        }

    }

    public static void main(String[] args) {
        ThreadTest1  t= new ThreadTest1();
        Thread ta =new Thread(t);
        Thread tb =new Thread(t);
        Thread tc =new Thread(t);
        Thread td =new Thread(t);
        ta.start();
        tb.start();
        tc.start();
        td.start();

    }
    public synchronized void doit(){
        if(num>0){
            try{
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ticket"+ --num);

        }
    }

}
