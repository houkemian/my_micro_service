package org.hkm.algorithm.string;

import java.util.ArrayList;
import java.util.List;

public class Atoi {

    public int myAtoi(String str) {

        str = str.trim();

        if (str.length() == 0) {
            return 0;
        }

        boolean negative = false;

        if (str.startsWith("-") || str.startsWith("+")) {
            negative = str.startsWith("-");
            str = str.substring(1, str.length());
        }

        List<Integer> list = new ArrayList<>();

        boolean start = false;

        for (int i = 0; i < str.length(); i++) {
            try {
                int v = Integer.parseInt(str.substring(i, i+1));
                if (!start) {
                    if (v != 0) {
                        start = true;
                        list.add(v);
                    }
                } else {
                    list.add(v);
                }
            } catch (NumberFormatException e) {
                break;
            }
        }

        long l = 0;

        for (int i = list.size()-1; i >= 0; i--) {
            l += list.get(i)*(Math.pow(10,list.size()-1-i));
        }

        l = negative?-l:l;

        l = l>Integer.MAX_VALUE?Integer.MAX_VALUE:l;
        l = l<Integer.MIN_VALUE?Integer.MIN_VALUE:l;

        return new Long(l).intValue();
    }

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);
        System.out.println(new Atoi().myAtoi("     00012989754 as"));
    }

}
