import java.util.ArrayList;

public class Node {
    public final int id;
    public volatile int color;
    public ArrayList<Node> neighbours;

    public Node(int id) {
        this.id = id;
        this.color = 0;
        this.neighbours = new ArrayList<>();
    }

    public Node(int id, int color) {
        this.id = id;
        this.color = color;
        this.neighbours = new ArrayList<>();
    }

    public Node(int id, int color, ArrayList<Node> neighbours) {
        this.id = id;
        this.color = color;
        this.neighbours = neighbours;
    }
}
