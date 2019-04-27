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


        for (Point p1 : points) {
            for (Point p2: points) {
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

        return triangleOrdering;
    }
}