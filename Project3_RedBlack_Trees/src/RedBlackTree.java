// Name: Licia Bordignon
// AndrewId: lbordign

public class RedBlackTree {

    private RedBlackNode root;
    private final RedBlackNode nil;

    public RedBlackTree() {
        // I create the NIL node, which represents all leaves in the tree
        // Every leaf (nil) is black
        nil = new RedBlackNode(null, -1, RedBlackNode.BLACK, null, null, null);
        // root points to nil for initial empty tree
        root = nil;
    }



    // This method does a binary search to find a given course name in the tree.
    // Starting from the root, it compares the given course name with the current node's course.
    // - If they are equal, it returns the id associated with that course.
    // - If the given name is alphabetically smaller, it moves to the left child.
    // - If it is alphabetically larger, it moves to the right child.
    public Integer getValue(String course) {
        RedBlackNode x = root; // we start from the root
        while (x != nil) {
            int comparison = course.compareTo(x.getCourse()); //course name is the key. being it a string we use compareTo to compare it.

            if (comparison == 0) {
                return x.getId();
            }
            else if (comparison < 0) {
                x = x.getLc(); // update x to left child
            }
            else {
                x = x.getRc(); // update x to right child
            }
        }
        return null; // If id not found (while loops stops when x == nil), the method returns null
    }


    // This method inserts a new (course, id) pair into the Red-Black Tree
    // Pre-condition: memory is available for insertion
    public void insert(String course, int id) {
        // first, we create the new node z (new nodes in a Red-Black Tree are always inserted as RED and thier parent and children are initially set to nil)
        RedBlackNode z = new RedBlackNode(course, id, RedBlackNode.RED, nil, nil, nil);

        RedBlackNode y = nil; // y will keep track of the parent of the new node
        RedBlackNode x = root; // x is used to move down the tree starting from the root

        // Here we simply use binary search to find the correct position to insert the new node
        while (x != nil) {
            y = x; // y becomes the current node (possible parent)
            if (z.getCourse().compareTo(x.getCourse()) < 0) x = x.getLc(); // as before course is the key and we thus use it to walk correctly (alphabetically) down the tree
            else x = x.getRc();
        }

        // We set the parent of the new node to y
        z.setP(y);

        if (y == nil) root = z; // If the tree was empty, the new node becomes the root
        // Otherwise, attach the new node as left or right child
        else if (z.getCourse().compareTo(y.getCourse()) < 0) y.setLc(z);
        else y.setRc(z);

        // As per the initialization of the new node z:
        // - The children of z are both nil
        // - The color of z is RED

        // Then, RBInsertFixup is called to fix any violations of Red-Black Tree properties
        RBInsertFixup(z);
    }

    public void RBInsertFixup(RedBlackNode z) {
        // We only need to fix the tree if z's parent is RED (because z is red as well -> violation)
        while (z.getP().getColor() == RedBlackNode.RED) {
            // Case A: z's parent is the LEFT child of z's grandparent
            if (z.getP() == z.getP().getP().getLc()) {
                RedBlackNode y = z.getP().getP().getRc(); // y is z's uncle (the RIGHT child of the grandparent)
                // Case 1: Uncle is RED -> only recolor needed to fix violation
                if (y.getColor() == RedBlackNode.RED) {
                    // Parent and uncle become BLACK
                    z.getP().setColor(RedBlackNode.BLACK);
                    y.setColor(RedBlackNode.BLACK);
                    // Grandparent becomes RED to maintain the fact that , all paths from the node to descendant leaves contain the same number of black nodes
                    z.getP().getP().setColor(RedBlackNode.RED);

                    // Then we move z up to grandparent and continue fixing upwards
                    z = z.getP().getP();
                // Case 2: Uncle is BLACK -> rotations + recolor to fix violation
                } else {
                    // ZIG-ZAG: If z is a RIGHT child, rotate left first on parent -> this will give us ZIG-ZIG
                    if (z == z.getP().getRc()) {
                        z = z.getP();
                        leftRotate(z);
                    }
                    // ZIG-ZIG: If z is a LEFT child (or it just became one from ZIG-ZAG rotation), recolor and rotate right on grandparent
                    z.getP().setColor(RedBlackNode.BLACK);
                    z.getP().getP().setColor(RedBlackNode.RED);
                    rightRotate(z.getP().getP());
                }

            // Case B: z's parent is the RIGHT child of grandparent
            } else {
                RedBlackNode y = z.getP().getP().getLc(); // y is z's uncle (the LEFT child of the grandparent)
                // Case 1: Uncle is RED -> only recolor needed to fix violation -> same as before
                if (y.getColor() == RedBlackNode.RED) {
                    z.getP().setColor(RedBlackNode.BLACK);
                    y.setColor(RedBlackNode.BLACK);
                    z.getP().getP().setColor(RedBlackNode.RED);
                    z = z.getP().getP();
                // Case 2: Uncle is BLACK -> rotations + recolor to fix violation
                } else {
                    // ZIG-ZAG: If z is a LEFT child, rotate right first on parent -> this will give us ZIG-ZIG
                    if (z == z.getP().getLc()) {
                        z = z.getP();
                        rightRotate(z);
                    }
                    // ZIG-ZIG: If z is a RIGHT child (or it just became one from ZIG-ZAG rotation), recolor and rotate left on grandparent
                    z.getP().setColor(RedBlackNode.BLACK);
                    z.getP().getP().setColor(RedBlackNode.RED);
                    leftRotate(z.getP().getP());
                }
            }
        }
        // finally, the root must always be BLACK
        root.setColor(RedBlackNode.BLACK);
        root.setP(nil);
    }

    //A left rotation moves x down to the left and moves its right child (y) up.
    // Precondition: x.getRc() != nil (x must have a right child)
    // Precondition: the root's parent is always nil
    public void leftRotate(RedBlackNode x) {
        RedBlackNode y = x.getRc();

        // We turn y's left subtree into x's right subtree
        x.setRc(y.getLc());
        if (y.getLc() != nil) y.getLc().setP(x);   // if y's left child is not nil, we also update its parent to x

        // then we link y's parent to x's parent
        y.setP(x.getP());
        if (x.getP() == nil) root = y; // if x was the root, y becomes the new root
        // Otherwise, update x's parent to point to y instead of x
        else if (x == x.getP().getLc()) //if x was the left child
            x.getP().setLc(y);
        else //else (x was right child)
            x.getP().setRc(y);

        // Finally, we set the left child of y to x and the parent of x to y
        y.setLc(x);
        x.setP(y);
    }

    // A right rotation moves x down to the right and moves its left child (y) up.
    // Precondition: x.getLc() != nil (x must have a left child)
    // Precondition: the root's parent is always nil
    public void rightRotate(RedBlackNode x) {
        RedBlackNode y = x.getLc();

        // We turn y's right subtree into x's left subtree
        x.setLc(y.getRc());
        if (y.getRc() != nil) y.getRc().setP(x); // if y's right child is not nil, we also update its parent to x

        // then we link y's parent to x's parent
        y.setP(x.getP());
        if (x.getP() == nil) root = y; // if x was the root, y becomes the new root
        // Otherwise, update x's parent to point to y instead of x
        else if (x == x.getP().getRc()) // if x was the right child
            x.getP().setRc(y);
        else // else (x was left child)
            x.getP().setLc(y);

        // Finally, we set the right child of y to x and the parent of x to y
        y.setRc(x);
        x.setP(y);
    }


}
