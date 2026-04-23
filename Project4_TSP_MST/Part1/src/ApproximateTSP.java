// Name: Licia Bordignon
// AndrewId: lbordign
// Project 4, Part 1

import java.util.LinkedList;

public class ApproximateTSP {
    private double[][] adj; // adj[u][v] is the matrix that contains distances (weights that connect u and v)
    private int[] parent; // parent[u] is the node that connects u to the MST
    private double[] key; // key[u] is the smallest known distance to connect u to the MST
    private boolean[] inMST; // inMST[u] boolean that equals true if u has already been added to the MST
    private LinkedList<Integer>[] children;
    private int root;

    // heap[1..heapSize] stores vertex indices
    private int[] heap;
    private int[] position;
    private int heapSize;

    // Pre-condition: adj is a square matrix that contains the distances between the various crimes (nodes)
    // Post-condition: this initializes the arrays that are needed for Prim's algorithm
    public ApproximateTSP(double[][] adj) {
        this.adj = adj;
        int n = adj.length;

        parent = new int[n];
        key = new double[n];
        inMST = new boolean[n];

        // At the beginning:
        // - we don't know how to connect any node yet (so key[u] = infinity for evry node)
        // - no node is in the MST
        // - parent is unknown (null is considered to be -1 ->integer)
        for (int u = 0; u < n; u++) {
            key[u] = Double.MAX_VALUE; // 'infinity'
            // inMST[u] is already false by default
            parent[u] = -1; // I set this to -1 because parent = new int[n]; initialized the array to 0 by default.
            // this could be confusing as it would look like every node is connected to vertex 0 (the root)
        }

        // to perform a preorder traversal of the tree, I will use an array of LinkedLists where children[u] will contain all nodes v such that parent[v] = u
        // here I initialize the linked lists
        children = new LinkedList[n];
        for (int i = 0; i < n; i++) {
            children[i] = new LinkedList<>();
        }

        heap = new int[n];
        position = new int[n];
        heapSize = 0;
        root = 0;
    }


    // Pre-condition: root is a valid index (0)
    // Post-condition: builds the MST and fills parent[] and key[]
    public void buildMST(int root) {
        this.root = root;
        int n = adj.length;

        key[root] = 0; // set the root key to 0 so it is selected first
        parent[root] = -1; // root has no parent

        buildHeap();

        // We loop through every node because each time we add a node to the MST, so at the end of the loop the MST will be complete
        for (int count = 0; count < n; count++) {
            // we pick the node with the smallest key value that is not yet in the MST
            int u = extractMin();

            // u is officially part of the MST
            inMST[u] = true;

            // Try to improve the connection cost for all other nodes
            for (int v = 0; v < n; v++) {

                // the graph is complete so adj[u][v] will be a distance between u and v (never null)
                // at the first loop no v except for the root is in the MST and key[v] will be infinite so we will update every key[v]. For each node, the smallest known distance to the MST will be the smallest known distance to the root
                // when we move to another node, if v is not yet in the MST AND going through u (the newly added node to the MST) is cheaper than what we knew before we update
                // - the parent of v to be u and
                // - its smallest known distance (key[v]) that would connect v to u (smallest known distance to the MST)
                if (!inMST[v] && adj[u][v] < key[v]) {
                    // best way to connect v is now through u
                    parent[v] = u;
                    key[v] = adj[u][v];
                    decreaseKey(v);
                }
            }
        }
    }

    private void buildHeap() {
        // at the beginning, all nodes are inserted into the heap
        heapSize = adj.length;

        // Fill the heap with all vertex indices
        for (int i = 0; i < adj.length; i++) {
            heap[i] = i;
            position[i] = i;   // track where each vertex is in the heap
        }

        // we build a min-heap by pushing elements down where needed (using the function heapifyDown)
        for (int i = (heapSize / 2) - 1; i >= 0; i--) {
            heapifyDown(i);
        }
    }


    // Post-condition: returns the index of the node with the smallest key that is not yet in the MST
    private int extractMin() {
        //if the heap is empty, there are no more nodes to process
        if (heapSize == 0) {
            return -1;
        }

        int minNode = heap[0]; // The root of the minheap contains the node with the smallest key value

        swap(0, heapSize-1);// we move the last added element to the root
        position[minNode] = -1; // mark this node as removed from the heap
        heapSize--;

        // we restore teh heap order after removal and swap
        heapifyDown(0); // to do so, I call this function on the new root

        return minNode; //we return the root of the minheap
    }

    private void swap(int i, int j) {
        // we swap two elements in the heap
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;

        // and also update their positions in the position array
        position[heap[i]] = i;
        position[heap[j]] = j;
    }



    private void decreaseKey(int node) {
        // get the current position of the node in the heap
        int i = position[node];

        // we move the node up the heap while its key is smaller than its parent's key (index is (i-1)/2)
        // this restores the min-heap property
        while (i > 0 && key[heap[i]] < key[heap[(i - 1) / 2]]) {
            swap(i, (i - 1) / 2);
            i = (i - 1) / 2;
        }
    }

    private void heapifyDown(int i) {
        while (true) {
            int left = 2 * i+1;       // left child
            int right = 2 * i+ 2;  // right child
            int smallest = i;

            // check if left child is smaller
            if (left < heapSize && key[heap[left]] < key[heap[smallest]]) {
                smallest = left;
            }

            // check if right child is even smaller (of current smallest)
            if (right < heapSize && key[heap[right]] < key[heap[smallest]]) {
                smallest = right;
            }

            // if one of the children is smaller, we swap and continue
            if (smallest != i) {
                swap(i, smallest);
                i = smallest;
            } else {
                break; // heap order is correct
            }
        }
    }



    public void buildChildrenLists() {
        // this builds the children array useful for the preorder traversal
        // starting from the parent array, each node v becomes a child of parent[v]
        for (int v = 0; v < parent.length; v++) {
            if (parent[v] != -1) {
                children[parent[v]].add(v);
            }
        }
    }

    public void preorder(int node, LinkedList<Integer> TSPtour) {
        // Visit the current node first -> we add the current node to the tour before its children
        TSPtour.add(node);

        // preorder recursively calls itself to visit all the children of the first node
        // children[node] contains all vertices directly connected below this node in the MST.
        for (int child : children[node]) {
            preorder(child, TSPtour);
        }
    }

    public LinkedList<Integer> getPreorderTSPTour() {
        LinkedList<Integer> TSPtour = new LinkedList<>();
        // the traversal starts from the root of the MST
        preorder(root, TSPtour);
        return TSPtour;
    }

    public double computeCycleLength(LinkedList<Integer> TSPtour) {
        double total = 0.0;

        // we sum the distances between consecutive nodes in the tour
        for (int i = 0; i < TSPtour.size() - 1; i++) {
            total += adj[TSPtour.get(i)][TSPtour.get(i + 1)];
        }

        // and then add the distance from the last node back to the first to complete the cycle
        total += adj[TSPtour.getLast()][TSPtour.getFirst()];

        return total;
    }


}