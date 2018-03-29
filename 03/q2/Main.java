import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        /**
         * Parse command-line arguments
         */
        String helpMessage =
            "usage: java Main n e t\n" +
            "int n: the number of nodes in the graph\n" +
            "       n must be greater than 3\n" +
            "int e: the number of undirected edges in the graph\n" +
            "       e must be greater than 0\n" +
            "int t: the number of threads to use to color the graph\n";
        String badNumArgsMessage =
            "wrong number of arguments, expected 3\n\n";

        if (args.length != 3)
            throw new Exception(badNumArgsMessage + helpMessage);

        int n, e, t;
        try {
            n = Integer.parseInt(args[0]);
            e = Integer.parseInt(args[1]);
            t = Integer.parseInt(args[2]);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage() + "\n");
            throw new Exception(helpMessage);
        }

        /**
         * run concurrent graph coloring algorithm
         */
        ArrayList<Node> graph = MyUtils.createGraph(n, e);
        ArrayList<Node> conflicting = graph;

        //MyUtils.printGraph(graph);

        long startTime = System.currentTimeMillis();

        while (conflicting.size() > 0) {
            break;
            // TODO: the algorithm
        }

        System.out.println(
            n + "," + e + "," + t + "," + (System.currentTimeMillis() - startTime)
        );
    }
}
