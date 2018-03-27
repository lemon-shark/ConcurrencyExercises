import java.util.Deque;
import java.util.ArrayDeque;

public class SynchronizedQueue imlements Queue {
    private Deque<Integer> queue = new ArrayDeque<>();

    public SynchronizedQueue(boolean verbose)
    { this.verbose = verbose; }
    private void printlnIfVerbose(String s)
    { if (this.verbose) { System.out.println(s); } }

    public void enqueue(QueueElement elem) {
        synchronized(this) {
            elem.timeEnqueued = System.currentTimeMillis();
            queue.addLast(elem);
        }
    }
    public QueueElement dequeue() {
        synchronized(this) {
            elem.timeDequeued = System.currentTimeMillis();
            queue.removeFirst();
        }
    }
}
