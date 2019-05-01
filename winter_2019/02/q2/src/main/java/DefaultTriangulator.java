import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;

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

                Edge edge = new Edge(p1, p2);

                boolean cannotAddEdge = false;
                for (Edge existing : edges) {
                    if (edge.equals(existing) || edge.intersects(existing)) {
                        cannotAddEdge = true;
                        break;
                    }
                }

                if (!cannotAddEdge) {
                    edges.add(edge);
                }
            }
        }

        return edges;
    }

    private static Map<Triangle, Integer> buildTriangulation(List<Edge> edges, List<Point> points) {
        Map<Triangle, Integer> triangleOrdering = new HashMap<Triangle, Integer>();

        // add triangles without setting neighbours
        for (int i = 0; i < edges.size(); i++) {
            for (int j = i+1; j < edges.size(); j++) {
                for (int k = j+1; k < edges.size(); k++) {
                    Edge e1 = edges.get(i);
                    Edge e2 = edges.get(j);
                    Edge e3 = edges.get(k);

                    HashSet<Point> edgePoints = new HashSet<Point>();
                    edgePoints.add(e1.p1);
                    edgePoints.add(e1.p2);
                    edgePoints.add(e2.p1);
                    edgePoints.add(e2.p2);
                    edgePoints.add(e3.p1);
                    edgePoints.add(e3.p2);

                    if (edgePoints.size() != 3)
                        continue;

                    Point[] ps = new Point[3];
                    int p_i = 0;
                    for (Point p : edgePoints) {
                        ps[p_i] = p;
                        p_i++;
                    }

                    if (noneWithin(points, ps[0], ps[1], ps[2])) {
                        Triangle triangle = new Triangle(e1, e2, e3);
                        triangleOrdering.put(triangle, (int)triangle.id);
                    }
                }
            }
        }

        System.out.println("----------------------------------------triangles made----------------------------------------------");
        for (Triangle t : triangleOrdering.keySet())
            System.out.println(t);

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
                    switch (i) {
                        case 0:
                            t.nt1 = tOther;
                            break;
                        case 1:
                            t.nt2 = tOther;
                            break;
                        case 2:
                            t.nt3 = tOther;
                            break;
                    }

                    Edge[] tOtherEdges = new Edge[] {tOther.e1, tOther.e2, tOther.e3};
                    for (int j = 0; j < tOtherEdges.length; j++) {
                        if (tOtherEdges[0] == tEdges[i]) {
                            tOther.nt1 = t;
                            break;
                        } else if (tOtherEdges[1] == tEdges[i]) {
                            tOther.nt2 = t;
                            break;
                        } else if (tOtherEdges[2] == tEdges[i]) {
                            tOther.nt3 = t;
                            break;
                        }
                    }
                }
            }
        }

        System.out.println("----------------------------------------neighbours added----------------------------------------------");
        for (Triangle t : triangleOrdering.keySet())
            System.out.println(t);

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
            return true;
        }

        return false;
    }
}
