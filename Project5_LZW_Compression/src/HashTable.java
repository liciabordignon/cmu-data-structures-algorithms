// Name: Licia Bordignon (lbordign)
// Course: Data Structures and Algorithms (95-771)
// Assignment: Project 5

public class HashTable {
    // hash map implemented as an array of linked lists
    private LinkedList[] table;

    // We create an array with 127 linked lists (indexes from 0 to 126)
    public HashTable() {
        table = new LinkedList[127];
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList(); // We initialize each element of the array with an empty linked list
        }
    }

    // Hash function: converts a string key into an index (0–126)
    // The hash function is created following Java documentation approach (Java's String.hashCode() https://docs.oracle.com/javase/8/docs/api/java/lang/String.html#hashCode--)
    private int hash(String key) {
        int h = 0;
        // We loop through each character to build the hash calue
        for (int i = 0; i < key.length(); i++) {
            // Combine previous hash with current character
            h = (31 * h + key.charAt(i)) % 127; // We take % 127 to make sure the result fits in the table
        }
        return Math.abs(h); // Just to be sure the index is non-negative
    }

    // This inserts a (key, value) pair into the table
    public void put(String key, int value) {
        table[hash(key)].putll(key, value); // we hash the key string to identify which bucket of the array this pair belongs to
        // then we use the putll method of the linked list to insert the value
    }

    // This retrieves the value associated with a key
    public int get(String key) {
        return table[hash(key)].getll(key);
        // first we compute the correct bucket using the hash function
        // then use the linked list method getll to search the linked list in the identified bucket
    }
}