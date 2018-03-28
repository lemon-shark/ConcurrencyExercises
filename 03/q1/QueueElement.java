import java.util.concurrent.atomic.AtomicReference;

public class QueueElement {
    public int uniqueId;
    public long timeEnqueued;
    public long timeDequeued;
    public AtomicReference<QueueElement> next;

    public QueueElement(int uniqueId) {
        this.uniqueId = uniqueId;
    }
}
