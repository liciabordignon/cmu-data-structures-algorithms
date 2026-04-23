// Name: Licia Bordignon (lbordign)
// Course: Data Structures and Algorithms (95-771)
// Assignment: Project 1 - Part 1

package edu.cmu.andrew.lbordign;

import edu.colorado.nodes.ObjectNode;

import java.util.Random;

public class OrderedLinkedListOfIntegers {

    private ObjectNode head;
    private ObjectNode tail;
    private ObjectNode cursor;

    // constructor
    public OrderedLinkedListOfIntegers() {
        head = null;
        tail = null;
        cursor = null;
    }

    // SORTEDADD
    // In the best case, this method is Big Theta(1) when the list is empty, or when the value belongs at the head or tail.
    // In the worst case, this method is Big Theta(n) because it may traverse much of the list to find the insertion point.
    // Postcondition: the list remains sorted in nondecreasing order and includes the new value.
    public void sortedAdd(int value) {
        ObjectNode newNode = new ObjectNode(Integer.valueOf(value), null);

        // empty list
        if (head == null) {
            head = newNode;
            tail = newNode;
            return;
        }

        // insert at front (duplicates allowed)
        int headVal = (Integer) head.getData();
        if (value <= headVal) {
            newNode.setLink(head);
            head = newNode;
            return;
        }

        // insert at end (duplicates allowed)
        int tailVal = (Integer) tail.getData();
        if (value >= tailVal) {
            tail.setLink(newNode);
            tail = newNode;
            return;
        }

        // insert in the middle (duplicates allowed)
        ObjectNode prev = head;
        ObjectNode curr = head.getLink();

        while (curr != null) {
            int currVal = (Integer) curr.getData();
            if (value <= currVal) {
                prev.setLink(newNode);
                newNode.setLink(curr);
                return;
            }
            prev = curr;
            curr = curr.getLink();
        }

    }

    // ITERATION
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
    public Integer next() {
        Integer data = (Integer) cursor.getData();
        cursor = cursor.getLink();
        return data;
    }



    // This method has no cases to consider and is Big Theta(n) because it goes through the entire list to count nodes
    public int size() {
        return ObjectNode.listLength(head);
    }

    // This method has no cases to consider and is Big Theta(n) because it goes through the entire list to build the string
    public String toString() {
        StringBuilder sb = new StringBuilder();
        ObjectNode c = head;

        while (c != null) {
            sb.append(c.getData());
            if (c.getLink() != null) sb.append(" "); //I added a space between printed numbers to make them more readable
            c = c.getLink();
        }
        return sb.toString();
    }


    // MERGE
    // This method has no cases to consider and is Big Theta(m + n) because it traverses each input list at most once (m length of a and n length of b)
    //Precondition: a and b are either null or null terminated linked lists.
    // AND each is already sorted in nondecreasing order
    public static OrderedLinkedListOfIntegers merge(OrderedLinkedListOfIntegers a,
                                                    OrderedLinkedListOfIntegers b) {

        OrderedLinkedListOfIntegers result = new OrderedLinkedListOfIntegers();

        ObjectNode p = (a == null) ? null : a.head;
        ObjectNode q = (b == null) ? null : b.head;

        while (p != null && q != null) {
            int pv = (Integer) p.getData();
            int qv = (Integer) q.getData();

            if (pv <= qv) {
                result.appendAtEnd(pv);     // O(1)
                p = p.getLink();
            } else {
                result.appendAtEnd(qv);     // O(1)
                q = q.getLink();
            }
        }

        // append leftovers
        while (p != null) {
            result.appendAtEnd((Integer) p.getData());
            p = p.getLink();
        }

        while (q != null) {
            result.appendAtEnd((Integer) q.getData());
            q = q.getLink();
        }

        return result;
    }

    // This method has no cases to consider and is Big Theta(1) because it appends a new node by updating only tail and one link reference.
    private void appendAtEnd(int value) {
        ObjectNode newNode = new ObjectNode(Integer.valueOf(value), null);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setLink(newNode);
            tail = newNode;
        }
    }

    public static void main(String[] args) {

        OrderedLinkedListOfIntegers list1 = new OrderedLinkedListOfIntegers();
        OrderedLinkedListOfIntegers list2 = new OrderedLinkedListOfIntegers();

        Random r = new Random();

        // add 20 random values to each list
        for (int i = 0; i < 20; i++) {
            list1.sortedAdd(r.nextInt(100));
            list2.sortedAdd(r.nextInt(100));
        }

        // show the two sorted lists
        System.out.println("List1: " + list1.toString());
        System.out.println("List2: " + list2.toString());

        // demonstrate iteration (as in exercise 9)
        System.out.println("Iterating List1:");
        list1.reset();
        while (list1.hasNext()) {
            System.out.println(list1.next());
        }



        // merge into a third list
        OrderedLinkedListOfIntegers merged = OrderedLinkedListOfIntegers.merge(list1, list2);

        System.out.println("Merged: " + merged.toString());
        System.out.println("Merged size = " + merged.size());
    }
}


// Note: Portions of this code were developed with the assistance of ChatGPT
// The final implementation, structure, and comments are my own.