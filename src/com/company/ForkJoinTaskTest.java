package com.company;

import java.util.concurrent.ForkJoinTask;

public class ForkJoinTaskTest {
    public static void main(String[] args) {
        ForkJoinTask t = ForkJoinTask.adapt(()->1);

        System.out.println(t.fork().join());

        ForkJoinTask t1 = ForkJoinTask.adapt(()->{System.out.println("runable is running "+Thread.currentThread().getName());});
        System.out.println(t1.isDone());
        System.out.println(t1.isCancelled());
        t1.fork();
        System.out.println(t1.isDone());
        System.out.println(t1.isCancelled());

        System.out.println(t1.cancel(true));
        //t1.join();
        System.out.println(t1.isDone());
        System.out.println(t1.isCancelled());


        ForkJoinTask t2 = ForkJoinTask.adapt(()->{System.out.println("runable is running too");}, 2);
        System.out.println(t2.fork().join());
    }
}
