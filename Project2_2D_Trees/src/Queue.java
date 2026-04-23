//Name: Licia Bordignon
//AndrewId: lbordign

public class Queue {

    private static class QNode {
        TwoDTree.BTNode node;
        QNode next;

        // pre-condition: n is a valid TwoDTree.BTNode
        // post-condition: a QNode is created storing n and next is set to null
        QNode(TwoDTree.BTNode n) {
            node = n;
            next = null;
        }
    }

    private QNode front;
    private QNode rear;

    // pre-condition: none
    // post-condition: an empty queue is created
    public Queue() {
        front = null;
        rear = null;
    }

    // pre-condition: none
    // post-condition: returns true if the queue is empty, false otherwise
    public boolean isEmpty() {
        return front == null;
    }


    // pre-condition: x is a valid TwoDTree.BTNode
    // post-condition: x is added to the rear of the queue
    public void enqueue(TwoDTree.BTNode x) {
        QNode n = new QNode(x);
        if (rear == null) {front = n;}
        else {rear.next = n;}
        rear = n;

    }

    // pre-condition: the queue is not empty
    // post-condition: removes and returns the node at the front of the queue
    public TwoDTree.BTNode dequeue() {
        if (isEmpty()) throw new RuntimeException("Queue already empty");
        TwoDTree.BTNode n = front.node;
        front = front.next;
        if (front == null) rear = null;
        return n;
    }

}
