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
    public QueueElement dequeue() throws Exception {
        synchronized(this) {
            if (queue.isEmpty()) throw new Exception("Empty Queue"); // releases lock

            QueueElement elem = queue.removeFirst();
            elem.timeDequeued = System.currentTimeMillis();
            return elem;
        }
    }
}
