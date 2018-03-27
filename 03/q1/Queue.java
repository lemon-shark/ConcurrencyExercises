public interface Queue {
    private boolean verbose;
    public Queue(boolean verbose);
    public void enqueue(QueueElement elem);
    public QueueElement dequeue();
    private void printlnIfVerbose(String s);
}
