import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * A Class containing useful graph utilities such as:
 * - createGraph(numNodes, numEdges)
 * - partitionGraph(graph, numPartitions, minNodesPerPartition)
 * - getMaxNodeDegree(graph)
 * - getMaxNodeColor(graph)
 * - validateGraphColoring(graph)
 */
public class MyUtils {
    public static List<Node> createGraph(int numNodes, int numEdges) {
        // create all the nodes
        List<Node> graph = new ArrayList<>();
        for (int i = 0; i < numNodes; i++)
            graph.add(new Node(i));

        // create all the edges
        Random random = new Random();
        for (int i = 0; i < numEdges; i++) {
            Node fromNode = graph.get(random.nextInt(numNodes));
            Node toNode = graph.get(random.nextInt(numNodes));

            // but make sure there are no self-edges and no duplicate edges
            while (fromNode == toNode || fromNode.neighbours.contains(toNode))
                toNode = graph.get(random.nextInt(numNodes));

            // if for all directed edges, the opposite edge also exists, then
            // that's basically the same as undirected edges
            fromNode.neighbours.add(toNode);
            toNode.neighbours.add(fromNode);
        }

        return graph;
    }

    public static List<List<Node>> partitionGraph(
            List<Node> graph,
            int numPartitions,
            int minNodesPerPartition
    ) {
        // how many partitions actually needed, given we want nodesPerPart >= minNodesPerPartition
        int numParts = numPartitions;
        if (!(graph.size() > (minNodesPerPartition * numParts)))
            numParts = (graph.size() / minNodesPerPartition);
        if (graph.size() % minNodesPerPartition != 0) numParts++;
        int nodesPerPart = graph.size() / numParts;

        // partition the graph into numParts subgraphs, each subgraph getting nodesPerPart nodes
        List<List<Node>> subgraphs = new ArrayList<List<Node>>(numParts);
        for (int i = 0; i < numParts-1; i++)
            subgraphs.add(graph.subList(i*nodesPerPart, (i+1)*nodesPerPart));
        subgraphs.add(graph.subList((numParts-1)*nodesPerPart, graph.size()));

        return subgraphs;
    }

    public static int getMaxNodeDegree(List<Node> graph) {
        int maxDegree = 0;
        for (Node node : graph) {
            if (node.neighbours.size() > maxDegree) {
                maxDegree = node.neighbours.size();
            }
        }
        return maxDegree;
    }

    public static int getMaxNodeColor(List<Node> graph) {
        int maxColor = 0;
        for (Node node : graph) {
            if (node.color > maxColor) {
                maxColor = node.color;
            }
        }
        return maxColor;
    }

    public static void validateGraphColoring(List<Node> graph) {
        for (Node node : graph) {
            for (Node neighbour : node.neighbours) {
                if (node.color == neighbour.color) {
                    System.out.println("Invalid Graph!");
                    return;
                }
            }
        }
        // print nothing if graph is valid
    }


    /**
     * Printing functions. Only use these for small graphs!
     * ====================================================
     */

    // adjacency list representation
    public static synchronized void printGraph(List<Node> graph) {
        int numDigits = (int) Math.ceil( Math.log10(graph.size()) );

        StringBuilder str = new StringBuilder();
        for (Node node : graph) {
            str.append(leftPad(node.id, numDigits) + ": ");
            for (Node neighbour : node.neighbours)
                str.append(leftPad(neighbour.id, numDigits) + " ");
            str.append("\n");
        }
        System.out.println(str);
    }

    // same graph representation, but each node shown as 'node.id,node.color'
    public static synchronized void printGraphWithColors(List<Node> graph) {
        int numDigits = (int) Math.ceil( Math.log10(graph.size()) );
        numDigits = numDigits == 0? 1: numDigits;

        StringBuilder str = new StringBuilder();
        for (Node node : graph) {
            str.append(
                    leftPad(node.id, numDigits) + "," +
                    leftPad(String.valueOf(node.color), numDigits) + ": "
            );
            for (Node neighbour : node.neighbours)
                str.append(
                        leftPad(neighbour.id, numDigits) + "," +
                        leftPad(neighbour.color, numDigits) + " "
                );
            str.append("\n");
        }
        System.out.println(str);
    }

    public static String leftPad(String str, int n) {
        return String.format("%" + n + "s", str).replace(' ', '_');
    }

    public static String leftPad(int str, int n) {
        return leftPad(String.valueOf(str), n).replace(' ', '_');
    }
}
