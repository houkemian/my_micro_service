package org.hkm.tencent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MergeKLists {

        public static ListNode mergeKLists(ListNode[] lists) {

        boolean finish = false;

        ListNode head = new ListNode(-1);
        ListNode n = head;

        while (!finish) {

            int min = 999;

            int minIndex = -1;

            int nullCount = 0;

            for (int i = 0; i < lists.length; i++) {
                if (lists[i] != null) {
                    int v = lists[i].val;
                    if (v <= min) {
                        min = v;
                        minIndex = i;
                    }
                } else {
                    nullCount++;
                }
            }

            if (nullCount == lists.length) {
                finish = true;
            } else {
                ListNode tmp = new ListNode(min);
                n.next = tmp;
                n = n.next;
                lists[minIndex] = lists[minIndex].next;
            }
        }

        return head.next;
    }

    public static void main(String[] args) {
        ListNode a = new ListNode(1);
        a.append(4);
        a.append(5);

        ListNode b = new ListNode(1);
        b.append(3);
        b.append(4);

        ListNode c = new ListNode(2);
        c.append(6);

        ListNode[] lists = new ListNode[3];
        lists[0] = a;
        lists[1] = b;
        lists[2] = c;

        mergeKLists(lists).print();

    }

}
