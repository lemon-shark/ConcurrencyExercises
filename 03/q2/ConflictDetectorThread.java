import java.util.List;

public class ConflictDetectorThread extends Thread {

    private List<Node> graph;

    public List<Node> detectedConflicts;

    public ConflictDetectorThread(List<Node> graph)
    { this.graph = graph; }

    @Override
    public void run() {
        // TODO
    }
}
