public class redBlack {

    Node root;
    List[] table;

    public redBlack() {
       table = new List[10];   //table for hashing
       for (int i = 0; i < 10; i++)
           table[i] = new List();
    }

    public void pushBlack (Node n) {
        n.color--;
        n.left.color++;
        n.right.color++;
    }

    public void pullBlack (Node n) {
        n.color++;
        n.left.color--;
        n.right.color--;
    }

    public void flipLeft(Node n) {
        swapColors(n, n.right);
        rotateLeft(n);
    }

    public void flipRight(Node n) {
        swapColors(n, n.left);
        rotateRight(n);
    }

    public void swapColors(Node n, Node child) {
        byte temp = n.color;
        n.color = child.color;
        child.color = temp;
    }

    public boolean add(String username, int rating) {
        if (table[hash(username)].find(username) != null) {
            System.out.println("already in here");
            return false;
        }

        Node n = new Node(username, rating); //create the node to be added
        n.color = 0; // make it red first
        Node addTo = this.findLast(rating); // find where to put the node


 // perhaps implement checking condition
        table[hash(username)].add(n);
        // implementing a hashtable inside
        System.out.println("Adding " + username);
        boolean added = addChild(addTo, n);
        if (added) {
            System.out.println("Added and now we fix it!");
            addFixup(n);
        }
        return added;
    }

// finds where the node should go
    public Node findLast(int i) {
        Node temp = root, prev = null;
        if (root != null)
        System.out.println("root is " + temp.rating);
        while (temp != null) {
            prev = temp;
            if (i <= temp.rating) {
                temp = temp.left;
            } else {
                temp = temp.right;
            }
        }
        if (prev != null)
        System.out.println("last node is " + prev.rating);
        return prev;
    }

