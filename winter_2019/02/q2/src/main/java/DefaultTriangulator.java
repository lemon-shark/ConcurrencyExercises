import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;
import java.util.Arrays;

public class DefaultTriangulator {
    public static Map<Triangle, Integer> buildTriangulation(List<Point> points) {
        List<Edge> edges = buildEdges(points);

        return buildTriangulation(edges, points);
    }

    private static List<Edge> buildEdges(List<Point> points) {
        List<Edge> edges = new ArrayList<Edge>();

        for (int i = 0; i < points.size(); i++) {
            for (int j = i+1; j < points.size(); j++) {
                Point p1 = points.get(i);
                Point p2 = points.get(j);
                if (p1 == p2) continue;

                boolean cannotAddEdge = false;
                for (Edge existing : edges) {
                    if (existing.intersects(p1, p2)) {
                        cannotAddEdge = true;
                        break;
                    }
                }

                if (!cannotAddEdge) {
                    edges.add(new Edge(p1, p2));
                }
            }
        }

        return edges;
    }

    private static Map<Triangle, Integer> buildTriangulation(List<Edge> edges, List<Point> points) {
        Map<Triangle, Integer> triangleOrdering = new HashMap<Triangle, Integer>();

        // create triangles without setting neighbours
        for (int i = 0; i < edges.size(); i++) {
            for (int j = i+1; j < edges.size(); j++) {
                for (int k = j+1; k < edges.size(); k++) {
                    Edge e1 = edges.get(i);
                    Edge e2 = edges.get(j);
                    Edge e3 = edges.get(k);

                    HashSet<Point> triPoints = new HashSet<Point>(Arrays.asList(new Point[] {
                        e1.p1, e1.p2,
                        e2.p1, e2.p2,
                        e3.p1, e3.p2
                    }));

                    if (triPoints.size() != 3)
                        continue;
                    Point[] ps = new Point[3];
                    triPoints.toArray(ps);

                    if (noneWithin(points, ps[0], ps[1], ps[2])) {
                        Triangle triangle = new Triangle(e1, e2, e3);
                        triangleOrdering.put(triangle, (int)triangle.id);
                    }
                }
            }
        }

        // add neighbours to triangles
        HashMap<Edge, Triangle> edgeToTriangle = new HashMap<Edge, Triangle>();
        for (Triangle t : triangleOrdering.keySet()) {
            edgeToTriangle.put(t.e1, t);
            edgeToTriangle.put(t.e2, t);
            edgeToTriangle.put(t.e3, t);
        }
        for (Triangle t : triangleOrdering.keySet()) {
            Edge[] tEdges = new Edge[] {t.e1, t.e2, t.e3};
            for (int i = 0; i < tEdges.length; i++) {
                Triangle tOther = edgeToTriangle.get(tEdges[i]);
                if (t != tOther) {
                    if (i == 0)
                        t.nt1 = tOther;
                    else if (i == 1)
                        t.nt2 = tOther;
                    else if (i == 2)
                        t.nt3 = tOther;

                    if (tOther.e1 == tEdges[i])
                        tOther.nt1 = t;
                    else if (tOther.e2 == tEdges[i])
                        tOther.nt2 = t;
                    else if (tOther.e3 == tEdges[i])
                        tOther.nt2 = t;
                }
            }
        }

        return triangleOrdering;
    }

    /**
     * true if none of innerPoints are within Triangle formed by p1, p2, p3
     */
    private static boolean noneWithin(Collection<Point> innerPoints, Point p1, Point p2, Point p3) {
        double x1=p1.getX();
        double x2=p2.getX();
        double x3=p3.getX();
        double y1=p1.getY();
        double y2=p2.getY();
        double y3=p3.getY();

        double y23 = y2 - y3;
        double x32 = x3 - x2;
        double y31 = y3 - y1;
        double x13 = x1 - x3;
        double det = y23 * x13 - x32 * y31;
        double minD = Math.min(det, 0);
        double maxD = Math.max(det, 0);

        for (Point p : innerPoints) {
            if (p == p1 || p == p2 || p == p3)
                continue;
            double x = p.getX();
            double y = p.getY();

            double dx = x - x3;
            double dy = y - y3;
            double a = y23 * dx + x32 * dy;
            if (a < minD || a > maxD)
                continue;
            double b = y31 * dx + x13 * dy;
            if (b < minD || b > maxD)
                continue;
            double c = det - a - b;
            if (c < minD || c > maxD)
                continue;
            return false;
        }

        return true;
    }
}
