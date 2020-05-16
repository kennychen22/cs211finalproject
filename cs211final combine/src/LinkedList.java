public class LinkedList {
    private Node dummy;
    private int count;

    public int getCount() {return count;}

    public LinkedList() {
        // COLLABORATIVE ASSIGNMENT: IMPLEMENT THIS
        dummy = new Node();
        dummy.prev = dummy;
        dummy.next = dummy;
        count = 0;
    }

    public Node getDummy() {return dummy;}

    public void printListCount() {
        int count = 0;
        Node n = dummy.next;
        while (n != dummy ) {
            //    System.out.println(n.username);
            count++;
            n = n.next;
        }
        System.out.println(count);
    }

    public void printList() {
        Node n = dummy.next;
        while (n != dummy ) {
                System.out.println(n.username + ": " + n.rating);
            n = n.next;
        }
    }

    public int size() {
        // COLLABORATIVE ASSIGNMENT: IMPLEMENT THIS
        return count;
    }

    public void add(Node n) {
        // COLLABORATIVE ASSIGNMENT: IMPLEMENT THIS
//        Node n = new Node(username);


        //points new node back to dummy
        n.next = dummy;
        // makes new node's prev refer to second to last node
        n.prev = dummy.prev;
        // makes prev node point to new node
        n.prev.next = n;
        //makes dummy node point to end
        dummy.prev = n;
        count++;

    }

    public Node find(String toFind) {
        // COLLABORATIVE ASSIGNMENT: IMPLEMENT THIS
        Node n = dummy;
        if (count == 0) {
            return null;
        }
        else {
            n = n.next;
            for (int i = 0; i < count; i++) {
                if (n.username.equals(toFind)) {
                    return n;
                }
                n = n.next;

            }
        }
        return null;
    }

    public void remove(String toRemove) {
        // COLLABORATIVE ASSIGNMENT: IMPLEMENT THIS
        Node n = find(toRemove);

        if (n != dummy || n != null) {
            n.prev.next = n.next;
            n.next.prev = n.prev;
            count--;
        }
    }

}
