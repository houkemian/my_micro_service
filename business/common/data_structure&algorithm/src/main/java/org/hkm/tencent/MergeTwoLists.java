package org.hkm.tencent;

public class MergeTwoLists {

    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {

        ListNode head = new ListNode(-1);
        ListNode r = head;
        while (l1 != null || l2 != null) {
            ListNode tmp = null;
            if (l1 == null) {
                tmp = new ListNode(l2.val);
                l2 = l2==null?null:l2.next;
            } else if (l2 == null) {
                tmp = new ListNode(l1.val);
                l1 = l1==null?null:l1.next;
            } else {
                if (l1.val < l2.val) {
                    tmp = new ListNode(l1.val);
                    l1 = l1.next;
                } else if (l1.val > l2.val) {
                    tmp = new ListNode(l2.val);
                    l2 = l2.next;
                } else {
                    ListNode equalNode = new ListNode(l1.val);
                    r.next = equalNode;
                    r = r.next;
                    tmp = new ListNode(l2.val);
                    l1 = l1==null?null:l1.next;
                    l2 = l2==null?null:l2.next;
                }
            }
            r.next = tmp;
            r = r.next;
        }


        return head.next;
    }

    public static void main(String[] args) {
        ListNode a = new ListNode(1);
        a.append(2);
        a.append(4);

        ListNode b = new ListNode(1);
        b.append(3);
        b.append(4);
        mergeTwoLists(a,b).print();
    }

}
