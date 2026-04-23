// Name: Licia Bordignon (lbordign)
// Course: Data Structures and Algorithms (95-771)
// Assignment: Project 5

public class LinkedList {
    private Node head;

    // Insert a (key, value) pair into this linked list
    // After having identified the correct bucket, the HashTable put method calls this method to insert an element in the linked list
    public void putll(String key, int value) {

        // we create a new node and insert it at the front of the list
        Node n = new Node(key, value);
        n.next = head; // 1. make it point to head
        head = n; // 2. make it equal to head
    }

    // Retrieve the value associated with a key
    // After having identified the correct bucket, the HashTable get method calls this method to retrieve an element from the linked list
    public int getll(String key) {
        Node current = head;

        // Traverse the list looking for the key
        while (current != null) {
            // If found, return its value
            if (current.key.equals(key)) {
                return current.value;
            }
            // else, move to next node
            current = current.next;
        }
        // If key is not found, return -1. This is then used in LZW to understand we have to write a new value
        return -1;
    }
}