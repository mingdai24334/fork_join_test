package com.company;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class SortTest extends RecursiveAction {
    Integer[] arr;
    int start;
    int end;
    int[] temp;

    public void mergeSort(Integer[] arr, int start, int end, int[] temp) {
        if (end == start)
            return;

        int mid = start + (end-start)/2;

        mergeSort(arr, start, mid, temp);
        mergeSort(arr,mid+1,end, temp);

        merge(arr, start, mid, mid+1, end, temp);
    }

    private void merge(Integer[] arr, int start1, int end1, int start2, int end2, int[] temp) {
        int i = start1;
        int j = start2;
        int k = start1;
        for (int m=start1; m<=end1; m++) {
            temp[m] = arr[m];
        }
        for (int m=start2; m<=end2; m++) {
            temp[m] = arr[m];
        }

        while (i <= end1 && j <= end2) {
            if (temp[i] <= temp[j]) {
                arr[k++] = temp[i++];
            }
            else
                arr[k++] = temp[j++];
        }

        if (i>end1) {
            for (int m=j; m<=end2; m++)
                arr[k++] = temp[j++];
        }
        else {
            for (int m=i; m<=end1; m++)
                arr[k++] = temp[i++];
        }
    }

    public SortTest(Integer[] arr, int start, int end, int[] temp) {
        this.arr = arr;
        this.start = start;
        this.end = end;
        this.temp = temp;
    }

    @Override
    protected void compute() {
        //System.out.println(Thread.currentThread().getName());
        if (end - start < 100)
            mergeSort(arr, start, end, temp);
        else {
            int mid = start + (end-start)/2;
            invokeAll(new SortTest(arr, start, mid, temp), new SortTest(arr, mid+1, end, temp));
            merge(arr, start, mid, mid+1, end, temp);
        }
    }

    public static void main(String[] args) {
        Integer[] arr = new Integer[1000000];
        for (int i=0; i<arr.length; i++)
            arr[i] = i;

        List<Integer> l = Arrays.asList(arr);
        Collections.shuffle(l);
        l.toArray(arr);

        Integer[] arr1 = arr.clone();

        //print(arr);

        int[] temp = new int[arr.length];
        SortTest sort = new SortTest(arr, 0, arr.length-1, temp);

        ForkJoinPool pool = new ForkJoinPool();
        long t1 = Calendar.getInstance().getTimeInMillis();
        pool.invoke(sort);
        long t2 = Calendar.getInstance().getTimeInMillis();
        //print(arr);
        System.out.println(t2-t1);

        //print(arr1);

        SortTest sort1 = new SortTest(arr1, 0, arr1.length-1, temp);
        long t3 = Calendar.getInstance().getTimeInMillis();
        sort1.mergeSort(arr1, 0, arr1.length-1, temp);
        long t4 = Calendar.getInstance().getTimeInMillis();

        //print(arr1);
        System.out.println(t4-t3);

    }

    static void print(Integer[] arr) {
        for (Integer v : arr) {
            System.out.print(v + ",");
        }
        System.out.println();
    }

}
