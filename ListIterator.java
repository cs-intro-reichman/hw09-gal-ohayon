/** Represents an iterator over a list of CharData objects. */
public class ListIterator {

    // Current position in the list (cursor)
    private Node current;

    /** Constructs a list iterator, starting at the given node. */
    public ListIterator(Node node) {
        this.current = node;
    }

    /** Checks if this iterator has more nodes to process */
    public boolean hasNext() {
        return (current != null);
    }
  
    /** Returns the CharData object of the current element in this iteration,
     * and advances the cursor to the next element.
     * Should be called only if hasNext() is true. */
    public CharData next() {
        if (!hasNext()) {
            return null;
        }
        CharData cd = current.cp;
        current = current.next;
        return cd;
    }
}