import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.Map;

public class Main {
    private static final String helpString = ""
        + "usage: java Main n t [r]"
        + "  int n:            number of points (n > 3)"
        + "  int t:            number of threads (t > 0)"
        + "  int r (optional): RNG seed";

    private static int n, t;
    private static int r = 31;

    public static void main(String[] args) {
        parseArgs(args);

        List<Point> points = generatePoints(n);

        Map<Triangle, Integer> triangleOrdering =
            DefaultTriangulator.buildTriangulation(points);

        ArrayList<Triangle> triangles = triangleSet_to_triangleArrayList(triangleOrdering.keySet());
        TriangulationDrawer.make(triangles, "default.png");
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
        } catch (Exception e) {
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
