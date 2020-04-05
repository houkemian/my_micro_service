package org.hkm.data_structure.link;

public interface Link {

    void add(int val);

    Node find(int val);

    Node remove(int val);

    Node resever();

    Node partation(int val);

    default void print(Node root){
        if (root == null) {
            System.out.println("null link");
            return;
        }

        StringBuilder sb = new StringBuilder();

        Node n = Node.guard();
        n.next = root;
        while (n.next != null ) {
            sb.append(n.next.val).append(",");
            n = n.next;
        }
        String str = sb.toString();
        System.out.println(str.substring(0, str.length()-1));
    }

}
