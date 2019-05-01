import java.util.Random;

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
    private static long r = 31;
    private static Random random;

    public static void main(String[] args) throws InterruptedException {
        parseArgs(args);

        List<Point> points = generatePoints(n);

        Map<Triangle, Integer> triangleOrdering = DefaultTriangulator.buildTriangulation(points);

        TriangulationDrawer.make(triangleOrdering.keySet(), "default.png");

        new DelaunayTriangulator().doDelaunayTriangulation(triangleOrdering, t);

        TriangulationDrawer.make(triangleOrdering.keySet(), "delaunay.png");
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
                r = Long.parseLong(args[2]);
            }
            random = new Random(r);

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
            double x = random.nextDouble();
            double y = random.nextDouble();
            points.add(new Point(x, y));
        }

        return points;
    }
}
