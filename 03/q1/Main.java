import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;

public class Main {
    private static AtomicInteger queueElementCount;

    public static void main(String[] args) throws Exception {
        /**
         * Parse command-line arguments
         */
        String helpString = "\nusage: java Main p q n\n" +
            "int p: number of creation/enqueue threads\n" +
            "int q: number of dequeue threads\n" +
            "int n: number of dequeue operations per dequeue thread before program termination";
        String badNumArgs = "\nwrong number of arguments: 3 expected";
        if (args.length != 3) { throw new Exception(badNumArgs + helpString); }
        try {
            int p = Integer.parseInt(args[0]);
            int q = Integer.parseInt(args[0]);
            int n = Integer.parseInt(args[0]);
        } catch(Exception e){
            System.out.println(e.getMessage());
            throw new Exception(helpString);
        }

        // HARDCODED CONSTANT
        boolean verbose = true;

        // how long do the simulations take?
        long startTime = 0;
        long endTime = 0;

        /**
         * Run the expriment with a blocking, synchronized queue
         */
        SynchronizedQueue synq = new SynchronizedQueue();
        AtomicInteger sharedCount = new AtomicInteger(1);
        Thread[] enqueueThreads = new Thread[p];
        Thread[] dequeueThreads = new Thread[q];

        // create the threads needed
        for (int i = 0; i < enqueueThreads.length; i++)
            enqueueThreads[i] = new EnqueueThread(synq, sharedCount);
        for (int i = 0; i < dequeueThreads.length; i++)
            dequeueThreads[i] = new DequeueThread(synq, n);

        // start the threads
        for (Thread t : enqueueThreads)
            t.start();
        for (Thread t : dequeueThreads)
            t.start();

        // wait for the dequeueThreads to finish
        for (Thread t : dequeueThreads)
            t.join();
        // ...and then kill the enqueueThreads
        for (Thread t : enqueueThreads)
            t.shouldContinueEnqueueing = false;

        ArrayList<QueueElements> allDequeuedElements = new ArrayList<>();
        for (Thread t : dequeueThreads)
            allDequeuedElements.addAll(Arrays.asList(t.dequeuedElements));
    }
}
