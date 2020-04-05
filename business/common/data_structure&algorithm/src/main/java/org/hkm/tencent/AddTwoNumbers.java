package org.hkm.tencent;

public class AddTwoNumbers {

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head = new ListNode(-1);
        ListNode r = head;

        boolean extract = false;

        while (l1 != null || l2 != null) {
            int v = 0;
            if (l1 != null && l2 != null) {
                v = l1.val+l2.val+(extract?1:0);
            } else if (l1 != null) {
                v = l1.val+(extract?1:0);
            } else if (l2 != null) {
                v = l2.val + (extract ? 1 : 0);
            }
            ListNode ln = new ListNode(v%10);
            r.next = ln;
            extract = v/10>0;
            r = ln;
            l1 = l1==null?null:l1.next;
            l2 = l2==null?null:l2.next;
        }
        if (extract) {
            r.next = new ListNode(1);
        }

        return head.next;
    }

    public static void main(String[] args) {
        ListNode l = new ListNode(8);
        l.append(1);

        ListNode x = new ListNode(0);

        System.out.println(addTwoNumbers(l,x));

    }

    static ListNode int2Link(Long val) {

        ListNode head = new ListNode(-1);
        ListNode node = head;
        Long v = 0l;
        while (val >= 10) {
            v = val%10;
            val = val/10;
            ListNode n = new ListNode(v.intValue());
            node.next =n;

            node = n;
        }
        node.next = new ListNode(val.intValue());

        return head.next;
    }

    static long link2Int(ListNode node) {

        long ret = 0;

        int bit = 0;

        while (node != null) {
            ret += node.val * Math.pow(10, bit++);
            node = node.next;
        }

        return ret;

    }


}
