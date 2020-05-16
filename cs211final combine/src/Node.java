public class Node {
    String username;
    int rating;
    byte color;  //0 is red 1 is black
    Node parent;
    Node left;
    Node right;
    Node next;
    Node prev;

    Node(String username, int rating) {
        this.username = username;
        this.rating = rating;
        this.color = 0;
    }
    public Node() {
        this.color = 1;
    } //for black null leaves AND dummy node
}