import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

public class DelaunayTriangulator {
    private volatile int globalFlipCount = 0;

    public int doDelaunayTriangulation(Map<Triangle, Integer> triangleOrdering, int n)
            throws InterruptedException {
        globalFlipCount = 0;

        Thread[] ts = new Thread[n];
        for (int i = 0; i < n; i++)
            ts[i] = new Thread(() -> {
                int localFlipCount = 0;
                int currFlipCount = 0;
                do {
                    currFlipCount = 0;

                    for (Triangle triangle : triangleOrdering.keySet()) {
                        int triangleOrdinal = triangleOrdering.get(triangle);

                        Triangle[] neighbours = new Triangle[] {
                            triangle.nt1, triangle.nt2, triangle.nt3
                        };

                        for (Triangle neighbour : neighbours) {
                            int neighbourOrdinal = triangleOrdering.get(neighbour);

                            if (triangleOrdinal < neighbourOrdinal) {
                                triangle.lock(); neighbour.lock();
                            } else {
                                neighbour.lock(); triangle.lock();
                            }

                            {
                                Edge sharedEdge = getSharedEdge(triangle, neighbour);

                                if (formsConvexParallelogram(triangle, neighbour, sharedEdge) &&
                                    needsFlip(triangle, neighbour, sharedEdge))
                                {
                                    flip(triangle, neighbour, sharedEdge);
                                    currFlipCount++;
                                }
                            }

                            triangle.unlock(); neighbour.unlock();
                        }
                    }

                    localFlipCount += currFlipCount;
                } while (currFlipCount > 0);

                globalFlipCount += localFlipCount;
            });

        for (Thread t : ts)
            t.start();
        for (Thread t : ts)
            t.join();

        return globalFlipCount;
    }

    private Edge getSharedEdge(Triangle t1, Triangle t2) {
        Edge[] edges = new Edge[] {
            t1.e1, t1.e2, t1.e3,
            t2.e1, t2.e2, t2.e3
        };
        HashMap<Edge, Integer> edgeCount = new HashMap<>();
        for (Edge e : edges) {
            if (edgeCount.containsKey(e)) {
                int currCount = edgeCount.get(e);
                edgeCount.put(e, currCount+1);
            } else {
                edgeCount.put(e, 1);
            }
        }
        for (Edge e : edgeCount.keySet()) {
            if (edgeCount.get(e) == 2) {
                return e;
            }
        }
        return null;
    }

    private boolean formsConvexParallelogram(Triangle t1, Triangle t2, Edge sharedEdge) {
        // FIXME
        return true;
    }

    private boolean needsFlip(Triangle t1, Triangle t2, Edge sharedEdge) {
        double angleT1;
        if (t1.e1 == sharedEdge) {
            angleT1 = t1.e2.innerAngleWith(t1.e3);
        } else if (t1.e2 == sharedEdge) {
            angleT1 = t1.e1.innerAngleWith(t1.e3);
        } else {
            angleT1 = t1.e1.innerAngleWith(t1.e2);
        }

        double angleT2;
        if (t2.e1 == sharedEdge) {
            angleT2 = t2.e2.innerAngleWith(t2.e3);
        } else if (t2.e2 == sharedEdge) {
            angleT2 = t2.e1.innerAngleWith(t2.e3);
        } else {
            angleT2 = t2.e1.innerAngleWith(t2.e2);
        }

        return angleT1 + angleT2 > Math.PI;
    }

