package org.hkm.algorithm.sort;

import org.springframework.util.comparator.Comparators;

import java.util.Comparator;

public class QuickSort extends Sort{

    public static void main(String[] args) {
            QuickSort qs = new QuickSort(20);
            qs.print();
            qs.sort();
            qs.print();
            System.out.println("====================================");
    }

    public QuickSort(int size) {
        super(size);
    }

    public QuickSort() {
        super(10);
    }

    @Override
    void sort() {
        sort(0, ary.length-1);
    }

    void sort(int start, int end) {
        if (start < end) {
            int p = partation(start, end);
            sort(start, p - 1);
            sort(p+1, end);
        }
    }


    int partation(int start, int end) {

        int base = ary[end];

        int smallIndex = start-1;
        int bigIndex = start;

        for (; bigIndex < end; bigIndex++) {
            if (ary[bigIndex] <= base) {
                swap(bigIndex,++smallIndex);
            }
        }
        swap(++smallIndex, end);

        return smallIndex;

    }



}
