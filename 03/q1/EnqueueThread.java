import java.util.concurrent.atomic.AtomicInteger;

public class EnqueueThread extends Thread {
    Queue q;
    AtomicInteger sharedCount;

    public volatile boolean shouldContinueEnqueuing = true;

    public EnqueueRunnable(Queue q, AtomicInteger sharedCount) {
        this.q = q;
        this.sharedCount = sharedCount;
    }

    @Override
    public void run() {
        while (shouldContinueEnqueuing) {
            q.enqueue(new QueueElement(sharedCount.getAndIncrement()));
            Thread.sleep(10);
        }
    }
}