    private void flip(Triangle t1, Triangle t2, Edge sharedEdge) {
        Point sharedPoint1 = sharedEdge.p1;
        Point sharedPoint2 = sharedEdge.p2;

        // get opposite points for new edge
        HashSet<Point> sharedPoints = new HashSet<>();
        sharedPoints.add(sharedPoint1);
        sharedPoints.add(sharedPoint2);

        Point oppositePoint1 = null;
        for (Edge e : new Edge[] {t1.e1, t1.e2, t1.e3}) {
            for (Point p : new Point[] {e.p1, e.p2}) {
                if (!sharedPoints.contains(p)) {
                    oppositePoint1 = p;
                }
            }
        }

        Point oppositePoint2 = null;
        for (Edge e : new Edge[] {t2.e1, t2.e2, t2.e3}) {
            for (Point p : new Point[] {e.p1, e.p2}) {
                if (!sharedPoints.contains(p)) {
                    oppositePoint2 = p;
                }
            }
        }

        Edge newEdge = new Edge(oppositePoint1, oppositePoint2);

        Triangle[] triangles = new Triangle[] {t1, t2};

        //String s = ""+
            //"---------------------------------------FLIP( t1, t2 )---------------------------------"+
            //"-----------";
        //System.out.println(s);
        //System.out.println(t1);
        //System.out.println(t2);

        // remove shared edge from two triangles
        for (Triangle t : triangles) {
            for (Edge e : new Edge[] {t.e1, t.e2, t.e3}) {
                if (sharedPoints.contains(e.p1) && sharedPoints.contains(e.p2)) {
                    if (t.e1 == e) {
                        t.e1 = null;
                    } else if (t.e2 == e) {
                        t.e2 = null;
                    } else if (t.e3 == e) {
                        t.e3 = null;
                    }
                }
            }
        }
        //System.out.println(t1);
        //System.out.println(t2);

        // correct triangles for new shared edge

        Edge[] newTriangleEdges = new Edge[3];
        Triangle[] newNeighbours = new Triangle[3];


        // get new triangle 1
        int edgeCount = 0;
        for (Triangle t : triangles) {
            Edge[] edges = new Edge[] {t.e1, t.e2, t.e3};
            Triangle[] neighbours = new Triangle[] {t.nt1, t.nt2, t.nt3};

            for (int iEdge = 0; iEdge < edges.length; iEdge++) {
                Edge edge = edges[iEdge];
                Triangle neighbour = neighbours[iEdge];

                for (Point p : new Point[] {edge.p1, edge.p2}) {
                    if (p == sharedPoint1) {
                        newTriangleEdges[edgeCount] = edge;
                        newNeighbours[edgeCount] = neighbour;
                        edgeCount++;

                        if (edgeCount == 1) {
                            newTriangleEdges[edgeCount] = sharedEdge;
                            newNeighbours[edgeCount] = t2;
                        }
                    }
                }
            }
        }

        t1.e1 = newTriangleEdges[0];
        t1.e2 = newTriangleEdges[1];
        t1.e3 = newTriangleEdges[2];
        t1.nt1 = newNeighbours[0];
        t1.nt2 = newNeighbours[1];
        t1.nt3 = newNeighbours[2];

        // get new triangle 2
        edgeCount = 0;
        for (Triangle t : triangles) {
            Edge[] edges = new Edge[] {t.e1, t.e2, t.e3};
            Triangle[] neighbours = new Triangle[] {t.nt1, t.nt2, t.nt3};

            for (int iEdge = 0; iEdge < edges.length; iEdge++) {
                Edge edge = edges[iEdge];
                Triangle neighbour = neighbours[iEdge];

                for (Point p : new Point[] {edge.p1, edge.p2}) {
                    if (p == sharedPoint2) {
                        newTriangleEdges[edgeCount] = edge;
                        newNeighbours[edgeCount] = neighbour;
                        edgeCount++;

                        if (edgeCount == 2) {
                            newTriangleEdges[edgeCount] = sharedEdge;
                            newNeighbours[edgeCount] = t2;
                        }
                    }
                }
            }
        }

        t2.e1 = newTriangleEdges[0];
        t2.e2 = newTriangleEdges[1];
        t2.e3 = newTriangleEdges[2];
        t2.nt1 = newNeighbours[0];
        t2.nt2 = newNeighbours[1];
        t2.nt3 = newNeighbours[2];
    }
}
