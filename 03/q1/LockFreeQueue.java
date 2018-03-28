public class LockFreeQueue.java implements Queue {
    AtomicReference<QueueElement> head = new AtomicReference<>(null);
    AtomicReference<QueueElement> tail = new AtomicReference<>(null);

    public void enqueue(QueueElement elem) {
        while (true) {
            Node last = tail.get();
            Node nextAfterLast = last.next.get();
            if (last == tail.get()) {
                if (nextAfterLast == null) {
                    if (last.next.compareAndSet(nextAfterLast, node)) {
                        tail.compareAndSet(last, node);
                        node.enqueueTime = System.currentTimeMillis();
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
            QueueElement second = first.next.get();
            QueueElement last = tail.get();
            if (fisrt == head.get()) {
                if (first == last) {
                    if (second == null) {
                        throw new Exception("Empty Queue");
                    }
                    tail.compareAndSet(last, second);
                }
                else {
                    QueueElement retval = second.get();
                    if (head.compareAndSet(first, second)) {
                        retval.timeDequeued = System.currentTimeMillis();
                        return retval;
                    }
                }
            }
        }
    }
}
