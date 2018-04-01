import java.util.List;
import java.util.HashSet;

public class ColorerThread extends Thread {

    private List<Node> graph;

    public ColorerThread(List<Node> graph)
    { this.graph = graph; }

    @Override
    public void run() {
        HashSet<Integer> neighbourColors;
        for (Node node : graph) {
            neighbourColors = new HashSet<>();

            for (Node neighbour : node.neighbours)
                neighbourColors.add(neighbour.color);

            int possibleColor = 1;
            while (neighbourColors.contains(possibleColor))
                possibleColor++;

            node.color = possibleColor;
        }
    }
}
