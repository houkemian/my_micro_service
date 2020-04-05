package org.hkm.data_structure.link;

import lombok.Data;

@Data
public class SingleLink implements Link{

    public static void main(String[] args) {
        SingleLink link = new SingleLink();

        link.add(0);
        link.add(3);
        link.add(5);
        link.add(9);
        link.add(6);
        link.add(2);
        link.add(1);
        link.add(8);
        link.print(link.root);

        link.print(link.partation(1));



    }

    Node root;

    public void add(int t) {

        Node nNode = new Node(t);

        if (root == null) {
            root = nNode;
            return;
        }

        Node n = Node.guard();
        n.next = root;
        while (n.next != null) {
            n = n.next;
        }
        n.next = nNode;


    }

    public Node remove(int t){

        if (root == null) {
            return null;
        }

        Node removeNode = null;

        Node preNode = Node.guard();
        preNode.next = root;

        while (preNode.next != null) {
            if (preNode.next.val == t) {
                removeNode = preNode.next;
                preNode.next = removeNode.next;
            } else {
                preNode = preNode.next;
            }
        }
        return removeNode;
    }

    public Node find(int val){

        if (root == null) {
            return null;
        }

        Node n = Node.guard();
        n.next = root;
        while (n.next != null) {
            if (n.next.val == val) {
                return n.next;
            }
            n = n.next;
        }

        return null;
    }

    public Node resever(){


        Node cur = root;
        Node pre = null;


        while (cur!=null ) {
            Node tmp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = tmp;
        }

        root = pre;

        return null;
    }

    @Override
    public Node partation(int val) {

        Node before = Node.guard();
        Node headbefore = before;
        Node after = Node.guard();
        Node headafter = after;

        Node n = root;

        int baseCount = 0;

        while (n != null) {
            if (n.val < val) {
                before.next = n;
                before = n;
            } else if (n.val>val){
                after.next = n;
                after = n;
            } else {
                baseCount++;
            }
            n = n.next;
        }
        while (baseCount > 0) {
            Node b = new Node(val);
            before.next = b;
            before = before.next;
            baseCount--;
        }
        before.next = headafter.next;
        headbefore = headbefore.next;

        return headbefore;
    }

}
