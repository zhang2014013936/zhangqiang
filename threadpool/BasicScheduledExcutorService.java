package threadpool;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class ScheduledTask implements Runnable {
    private int taskId;


    public ScheduledTask(int taskId) {
        this.taskId = taskId;
    }
    public void run(){
        LocalDateTime currentDateTime =LocalDateTime.now();
        System.out.println("task"+this.taskId+"run at"+currentDateTime);
    }
}



    public class BasicScheduledExcutorService {
    public static void main(String[] args) {
        final  int threadPoolSize=3;
        ScheduledExecutorService scheduledExcutorService=Executors.newScheduledThreadPool(threadPoolSize);
        ScheduledTask task1 =new ScheduledTask(1);
        ScheduledTask task2 =new ScheduledTask(2);

        scheduledExcutorService.schedule(task1,2, TimeUnit.SECONDS);
        scheduledExcutorService.scheduleAtFixedRate(task2,0,10,TimeUnit.SECONDS);
        try{
            TimeUnit.SECONDS.sleep(25);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scheduledExcutorService.shutdown();
    }
}
