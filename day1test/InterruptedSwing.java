package day1test;

import javax.swing.*;
import java.awt.*;

public class InterruptedSwing extends JFrame {
    Thread thread;

    public static void main(String[] args) {
        init(new InterruptedSwing(),100,100);
    }

    private static void init(JFrame frame , int width, int height) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(width,height);
    }

    public InterruptedSwing(){
        super();
        final JProgressBar progressBar =new JProgressBar();
        getContentPane().add(progressBar,BorderLayout.NORTH);
        progressBar.setStringPainted(true);
        thread =new Thread(new Runnable() {
            int count =0;
            @Override
            public void run() {
                while (true){
                    progressBar.setValue(++count);
                    try{
                        thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        thread.start();
        thread.interrupt();
    }
}
