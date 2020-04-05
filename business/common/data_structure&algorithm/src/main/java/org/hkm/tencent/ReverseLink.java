package org.hkm.tencent;

public class ReverseLink {


    public ListNode reverseList(ListNode head) {

        ListNode pre = null;
        ListNode cur = head;

        while (cur != null) {
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }


        return pre;
    }

    private ListNode recursive(ListNode head) {
        return null;
    }

}
