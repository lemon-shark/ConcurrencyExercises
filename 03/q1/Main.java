import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static AtomicInteger queueElementCount;
    
    public static void main(String[] args) throws Exception {
        // parse command-line arguments
        String helpString = "\nusage: java Main p q n\n" +
            " int p: number of creation/enqueue threads\n" +
            " int q: number of dequeue threads\n" +
            " int n: number of dequeue operations per dequeue thread before program termination";
        if (args.length != 3) { throw new Exception(helpString); }
        try {
            int p = Integer.parseInt(args[0]);
            int q = Integer.parseInt(args[0]);
            int n = Integer.parseInt(args[0]);
        } catch(Exception e){ throw new Exception(helpString); }

        // run the expriment with a blocking, synchronized queue
        SynchronizedQueue synq = new SynchronizedQueue();

        // TODO: run the experiment with a non-blocking queue
    }
}
