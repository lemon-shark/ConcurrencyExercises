import java.util.concurrent.atomic.AtomicReference;

public class LockFreeQueue implements Queue {
    QueueElement sentinel;
    AtomicReference<QueueElement> head;
    AtomicReference<QueueElement> tail;

    public LockFreeQueue() {
        this.sentinel = new QueueElement(-1);
        this.head = new AtomicReference<>(sentinel);
        this.tail = new AtomicReference<>(sentinel);
    }

    public void enqueue(QueueElement elem) {
        while (true) {
            QueueElement last = tail.get();
            QueueElement nextAfterLast = last.next.get();
            if (last == tail.get()) {
                if (nextAfterLast == null) {
                    if (last.next.compareAndSet(nextAfterLast, elem)) {
                        elem.timeEnqueued = System.currentTimeMillis();
                        tail.compareAndSet(last, elem);
                        return;
                    }
                }
                else {
                    tail.compareAndSet(last, nextAfterLast);
                }
            }
        }
    }

    public QueueElement dequeue() throws Exception {
        while (true) {
            QueueElement first = head.get();
            QueueElement last = tail.get();
            QueueElement second = first.next.get();
            if (first == head.get()) {
                if (first == last) {
                    if (second == null) {
                        throw new Exception("Empty Queue");
                    }
                    tail.compareAndSet(last, second);
                }
                else {
                    if (head.compareAndSet(first, second)) {
                        second.timeDequeued = System.currentTimeMillis();
                        return second;
                    }
                }
            }
        }
    }
}
