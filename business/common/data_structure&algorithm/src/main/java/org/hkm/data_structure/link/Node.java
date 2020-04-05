package org.hkm.data_structure.link;

import lombok.Data;

@Data
public class Node {

    Node next;
    Integer val;

    public Node(Integer val) {
        this.val = val;
    }

    public static Node guard(){
        return new Node(null);
    }

}
