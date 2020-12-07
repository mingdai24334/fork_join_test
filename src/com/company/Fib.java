package com.company;

import java.util.Calendar;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class Fib extends RecursiveTask<Long> {
    int n;
    Fib(int n) {
        this.n = n;
    }
    @Override
    protected Long compute() {
        System.out.println(Thread.currentThread().getName());
        if (n==0)
            return (long)1;
        else if (n==1)
            return (long)1;
        else {
            ForkJoinTask<Long> t1 = new Fib(n-1).fork();
            return t1.join() + new Fib(n-2).compute();
        }
    }

    public static void main(String[] args) {
        Fib f = new Fib(20);
        Calendar cal = Calendar.getInstance();
        long t1 = Calendar.getInstance().getTimeInMillis();
        System.out.println(f.fork().join());
        long t2 = Calendar.getInstance().getTimeInMillis();
        System.out.println(t2-t1);
    }
}
