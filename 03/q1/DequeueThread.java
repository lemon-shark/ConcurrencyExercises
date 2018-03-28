public class DequeueThread extends Thread {
    private Queue q;
    private int n;

    public QueueElement[] dequeuedElements;

    public DequeueThread(Queue q, int n) {
        this.q = q;
        this.n = n;

        this.dequeuedElements = new QueueElement[n];
    }

    @Override
    public void run() {

        for (int i = 0; i < n; i++) {
            try {
                dequeuedElements[i] = q.dequeue();
            } catch(Exception e){ // queue is empty
                i--; // try again
            }

            try { Thread.sleep(10); }
            catch(Exception e){ e.printStackTrace(); }
        }
    }
}
