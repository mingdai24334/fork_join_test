package com.company;


import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.FutureTask;

public class FutureTaskTest {
    private static FutureTask<String> task = new FutureTask<String>(new Callable<String>() {

        @Override
        public String call() throws Exception {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Running...");
                Thread.sleep(1000);
            }
            return "The End";
        }

    });

    public static void main(String[] args) throws InterruptedException {
        //new Thread(task).start();
        //Thread.sleep(1500);

        ForkJoinPool.commonPool().execute(task);
        System.out.println(task.isCancelled());
        System.out.println(task.isDone());
        Thread.sleep(1500);
        System.out.println(task.isCancelled());
        System.out.println(task.isDone());
        task.cancel(false);
        System.out.println(task.isCancelled());
        System.out.println(task.isDone());

    }
}
