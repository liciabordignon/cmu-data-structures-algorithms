//Name: Licia Bordignon
//AndrewId: lbordign

public class Stack {

    private static class SNode {
        TwoDTree.BTNode node;
        SNode next;

        // pre-condition: n is a TwoDTree.BTNode
        // post-condition: an SNode is created storing n and next is set to null
        SNode(TwoDTree.BTNode n) {
            node = n;
            next = null;
        }
    }

    private SNode top;

    // pre-condition: none
    // post-condition: an empty stack is created
    public Stack() {
        top = null;
    }

    // pre-condition: none
    // post-condition: returns true if the stack is empty, false otherwise
    public boolean isEmpty(){
        return top == null;
    }


    // pre-condition: x is a valid TwoDTree.BTNode
    // post-condition: x is pushed onto the top of the stack
    public void push(TwoDTree.BTNode x){
        SNode n = new SNode(x);
        n.next = top;
        top = n;
    }


    // pre-condition: the stack is not empty
    // post-condition: removes and returns the node at the top of the stack
    public TwoDTree.BTNode pop(){
        if (isEmpty()) throw new RuntimeException("Stack is empty");
        TwoDTree.BTNode n = top.node;
        top = top.next;
        return n;
    }


}
