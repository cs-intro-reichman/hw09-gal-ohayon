/** Represents a node in a linked list. */
public class Node {
    CharData cp;
    Node next;

    Node(CharData cp, Node next) {
        this.cp = cp;
        this.next = next;
    }

    Node(CharData cp) {
        this(cp, null);
    }
}