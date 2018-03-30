import java.util.ArrayList;
import java.util.List;

public class Main {
    static int n, e, t;
    static long startTime, endTime;
    static List<Node> graph, conflicting;

    static final int minNodesPerThread = 20;

    public static void main(String[] args) throws Exception {
        parseCommandLineArguments(args);

        graph = MyUtils.createGraph(n, e);
        //MyUtils.printGraph(graph);
        conflicting = graph;

        startTimer();
        while (conflicting.size() > 0) {
            //assign();
            //conflicting = detectConflicts();
            break;
        }
        endTimer();

        printResults();
    }

    public static void assign() throws InterruptedException {
        List<List<Node>> conflictings = partitionConflicting();

        Thread[] colorers = new ColorerThread[conflictings.size()];
        for (int i = 0; i < conflictings.size(); i++)
            colorers[i] = new ColorerThread(conflictings.get(i));
        for (Thread t : colorers)
            t.start();
        for (Thread t : colorers)
            t.join();
    }

    public static List<Node> detectConflicts() throws InterruptedException {
        List<List<Node>> conflictings = partitionConflicting();

        ConflictDetectorThread[] conflictDetectors = new ConflictDetectorThread[conflictings.size()];
        for (int i = 0; i < conflictings.size(); i++)
            conflictDetectors[i] = new ConflictDetectorThread(conflictings.get(i));
        for (Thread t : conflictDetectors)
            t.start();
        for (Thread t : conflictDetectors)
            t.join();

        List<Node> detectedConflicts = new ArrayList<Node>();
        for (ConflictDetectorThread t : conflictDetectors)
            detectedConflicts.addAll(t.detectedConflicts);
        return detectedConflicts;
    }

    public static List<List<Node>> partitionConflicting() {
        // how many threads actually needed, given we want nodesPerThread >= minNodesPerThread
        int numThreads = t;
        if (!(conflicting.size() > (minNodesPerThread * t)))
            numThreads = (conflicting.size() / minNodesPerThread) + 1;
        int nodesPerThread = conflicting.size() / numThreads;

        // partition the graph into numThreads partitions
        List<List<Node>> conflictings = new ArrayList<List<Node>>(numThreads);
        for (int i = 0; i < numThreads-1; i++)
            conflictings.add(conflicting.subList(i*nodesPerThread, (i+1)*nodesPerThread));
        conflictings.add(conflicting.subList((numThreads-1)*nodesPerThread, conflicting.size()));

        return conflictings;
    }

    public static void parseCommandLineArguments(String[] args) throws Exception {
        String helpMessage =
            "usage: java Main n e t\n" +
            "int n: the number of nodes in the graph\n" +
            "       n must be greater than 3\n" +
            "int e: the number of undirected edges in the graph\n" +
            "       e must be greater than 0\n" +
            "int t: the number of threads to use to color the graph\n";
        String badNumArgsMessage =
            "wrong number of arguments, expected 3\n\n";
        String notEnoughNodes =
            "number of nodes n must be greater than 3";
        String notEnoughEdges =
            "number of edges e must be greater than 0";
        String notEnoughThreads =
            "number of threads t must be greater than 0";

        if (args.length != 3)
            throw new Exception(badNumArgsMessage + helpMessage);

        try {
            n = Integer.parseInt(args[0]);
            e = Integer.parseInt(args[1]);
            t = Integer.parseInt(args[2]);

            if (!(n > 3)) throw new Exception(notEnoughNodes);
            if (!(e > 0)) throw new Exception(notEnoughEdges);
            if (!(t > 0)) throw new Exception(notEnoughThreads);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage() + "\n");
            throw new Exception(helpMessage);
        }
    }

    public static void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public static void endTimer() {
        endTime = System.currentTimeMillis();
    }

    public static void printResults() {
        System.out.println(n + "," + e + "," + t + "," + (endTime - startTime));
    }
}
