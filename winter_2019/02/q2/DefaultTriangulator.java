import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

public class DefaultTriangulator {
    public static Map<Triangle, Integer> buildTriangulation(List<Point> points) {
        List<Edge> edges = buildEdgeList(points);

        return buildTriangulationFromEdgeList(edges);
    }

    private static List<Edge> buildEdgeList(List<Point> points) {
        List<Edge> edges = new ArrayList<Edge>();

        for (int i = 0; i < points.size(); i++) {
            for (int j = i+1; j < points.size(); j++) {
                Point p1 = points.get(i);
                Point p2 = points.get(j);

                Edge edge = new Edge(p1, p2);

                boolean intersectionFound = false;
                for (Edge other : edges) {
                    Point p3 = other.p1;
                    Point p4 = other.p2;
                    if ((p1 == p3 || p1 == p4) || (p2 == p3 || p2 == p4)) {
                        continue;
                    }
                    if (edge.intersects(other)) {
                        intersectionFound = true;
                        break;
                    }
                }

                if (!intersectionFound) {
                    edges.add(edge);
                }
            }
        }

        return edges;
    }

    private static Map<Triangle, Integer> buildTriangulationFromEdgeList(List<Edge> edges) {
        Map<Triangle, Integer> triangleOrdering = new HashMap<Triangle, Integer>();
        int triangleCount = 0;

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

                    if (edgePoints.size() == 3) {
                        Triangle triangle = new Triangle(e1, e2, e3);
                        triangleOrdering.put(triangle, triangleCount++);
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
            Edge[] t_edges = new Edge[] {t.e1, t.e2, t.e3};
            for (int i = 0; i < t_edges.length; i++) {
                Triangle t_other = edgeToTriangle.get(t_edges[i]);
                if (t != t_other) {
                    switch (i) {
                        case 0:
                            t.nt1 = t_other;
                            break;
                        case 1:
                            t.nt2 = t_other;
                            break;
                        case 2:
                            t.nt3 = t_other;
                            break;
                    }

                    Edge[] t_other_edges = new Edge[] {t_other.e1, t_other.e2, t_other.e3};
                    for (int j = 0; j < t_other_edges.length; j++) {
                        if (t_other_edges[0] == t_edges[i]) {
                            t_other.nt1 = t;
                        } else if (t_other_edges[1] == t_edges[i]) {
                            t_other.nt2 = t;
                        } else if (t_other_edges[2] == t_edges[i]) {
                            t_other.nt3 = t;
                        }
                    }
                }
            }
        }

        return triangleOrdering;
    }
}
