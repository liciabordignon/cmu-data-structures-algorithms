// Name: Licia Bordignon (lbordign)
// Course: Data Structures and Algorithms (95-771)
// Assignment: Project 1 - Part 3


package edu.cmu.andrew.lbordign;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MerkleTree {

    // required hashing method
    public static String h(String text) throws
            NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash =
                digest.digest(text.getBytes(StandardCharsets.UTF_8));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i <= 31; i++) {
            byte b = hash[i];
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }


     // Read all lines from a UTF-8 text file into a SinglyLinkedList (each line is one node -> leaves fo the tree)
    public static SinglyLinkedList readFileToList(String filename) throws IOException {
        SinglyLinkedList lines = new SinglyLinkedList();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filename), StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.addAtEndNode(line);
            }
        }
        return lines;
    }


    // If the list has an odd number of nodes, duplicate the last node to make it even.
    private static void forceEvenSize(SinglyLinkedList list) {
        int n = list.countNodes();
        if (n % 2 == 1) {
            list.addAtEndNode(list.getLast());
        }
    }


    // We build the Merkle tree as a “list of lists”:
    // - the outer list stores one list per level of the tree
    // - each inner list stores the values at that level

    // Level definitions:
        // - Level 0 (leaves of the tree): each node stores one line from the input file
        // - Level 1: each node stores the hash of the corresponding leaf
        // - Level 2 and above: each node stores h(leftHash + rightHash), where leftHash and rightHash are adjacent hashes from the previous level (concatenated)
    // If a level (except for the last one, the root) has an odd number of nodes, we duplicate the last node to make the level even (forceEvenSize)

    // Example:
    // - Level 0: ["L1", "L2", "L3", "L4"]
    // - Level 1: [h(L1), h(L2), h(L3), h(L4)]
    // - Level 2: [h(h(L1)+h(L2)), h(h(L3)+h(L4))]
    // ...
    // - Last level : [h( ... )]  ->  single element: the Merkle root

    public static SinglyLinkedList buildMerkleTree(SinglyLinkedList leaves)
            throws NoSuchAlgorithmException {

        SinglyLinkedList levels = new SinglyLinkedList(); //outer list: each node stores a SinglyLinkedList representing one level

        // Level 0: raw leaves (lines)
        forceEvenSize(leaves);
        levels.addAtEndNode(leaves);

        // Level 1: hash of each leaf
        SinglyLinkedList level1 = new SinglyLinkedList();
        leaves.reset(); //start from head
        while (leaves.hasNext()) {
            String line = (String) leaves.next();
            level1.addAtEndNode(h(line)); //h is the hash function
        }
        forceEvenSize(level1); // ensure an even number of hashes so we can process pairs at the next level
        levels.addAtEndNode(level1);

        // Levels 2 until root
        SinglyLinkedList current = level1; //at the beginning current is level1 (hash of the leaves)

        while (current.countNodes() > 1) { //while we are not at the Merkle root
            SinglyLinkedList parent = new SinglyLinkedList(); // at each iteration we build a list that will contain the hashes of the level above the current

            current.reset();
            while (current.hasNext()) {
                String left = (String) current.next();
                String right = (String) current.next();
                parent.addAtEndNode(h(left + right));//h is the hash function
                //the loop constructs the parent level by hashing adjacent pairs of nodes from the current level
            }

            // If parent is not the root yet we force even
            if (parent.countNodes() > 1) {
                forceEvenSize(parent);
            }

            levels.addAtEndNode(parent);
            current = parent; //current becomes the level just created by the loop
        }

        return levels;
    }

    // computing the Merkle root of a file
    public static String merkleRootOfFile(String filename)
            throws IOException, NoSuchAlgorithmException {

        SinglyLinkedList leaves = readFileToList(filename); // we use the method defined above to build leaves

        // Edge case: empty file -> root defined as hash("").
        if (leaves.countNodes() == 0) {
            return h("");
        }

        SinglyLinkedList levels = buildMerkleTree(leaves); // we use the method defined above to build the entire Merkle Tree

        // Levels is a list of lists, the last level list is the list that contains the root
        Object lastLevelObj = levels.getLast();
        SinglyLinkedList lastLevel = (SinglyLinkedList) lastLevelObj;

        // The root is the only element in the last level. By calling reset(), the cursor is positioned at the head of the list, and next() returns the Merkle root.
        lastLevel.reset();
        return (String) lastLevel.next();
    }


     // Main in which I prompt user to enter the file name, build the Merkle tree, and print the root
    public static void main(String[] args) throws Exception {
        System.out.println("Enter a filename and I will compute the Merkle root:");
        byte[] buf = new byte[4096];
        int len = System.in.read(buf);
        if (len <= 0) return;

        String filename = new String(buf, 0, len, StandardCharsets.UTF_8).trim();

        String root = merkleRootOfFile(filename);
        System.out.println("Merkle root:");
        System.out.println(root);

        // smallFile.txt Merkle root: 1E65FD1D88D4B1133A8B0172A2C1E4FD6696E2794534C23AAC6722CAAD3809F3
        // CrimeLatLonXY.csv Merkle root: A5A74A770E0C3922362202DAD62A97655F8652064CCCBE7D3EA2B588C7E07B58
        // CrimeLatLonXY1990_Size2.csv Merkle root: DDD49991D04273A7300EF24CFAD21E2706C145001483D161D53937D90F76C001
        // CrimeLatLonXY1990_Size3.csv Merkle root: 313A2AD830ED85B5203C8C2A9895ADFA521CD4ABB74B83C25DA2C6A47AE08818

        // Therefore, the file that has the Merkle root equal to A5A74A770E0C3922362202DAD62A97655F8652064CCCBE7D3EA2B588C7E07B58
        // is the CrimeLatLonXY.csv file.
    }
}


// Note: Portions of this code were developed with the assistance of ChatGPT
// The final implementation, structure, and comments are my own.