package org.hkm.algorithm.sort;

import java.util.Random;

public abstract class Sort {

    int[] ary;

    public Sort(int size) {
        newAry(size);
    }

    abstract void sort();

    public void print() {
        StringBuilder sb = new StringBuilder();
        for (int o : ary) {
            sb.append(o).append(",");
        }
        String s = sb.toString().substring(0, sb.length() - 1);
        System.out.println(s);
    }

    public void swap(int x, int y) {
        int tmp = ary[x];
        ary[x] = ary[y];
        ary[y] = tmp;
    }

    public void newAry(int size){

        ary = new int[size];

        Random r = new Random();

        int range = size*10;

        for (int i = 0; i < size; i++) {
            ary[i] = r.nextInt(range);
        }
    }



}