    public boolean find(int i) {
        Node temp = root;
        while (temp != null) {
            if (i <= temp.rating) {
                temp = temp.left;
            } else if (i > temp.rating) {
                temp = temp.right;
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean addChild(Node addTo, Node child) {
        // when the tree is empty
        if (addTo == null) {
            root = child;
        }
        else {
            if (child.rating <= addTo.rating) {
                addTo.left = child;
            } else {
                addTo.right = child;
            }
            child.parent = addTo;
        }

        if (child.parent != null)
        System.out.println("Is a child of " + child.parent.rating);
        return true;
    }

    public boolean remove(String username) {
        Node n = this.find(username);
        if (n == null)
            return false;

        //remove from hash table as well
        table[hash(username)].remove(username);


        Node w = n.right;

        if (w == null) {
            w = n;
            n = w.left;
        } else {
            while (w.left != null)
                w = w.left;
            n.username = w.username;
            n.rating = w.rating;
        }
        splice(w);
        //if i try to remove a leaf, I get an error
        if (n == null) {
            n = new Node();
        }
        n.color = 1;

        n.parent = w.parent;
        removeFixup(n);
        return true;
    }

    public void removeFixup(Node n) {
        while (n.color > 1) {
            if (n == root) {
                n.color = 1;
            } else if (n.parent.left.color == 0) {
                n = removeFixupCase1(n);
            } else if (n == n.parent.left) {
                n = removeFixupCase2(n);
            } else {
                n = removeFixupCase3(n);
            }
        }
        if (n != root) { //to maintain left-lean
            Node w = n.parent;
            if (w.right != null  && w.left != null) { //how to consider leaves
                if (w.right.color == 0 && w.left.color == 1) {
                    flipLeft(w);
            }}
        }
    }

    public Node removeFixupCase1(Node n) {
            flipRight(n.parent);
            return n;
        }

    public Node removeFixupCase2(Node n) {
        Node w = n.parent;
        Node v = w.right;
            pullBlack(w);
            flipLeft(w);
            Node q = w.right;
            if (q.color == 0) {
                rotateLeft(w);
                flipRight(v);
                pushBlack(q);
                if (v.right.color == 0)
                    flipLeft(v);
                    return q;
            } else {
                return v;
            }
        }

    public Node removeFixupCase3(Node n) {
    Node w = n.parent;
    Node v = w.left;
    pullBlack(w);
    flipRight(w);
    Node q = w.left;
    if (q.color == 0) {
        rotateRight(w);
        flipLeft(v);
        pushBlack(q);
        return q;
    } else {
        if (v.left.color == 0) {
            pushBlack(v);
            return v;
        } else {
            flipLeft(v);
            return w;
        }
    }
    }

    public void rotateLeft(Node n) {
        Node w = n.right;
        w.parent = n.parent;
        if (w.parent != null) {
            if (w.parent.left == n) {
                w.parent.left = w;
            } else {
                w.parent.right = w;
            }
        }
        n.right = w.left;
        if (n.right != null) {
            n.right.parent = n;
        }
        n.parent = w;
        w.left = n;
        if (n == root) {
            root = w;
            root.parent = null;
        }
    }

    public void rotateRight(Node n) {
        // rotate right
        Node w = n.left;
        // sets node w's parent to the parent of the node we are rotating on
        w.parent = n.parent;
        // if the parent of the left child (the parent of the node we rotate on) is NOT null

        if (w.parent != null) {
            // AND if parent's left child is the same as the node we rotate on
            if (w.parent.left == n) {
                // set the child of the parent of
                // the node we rotate on to w which is the new node
                w.parent.left = w;
            } else {
                w.parent.right = w;
            }
        }

        // puts the right child of w to the left child of n to keep order
        n.left = w.right;
        if (n.left != null) {
            // if there is something , then just fix the parent.
            n.left.parent = n;
        }
        // make w the parent of the node we rotated on
        n.parent = w;
        w.right = n;
        if (n == root) {
            root = w;
            root.parent = null;
        }
    }

    public void addFixup(Node n) {
     //   System.out.println("Fixing " + n.username);
        while (n.color == 0) {
            if (n == root) {
             //   System.out.println(n.rating + " is the root and is changed to be black");
                n.color = 1;
                return;
            }
         //   System.out.println("out of the root condition");
            Node w = n.parent;
            System.out.println("w is " + w.rating);
        //    System.out.println(w.username + " is the parent of n");
            if (w.left != null)
                if (w.left.color == 1) { //ensure it is left leaning
                //      System.out.println("w.left.color is black");
                flipLeft(w);
                n = w;
                w = n.parent;
            }
         //   System.out.println("out of ensuring left leaning condition");
            if (w.color == 1) {
           //     System.out.println("w color is black so we golden");
                return;
            }
            Node g = w.parent; //grandparent of n
           // System.out.println("check to see if uncle is black or red");
            if (g.right == null || g.right.color == 1) { //if uncle is black
         //       System.out.println("uncle is black");
                flipRight(g);
                return;
            } else {
                pushBlack(g);
                n = g;
            }

        }
      //  System.out.println("fixed " + n.username);
    }

    public int total(Node n) {
        if (n == null)
            return 0;
        return n.rating + total(n.left) + total(n.right);

    }

    static void traversePrint(Node n) {
        if (n == null) return;
        else System.out.print(n.rating + " ");
        traversePrint(n.left);
        traversePrint(n.right);
    }

    static void inOrderTraversal(Node n) {
        //print in max
        if (n == null)
            return;

        /* first recur on child child */
        inOrderTraversal(n.right);

        /* then print the data of node */
        System.out.println(n.username + ": " + n.rating);

        /* now recur on left child */
        inOrderTraversal(n.left);
    }

    //// hashing stuff

    private int hash(String s) {

        int sum = 0;
        for (int i = 0; i < s.length(); i++) {
            sum += s.charAt(i) * 29^(s.length() - i - 1);
        }

        return sum%10;
    }

    public Node find(String s) {
        // COLLABORATIVE ASSIGNMENT: IMPLEMENT THIS
        //get index of the String
        return table[hash(s)].find(s);

    }

    public void splice(Node toRemove) {
        Node parent, successor;
        if (toRemove.left != null) {
            successor = toRemove.left;
        }
        else {
            successor = toRemove.right;
        }
        // special case if the node we want to remove is the root node
        if (toRemove == root) {
            root = successor;
            parent = null;
        }
        else {
            parent = toRemove.parent;

            // if the node we are removing (toRemove) is the left children of parent
            // we want to make the child of toRemove
            // be the left child of the parent and
            // similarly for the right children
            // if the successor is null, this will get rid of the leaf

            if (parent.left == toRemove && parent.left != null) {
                parent.left = successor;
            }
            else {
                parent.right = successor;
            }
        }
        // if the successor is not null, then we can make
        // its parent be the parent of toRemove which is now the
        // parent of the successor and toRemove has been delinked
        if (successor != null) {
            successor.parent = parent;
        }
    }

    class List {
        private Node dummy;
        private int count;

        public int getCount() {return count;}

        public List() {
            // COLLABORATIVE ASSIGNMENT: IMPLEMENT THIS
            dummy = new Node();
            dummy.prev = dummy;
            dummy.next = dummy;
            count = 0;
        }

        public Node getDummy() {return dummy;}

        public void printList() {
            int count = 0;
            Node n = dummy.next;
            while (n != dummy ) {
            //    System.out.println(n.username);
                count++;
                n = n.next;
            }
            System.out.println(count);
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

            if (n != dummy) {
                n.prev.next = n.next;
                n.next.prev = n.prev;
                count--;
            }
        }

    }
}
