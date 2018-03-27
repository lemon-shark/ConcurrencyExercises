import java.util.Deque;
import java.util.ArrayDeque;

public class SynchronizedQueue implements Queue {
    private Deque<QueueElement> queue = new ArrayDeque<>();

    public void enqueue(QueueElement elem) {
        synchronized(this) {
            elem.timeEnqueued = System.currentTimeMillis();
            queue.addLast(elem);
        }
    }
    public QueueElement dequeue() {
        synchronized(this) {
            QueueElement elem = queue.removeFirst();
            elem.timeDequeued = System.currentTimeMillis();
            return elem;
        }
    }
}
