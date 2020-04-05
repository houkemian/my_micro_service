package org.hkm.tencent;

public class ListNode {

    public int val;
    public ListNode next;

    public ListNode(int x) {
        this.val = x;
    }

    public void append(int x) {
        ListNode n = this;
        while (n.next != null) {
            n = n.next;
        }
        n.next = new ListNode(x);
    }

    public void print(){
        ListNode n = this;

        StringBuilder sb = new StringBuilder();

        while (n != null) {
            sb.append(n.val);
            sb.append("->");
            n = n.next;
        }
        System.out.println(sb.toString().substring(0,sb.length()-2));

    }
}
