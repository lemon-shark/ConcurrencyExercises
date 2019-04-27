import java.util.Map;
import java.util.HashMap;

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
                                if (needsFlip(triangle, neighbour, sharedEdge)) {
                                    flip(triangle, neighbour, sharedEdge);
                                }
                            }

                            triangle.unlock(); neighbour.unlock();
                        }
                    }

                    localFlipCount += currFlipCount;
                }  while (currFlipCount > 0);

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
                edgeCount.put(e, 0);
            }
        }
        Edge sharedEdge = null;
        for (Edge e : edgeCount.keySet()) {
            if (edgeCount.get(e) == 2) {
                sharedEdge = e;
                break;
            }
        }
        return sharedEdge;
    }

    private boolean needsFlip(Triangle t1, Triangle t2, Edge sharedEdge) {
        return false; // FIXME
    }

    private void flip(Triangle t1, Triangle t2, Edge sharedEdge) {
        // FIXME
    }
}
