// Name: Licia Bordignon
// AndrewId: lbordign

public class RedBlackNode {
    //I define two constants that represent node colors (used for readability instead of raw 0/1 values)
    public static final int RED = 1;
    public static final int BLACK = 0;

    // each node stores one dictionary entry (key-value pair)
    private String course; // course name -> key
    private int id; // course id -> value

    private int color; // RED= 1 and BLACK= 0
    private RedBlackNode p; // parent
    private RedBlackNode lc; // left child
    private RedBlackNode rc; // right child

    //constructor
    // precondition: color must be either RED or BLACK
    public RedBlackNode(String course, int id, int color, RedBlackNode p, RedBlackNode lc, RedBlackNode rc) {
        this.course = course;
        this.id = id;
        this.color = color;
        this.p = p;
        this.lc = lc;
        this.rc = rc;
    }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public RedBlackNode getP() { return p; }
    public void setP(RedBlackNode p) { this.p = p; }

    public RedBlackNode getLc() { return lc; }
    public void setLc(RedBlackNode lc) { this.lc = lc; }

    public RedBlackNode getRc() { return rc; }
    public void setRc(RedBlackNode rc) { this.rc = rc; }


    @Override
    public String toString() {
        return "RedBlackNode{" + "course='" + course + '\'' + ", id=" + id +
                ", color=" + (color == RED ? "RED" : "BLACK") + '}';
    }
}
