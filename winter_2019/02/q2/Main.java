import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.Map;

public class Main {
    private static final String helpString = ""
        + "\nusage: java Main n t [r]"
        + "\n  int n:            number of points (n > 3)"
        + "\n  int t:            number of threads (t > 0)"
        + "\n  int r (optional): RNG seed";

    private static int n, t;
    private static int r = 31;

    public static void main(String[] args) {
        parseArgs(args);

        List<Point> points = generatePoints(n);

        List<Edge> edges = DefaultTriangulator.buildEdgeList(points);

        TriangulationDrawer.make2(edges, "default2.png");
    }

    private static void parseArgs(String[] args) {
        if (args.length > 3 || args.length < 2) {
            System.out.println(helpString);
            System.exit(1);
        }

        try {
            n = Integer.parseInt(args[0]);
            t = Integer.parseInt(args[1]);

            if (args.length == 3) {
                r = Integer.parseInt(args[2]);
            }

            if (!(n > 3)) {
                System.out.println("require n > 3");
                System.out.println(helpString);
                System.exit(1);
            }
            if (!(t > 0)) {
                System.out.println("require t > 0");
                System.out.println(helpString);
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(helpString);
            System.exit(1);
        }
    }

    private static List<Point> generatePoints(int n) {
        List<Point> points = new ArrayList<Point>();

        for (int i = 0; i < n; i++) {
            points.add(Point.createRandomPoint());
        }

        return points;
    }

    private static ArrayList<Triangle> triangleSet_to_triangleArrayList(Set<Triangle> triangleSet) {
        ArrayList<Triangle> retval = new ArrayList<>();

        for (Triangle t : triangleSet) {
            retval.add(t);
        }

        return retval;
    }
}
