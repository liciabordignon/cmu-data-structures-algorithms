//Name: Licia Bordignon
//AndrewId: lbordign

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TwoDTree {

    static class BTNode {
        CrimeRow crime;
        BTNode left;
        BTNode right;

        // pre-condition: crime is a valid CrimeRow object
        // post-condition: a BTNode object is created and it stores the given crime record. The left and right child references are initialized to null
        private BTNode(CrimeRow crime) {
            this.crime = crime;
            this.left = null;
            this.right = null;
        }
    }

    private BTNode root;
    private int size;

    // pre-condition: The String crimeDataLocation contains the path to a file formatted in the exact same way as CrimeLatLonXY.csv
    // post-condition: The 2d tree is constructed and may be printed or queried.
    // This method calls the insert method as a helper to construct the tree
    public TwoDTree(String crimeDataLocation) {
        root = null;
        size = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(crimeDataLocation))) {
            String line;
            br.readLine(); // this is used to skip the heather row

            while ((line = br.readLine()) != null) {
                CrimeRow crime = new CrimeRow(line.split(","));
                root = insert(root, crime, 0);
                size++;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading crime file: " + crimeDataLocation, e);
        }
    }



    // pre-condition: node is either null or the root of a 2D tree, crime is a CrimeRow object with specific X and Y coordinates and depth correctly represents the level of node
    // post-condition: returns the root of the updated tree (either of the subtree or of the entire tree at the initial call)
    private BTNode insert(BTNode node, CrimeRow crime, int depth) {

        if (node == null) return new BTNode(crime);

        boolean using_X = (depth % 2 == 0);

        if (using_X) {
            if (crime.getX() < node.crime.getX()) node.left = insert(node.left, crime, depth + 1);
            else node.right = insert(node.right, crime, depth + 1);
        } else {
            if (crime.getY() < node.crime.getY()) node.left = insert(node.left, crime, depth + 1);
            else node.right = insert(node.right, crime, depth + 1);
        }
        return node;
    }





    // pre-condition: the 2D tree has been constructed
    // post-condition: returns the number of crime records stored in the tree
    public int getSize() {return size;}





    // pre-condition: The 2d tree has been constructed.
    // post-condition: The 2d tree is displayed with a pre-order traversal.
    // Note: This implementation is based on the method presented in the lecture slides.
    public void preOrderPrint() {
        if (root == null) return;
        preOrderPrint(root);
    }
    //helper
    private void preOrderPrint(BTNode node) {
        System.out.println(node.crime);
        if (node.left!= null){preOrderPrint(node.left);}
        if (node.right!= null){preOrderPrint(node.right);}
    }




    //RUN TIME ANALYSIS FOR inOrderPrint()
    // This method has no cases to consider and is Big Theta(n) because it performs an in-order traversal of the entire tree, visiting every node exactly once. The recursion does not revisit the nodes, it just follows the pointers.

    // pre-condition: The 2d tree has been constructed.
    // post-condition: The 2d tree is displayed with an in-order traversal.
    // Note: This implementation is based on the method presented in the lecture slides.
    public void inOrderPrint() {
        if (root == null) return;
        inOrderPrint(root);
    }
    //helper
    private void inOrderPrint(BTNode node) {
        if (node.left != null) {inOrderPrint(node.left);}
        System.out.println(node.crime);
        if (node.right != null) {inOrderPrint(node.right);}
    }





    // pre-condition: The 2d tree has been constructed.
    // post-condition: The 2d tree is displayed with a post-order traversal.
    // Note: This implementation is based on the method presented in the lecture slides.
    public void postOrderPrint() {
        if (root == null) return;
        postOrderPrint(root);
    }
    //helper
    private void postOrderPrint(BTNode node) {
        if (node.left != null) {postOrderPrint(node.left);}
        if (node.right != null) {postOrderPrint(node.right);}
        System.out.println(node.crime);
    }




    //RUN TIME ANALYSIS FOR levelOrderPrint()
    // This method has no cases and is Big Theta(n) because we only traverse the tree once.
    // Each node is enqueued exactly once and dequeued exactly once and the queue operations (enqueue/dequeue) are O(1) each
    // Therefore, the total amount of work is proportional to the number of nodes n.

    // pre-condition: The 2d tree has been constructed.
    // post-condition: The 2d tree is displayed with a level-order traversal.
    // Note: This implementation is based on the method presented in the lecture slides.
    public void levelOrderPrint() {
        if (root == null) return;

        Queue Q =  new Queue();
        Q.enqueue(root);
        while(!Q.isEmpty()){
            BTNode P = Q.dequeue();
            System.out.println(P.crime);

            if (P.left != null) Q.enqueue(P.left);
            if (P.right != null) Q.enqueue(P.right);


        }
    }




    // The following method has no cases and is Big Theta(n).
    // The method first visits every node in the tree once and performs a level-order traversal using a queue while pushing every visited node in a stack.
    // Then, each node stored in the stack is popped.
    // Each node is processed a constant amount of times (constant * n), therefore the total amount of work is proportional to the number of nodes in the tree (we can delete the constant)
    // This means that the running time grows linearly with n, resulting in a time complexity of Big Theta(n).

    // pre-condition: The 2d tree has been constructed.
    // post-condition: The 2d tree is displayed with a reverse level-order traversal.
    public void reverseLevelOrderPrint() {
        if (root == null) return;

        Queue Q =  new Queue();
        Stack S = new Stack();
        // The root is enqueued first to begin the level-order traversal
        Q.enqueue(root);
        // Then each node is dequeued and pushed in a stack
        // This gives us a stack ordered by level
        while(!Q.isEmpty()){
            BTNode P = Q.dequeue();
            S.push(P);
            if (P.right != null) Q.enqueue(P.right); //enqueuing the right child before the left ensures the correct reverse order.
            if (P.left != null) Q.enqueue(P.left);
        }
        // Popping nodes from the stack allows us to print the nodes in reverse level-order.
        // In fact, since the stack follows a LIFO approach (last in-first out), the last element that we pushed in the stack (which are the nodes from the lowest level), will be the first to be popped.
        while (!S.isEmpty()) {
            BTNode T = S.pop();
            System.out.println(T.crime);
        }
    }






    // pre-condition: The 2d tree has been constructed
    // post-condition: A list of 0 or more crimes is returned. These crimes occurred within the rectangular range specified by the four parameters.
    // The pair (x1, y1) is the left bottom of the rectangle. The pair (x2, y2) is the top right of the rectangle.
    public ListOfCrimes findPointsInRange(double x1, double y1, double x2, double y2){

        double xmin = Math.min(x1, x2);
        double xmax = Math.max(x1, x2);
        double ymin = Math.min(y1, y2);
        double ymax = Math.max(y1, y2);

        ListOfCrimes result = new ListOfCrimes();

        rangeSearch(root, xmin, xmax, ymin, ymax, 0, result);
        return result;
    }
    //helper
    private void rangeSearch(BTNode node, double xmin, double xmax, double ymin, double ymax, int depth, ListOfCrimes result) {

        if (node == null) return;
        result.incrementExamined();

        double x = node.crime.getX();
        double y = node.crime.getY();

        // If the point is inside the rectangle, we add it directly to the list
        if (x >= xmin && x <= xmax && y >= ymin && y <= ymax) {
            result.append(node.crime);
        }

        boolean using_X = (depth % 2 == 0);

        if (using_X) {
            // At an X-splitting level, the vertical splitting line is defined by the x value of the node.
            // If both of the if statements are true, it means that our splitting line hits the rectangle: we need to go down both ways because both subtrees may contain valid points
            // If only the first if statement is true, then the rectangle lies entirely to the left of our splitting line, we only need to go down the left subtree
            // If only the second if statement is true, then the rectangle lies entirely to the right of our splitting line, we only need to go down the right subtree
            if (xmin <= x) rangeSearch(node.left, xmin, xmax, ymin, ymax, depth + 1, result);
            if (x <= xmax) rangeSearch(node.right, xmin, xmax, ymin, ymax, depth + 1, result);
        } else {
            // At a Y-splitting level, the splitting line is horizontal and is defined by the y value of the node.
            // The same logic of above is applied.
            if (ymin <= y) rangeSearch(node.left, xmin, xmax, ymin, ymax, depth + 1, result);
            if (y <= ymax) rangeSearch(node.right, xmin, xmax, ymin, ymax, depth + 1, result);
        }
    }






    // pre-condition: the 2d tree has been constructed.
    // The (x1,y1) pair represents a point in space near Pittsburgh and in the state plane coordinate system.
    // post-condition: the distance in feet to the nearest node is returned in Neighbor.
    // In addition, the Neighbor object contains a reference to the nearest neighbor in the tree.
    public Neighbor nearestNeighbor(double x1, double y1){
        Neighbor best = new Neighbor();// distance is initialized to +infinity

        neighborSearch(root, x1, y1, 0, best);
        return best;
    }
    //helper
    private void neighborSearch(BTNode node,  double x1, double y1, int depth, Neighbor best) {

        if (node == null) return; // Base case is a empty subtree
        best.incrementLookedAt();

        // Euclidean distance between the point (x1,y1) and the current node
        double distance_x = node.crime.getX() - x1;
        double distance_y = node.crime.getY() - y1;
        double distance = Math.sqrt(distance_x * distance_x + distance_y * distance_y);

        // we update best if the current distance < best
        if (distance < best.getDistance()) {
            best.setDistance(distance);
            best.setCrime(node.crime);
        }

        boolean using_X = (depth % 2 == 0);

        BTNode nearChild;
        BTNode farChild;
        double splitDistance;

        // as for the insert, depending on the level we either look at the x coordinate or at the y coordinate
        if (using_X) {
            // We first look at the subtree that lies on the same side of the splitting line as the query point.
            if (x1 < node.crime.getX()) {
                nearChild = node.left;
                farChild = node.right;
            } else {
                nearChild = node.right;
                farChild = node.left;
            }
            // Distance from the point (x1,y1) to the vertical splitting line
            splitDistance = Math.abs(node.crime.getX() - x1);
        } else {
            if (y1 < node.crime.getY()) {
                nearChild = node.left;
                farChild = node.right;
            } else {
                nearChild = node.right;
                farChild = node.left;
            }
            // Distance from the point (x1,y1) to the horizontal splitting line
            splitDistance = Math.abs(node.crime.getY() - y1);
        }
        // We recursively search the subtree that is closer to the point (x1,y1)
        neighborSearch(nearChild, x1, y1, depth + 1, best);

        // We look at the far side only if it could contain a closer point
        // Essentially, if the distance to the split is grater than or equal to the current best, then no point on the far side can be closer. In this case we do not look at it.
        if (splitDistance < best.getDistance()) {
            neighborSearch(farChild, x1, y1, depth + 1, best);
        }
    }

}
