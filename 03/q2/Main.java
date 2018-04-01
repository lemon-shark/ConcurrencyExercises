import java.util.ArrayList;
import java.util.List;

/**
 * Contains the main logic of the multithreaded graph coloring experiment.
 *
 * Outputs a line of comma-separated values:
 * 'numNodes,numEdges,numThreads,totalTimeColoring,maxNodeDegree,maxNodeColor'
 */
public class Main {
    static int n, e, t; // number of nodes, number of edges, number of threads
    static long startTime, endTime;
    static List<Node> graph, conflicting;
    static int maxNodeDegree, maxNodeColor;

    static final int minNodesPerThread = 1000;

    public static void main(String[] args) throws Exception {
        // initialization
        parseCommandLineArguments(args);
        graph = MyUtils.createGraph(n, e);

        // run multithreaded graph coloring experiment
        startTimer();
        conflicting = graph;
        while (conflicting.size() > 0) {
            assign();
            conflicting = detectConflicts();
        }
        endTimer();

        // collect and display results
        maxNodeDegree = MyUtils.getMaxNodeDegree(graph);
        maxNodeColor = MyUtils.getMaxNodeColor(graph);
        MyUtils.validateGraphColoring(graph); // print "Invalid Graph!" if graph coloring is invalid
        printResults();
    }

    public static void assign() throws InterruptedException {
        List<List<Node>> conflictings = MyUtils.partitionGraph(conflicting, t, minNodesPerThread);

        Thread[] colorers = new ColorerThread[conflictings.size()];
        for (int i = 0; i < conflictings.size(); i++)
            colorers[i] = new ColorerThread(conflictings.get(i));
        for (Thread t : colorers)
            t.start();
        for (Thread t : colorers)
            t.join();
    }

    public static List<Node> detectConflicts() throws InterruptedException {
        List<List<Node>> conflictings = MyUtils.partitionGraph(conflicting, t, minNodesPerThread);

        ConflictDetectorThread[] conflictDetectors = new ConflictDetectorThread[conflictings.size()];
        for (int i = 0; i < conflictings.size(); i++)
            conflictDetectors[i] = new ConflictDetectorThread(conflictings.get(i));
        for (Thread t : conflictDetectors)
            t.start();
        for (Thread t : conflictDetectors)
            t.join();

        // collect all nodes with bad colorings detected by all ConflictDetectorThreads
        List<Node> detectedConflicts = new ArrayList<Node>();
        for (ConflictDetectorThread t : conflictDetectors)
            detectedConflicts.addAll(t.detectedConflicts);
        return detectedConflicts;
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
        int[] values = new int[] {n, e, t, (int)(endTime-startTime), maxNodeDegree, maxNodeColor};

        String[] strings = new String[values.length];
        for (int i = 0; i < values.length; i++) strings[i] = String.valueOf(values[i]);

        System.out.println(String.join(",", strings));
    }
}
