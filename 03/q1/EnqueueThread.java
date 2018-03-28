import java.util.concurrent.atomic.AtomicInteger;

public class EnqueueThread extends Thread {
    Queue q;
    AtomicInteger sharedCount;

    public volatile boolean shouldContinueEnqueueing = true;

    public EnqueueThread(Queue q, AtomicInteger sharedCount) {
        this.q = q;
        this.sharedCount = sharedCount;
    }

    @Override
    public void run() {
        while (shouldContinueEnqueueing) {
            q.enqueue(new QueueElement(sharedCount.getAndIncrement()));

            try { Thread.sleep(10); }
            catch(Exception e){ e.printStackTrace(); }
        }
    }
}
