import java.util.List;
import java.util.ArrayList;

public class ConflictDetectorThread extends Thread {

    private List<Node> graph;

    public List<Node> detectedConflicts;

    public ConflictDetectorThread(List<Node> graph)
    { this.graph = graph; }

    @Override
    public void run() {
        List<Node> nodesWithConflicts = new ArrayList<>();
        for (Node node : graph) {
            for (Node neighbour : node.neighbours) {
                if (node.color == neighbour.color) {
                    nodesWithConflicts.add(node);
                    break;
                }
            }
        }
        detectedConflicts = nodesWithConflicts;
    }
}
