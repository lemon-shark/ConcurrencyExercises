import java.util.ArrayList;
import java.util.Random;

public class MyUtils {
    public static ArrayList<Node> createGraph(int numNodes, int numEdges) {
        // create all the nodes
        ArrayList<Node> graph = new ArrayList<>();
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

            fromNode.neighbours.add(toNode);
        }

        // all done!
        return graph;
    }

    // adjacency list representation
    public static void printGraph(ArrayList<Node> graph) {
        for (int i = 0; i < graph.size(); i++) {
            Node node = graph.get(i);
            StringBuilder str = new StringBuilder(i + ": ");
            for (int j = 0; j < node.neighbours.size(); j++)
                str.append(node.neighbours.get(j).id + " ");
            System.out.println(str);
        }
    }
}
