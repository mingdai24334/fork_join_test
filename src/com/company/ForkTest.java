package com.company;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class ForkTest extends RecursiveTask<Long> implements Runnable {
    static Set<String> threadNames = new HashSet<>();

    int[] arr;
    int start;
    int end;
    long result;
    public ForkTest(int[] arr, int start, int end) {

        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    void setResult(long result) {
        this.result = result;
    }

    long getReslt() {
        return this.result;
    }

    @Override
    protected Long compute() {
        System.out.println(Thread.currentThread().getId()+ " " + Thread.currentThread().getName() + ", start: " + start
                + ", end: " + end );
        synchronized(ForkTest.class) {
            threadNames.add(Thread.currentThread().getName());
        }
        if (end - start < 1000) {
            long sum =0;
            for (int i= start; i<end; i++)
                sum += arr[i];
            System.out.println(Thread.currentThread().getId()+ " " + Thread.currentThread().getName() + " before return"
                    + ", start: " + start
                    + ", end: " + end + ", result: " + sum );
            return sum;
        }
        else {
            int dist = (end-start) / 2;
            int start1 = start;
            int end1 = start+dist;

            int start2 = end1;
            int end2 = start2+dist;

            int start3 = end2;
            int end3 = start3 + dist;

            int start4 = end3;
            int end4 = end;

            ForkTest t1 = new ForkTest(arr, start1, end1);
            ForkTest t2 = new ForkTest(arr, start2, end2);
            ForkTest t3 = new ForkTest(arr, start3, end3);
            ForkTest t4 = new ForkTest(arr, start4, end4);
//            t1.fork();
//            t2.fork();
//            t2.join();
//            t1.join();
            invokeAll(t1, t2);
            //invokeAll(t1, t2, t3, t4);

            try {
                long result =  t1.get() + t2.get();
                System.out.println(Thread.currentThread().getId()+ " " + Thread.currentThread().getName() + " before return"
                        + ", start: " + start
                        + ", end: " + end + ", result: " + result);
                return result;
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                return (long)-1;
            } catch (ExecutionException e) {

                e.printStackTrace();
                throw new RuntimeException("somethign wrong");
            }
        }
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getId()+ " " + Thread.currentThread().getName() + ", start: " + start
        + ", end: " + end);
        synchronized(ForkTest.class) {
            threadNames.add(Thread.currentThread().getName());
        }
        if (end - start < 10) {
            long sum =0;
            for (int i= start; i<end; i++)
                sum += arr[i];
            setResult(sum);
        }
        else {
            int dist = (end-start) / 2;
            int start1 = start;
            int end1 = start+dist;

            int start2 = end1;
            int end2 = end;

            ForkTest task1 = new ForkTest(arr, start1, end1);
            ForkTest task2 = new ForkTest(arr, start2, end2);
            Thread t1 = new Thread(task1);
            Thread t2 = new Thread(task2);
            t1.start();
            t2.start();

            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            setResult(task1.getReslt() + task2.getReslt());
        }
    }



    public static void main(String[] args) throws ExecutionException, InterruptedException {
	// write your code here
        int[]  arr = new int[100000];
        for (int i=0; i<arr.length; i++) {
            arr[i] = i;
        }

        ForkTest t = new ForkTest(arr, 0, arr.length);
        ForkJoinTask<Long> task = t.fork();
        Long v = task.join();
        System.out.println(v);
        System.out.println(ForkTest.threadNames);
        System.out.println(ForkTest.threadNames.size());

//        ForkTest t = new ForkTest(arr, 0, arr.length);
//        t.run();
//        Long v = t.getReslt();
//        System.out.println(v);
//        System.out.println(ForkTest.threadNames);
//        System.out.println(ForkTest.threadNames.size());
    }



}
