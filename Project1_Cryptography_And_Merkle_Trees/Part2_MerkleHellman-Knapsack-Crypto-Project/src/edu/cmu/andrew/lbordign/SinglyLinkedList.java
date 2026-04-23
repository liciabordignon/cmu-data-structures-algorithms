// Name: Licia Bordignon (lbordign)
// Course: Data Structures and Algorithms (95-771)
// Assignment: Project 1 - Part 1

package edu.cmu.andrew.lbordign;
import edu.colorado.nodes.ObjectNode;

public class SinglyLinkedList {

    private ObjectNode head;
    private ObjectNode tail;

    private ObjectNode cursor;

    //constructor
    // This method has no cases to consider and is Big Theta(1).
    public SinglyLinkedList() {
        head = null;
        tail = null;
        cursor = null;
    }

    // This method has no cases to consider and is Big Theta(1) because it creates one new node and appends it to the end of the list by updating only the tail reference and the link of the previous tail, without going through any part of the list
    // Postcondition: the list contains one additional node at the end, and the tail reference points to the new last node
    public void addAtEndNode(Object c) {
        ObjectNode newNode = new ObjectNode(c, null);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setLink(newNode);
            tail = newNode;
        }
    }

    // This method has no cases to consider and is Big Theta(1) because it creates one new node and updates only the head reference (and the tail if the list is empty) without traversing any part of the list
    // Postcondition: the list contains one additional node at the front, and the head reference points to the new first node.
    public void addAtFrontNode(Object c) {
        ObjectNode newNode = new ObjectNode(c, head);
        head = newNode;

        if (tail == null) {
            tail = newNode;
        }
    }

    // This method has no cases to consider and is Big Theta(n) because it goes through the entire list to count the nodes.
    public int countNodes() {
        return ObjectNode.listLength(head);
    }

    // This method has no cases to consider and is Big Theta(1) because it directly accesses the tail.
    public Object getLast() {
        if (tail == null) {
            return null;
        }
        return tail.getData();
    }

    // In the best case, this method is Big Theta(1) when i = 0.
    // In the worst case, this method is Big Theta(n) when the index refers to the last element or is out of range.
    // Precondition: i >= 0.
    // Postcondition: returns the object at index i if it exists, otherwise returns null.
    public Object getObjectAt(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Index must be non-negative");
        }

        ObjectNode cursor = head;
        int index = 0;

        while (cursor != null) {
            if (index == i) {
                return cursor.getData();
            }
            cursor = cursor.getLink();
            index++;
        }

        return null;
    }

    /// /////////////////////////
    // This method has no cases to consider and is Big Theta(1) because it simply assigns the cursor reference to the head reference
    public void reset() {
        cursor = head;
    }

    // This method has no cases to consider and is Big Theta(1) because it only checks whether the cursor reference is null
    public boolean hasNext() {
        return cursor != null;
    }

    // This method has no cases to consider and is Big Theta(1) because it returns one element and advances the cursor once.
    // Precondition: hasNext() is true
    public Object next() {
        Object data = cursor.getData();
        cursor = cursor.getLink();
        return data;
    }
    /// //////////////////


    // This method has no cases to consider and is Big Theta(n) because it goes through the entire list to build the string.
    public String toString() {
        if (head == null) {
            return "";
        }
        return head.toString();
    }


    public static void main(String[] args) {

        // Creating empty list
        SinglyLinkedList s = new SinglyLinkedList();

        // Test addAtEndNode
        s.addAtEndNode("a");
        s.addAtEndNode("b");
        s.addAtEndNode("c");

        // Test toString
        System.out.println("List contents (toString):");
        System.out.println(s.toString());

        // Test addAtFrontNode
        s.addAtFrontNode("z");
        System.out.println("After adding at front:");
        System.out.println(s.toString());

        // Test countNodes
        System.out.println("Number of nodes:");
        System.out.println(s.countNodes());

        // Test getLast
        System.out.println("Last element:");
        System.out.println(s.getLast());

        // Test getObjectAt (0-based)
        System.out.println("Element at index 1:");
        System.out.println(s.getObjectAt(1));

        // Test required (exercise 9)
        System.out.println("Iterating through list:");
        s.reset();
        while (s.hasNext()) {
            System.out.println(s.next());
        }
    }



}


// Note: Portions of this code were developed with the assistance of ChatGPT
// The final implementation, structure, and comments are my own.
// The implemented functions are those listed at the following link: https://www.andrew.cmu.edu/user/mm6/95-771/ObjectNodeProject/dist/javadoc/index.html
