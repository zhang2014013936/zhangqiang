package threadpool;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BasicExcutorService {
    private static class BasicTask implements Runnable{
        private int taskId;
        private int loopNum;
        public BasicTask (int taskId,int loopNum){
            this.loopNum=loopNum;
            this.taskId=taskId;
        }
        @Override
        public void run() {
            for (int i=0;i<=loopNum;i++){
                System.out.println("task"+this.taskId+"-Ineration"+i);
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("task"+this.taskId+"has been interrupted");
                    break;
                }

            }

        }
    }

    public static void main(String[] args) {
        final int threadNum=3;
         final int loopNum=3;
         final  int  taskNum =5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        for(int i=0;i<=taskNum;i++){
            BasicTask task =new BasicTask(i,loopNum);
            executorService.submit(task);

     }
     executorService.shutdown();

    }
}
