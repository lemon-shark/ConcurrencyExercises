import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    private static AtomicInteger queueElementCount;

    public static void main(String[] args) throws Exception {
        /**
         * Parse command-line arguments
         */
        String helpString = "\nusage: java Main p q n [--silent]\n" +
            "int p: number of creation/enqueue threads\n" +
            "int q: number of dequeue threads\n" +
            "int n: number of dequeue operations per dequeue thread before program termination\n" +
            "flag --silent: program will not output timestamp of every op, instead just start and end time";
        String badNumArgs = "\nwrong number of arguments: 3 required plus optional --silent flag";

        int p, q, n;
        boolean verbose = true;
        if (args.length < 3 || args.length > 4) {
            throw new Exception(badNumArgs + helpString);
        }
        try {
            p = Integer.parseInt(args[0]);
            q = Integer.parseInt(args[0]);
            n = Integer.parseInt(args[0]);

            if (args.length == 4) verbose = false;
        } catch(Exception e){
            System.out.println(e.getMessage());
            throw new Exception(helpString);
        }

        long startTime = 0;
        long endTime = 0;
        String[] ops = new String[] {"enq ", "deq "};

        /**
         * Run the expriment with a blocking, synchronized queue
         */
        SynchronizedQueue synq = new SynchronizedQueue();
        AtomicInteger sharedCount = new AtomicInteger(1);
        EnqueueThread[] enqueueThreads = new EnqueueThread[p];
        DequeueThread[] dequeueThreads = new DequeueThread[q];

        // create the threads needed
        for (int i = 0; i < enqueueThreads.length; i++)
            enqueueThreads[i] = new EnqueueThread(synq, sharedCount);
        for (int i = 0; i < dequeueThreads.length; i++)
            dequeueThreads[i] = new DequeueThread(synq, n);

        // start the threads
        for (EnqueueThread t : enqueueThreads)
            t.start();
        for (DequeueThread t : dequeueThreads)
            t.start();

        // wait for the dequeueThreads to finish
        for (DequeueThread t : dequeueThreads)
            t.join();
        // ...and then kill the enqueueThreads
        for (EnqueueThread t : enqueueThreads)
            t.shouldContinueEnqueueing = false;

        // collect all the dequeuedElements
        ArrayList<QueueElement> allDequeuedElements = new ArrayList<>();
        for (DequeueThread t : dequeueThreads)
            allDequeuedElements.addAll(Arrays.asList(t.dequeuedElements));
    }
}
