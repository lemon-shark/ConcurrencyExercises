import java.util.concurrent.locks.Lock; // Lock interface
import java.util.concurrent.locks.ReentrantLock; // Lock implementation

import java.util.concurrent.ThreadLocalRandom; // for random doubles
import static java.lang.Math.sqrt;

/**
 * 2D cartesian coordinate and linked list node
 * - uses double to store x and y
 * - locks on prev, self, and next before doing updateCoord such that
 *   deadlock is prevented and program state is uncorrupted
 */
public class SynchVertex {
    public double x;
    public double y;
    public SynchVertex prev;
    public SynchVertex next;
    public ReentrantLock lock = new ReentrantLock();

    public SynchVertex[] triangleVerticesInLockAcquisitionOrder = new SynchVertex[3];

    public SynchVertex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setTriangleVerticesInLockAcquisitionOrder(SynchVertex first, SynchVertex second, SynchVertex third) {
        triangleVerticesInLockAcquisitionOrder[0] = first;
        triangleVerticesInLockAcquisitionOrder[1] = second;
        triangleVerticesInLockAcquisitionOrder[2] = third;
    }

    public void lock()   { lock.lock(); }
    public void unlock() { lock.unlock(); }

    public void updateCoord() {
        for (SynchVertex sp : triangleVerticesInLockAcquisitionOrder)
            sp.lock();

        double r1 = ThreadLocalRandom.current().nextDouble();
        double r2 = ThreadLocalRandom.current().nextDouble();
        this.x = ((1 - sqrt(r1)) * prev.x) + ((sqrt(r1) * (1 - r2)) * this.x) + ((sqrt(r1) * r2) * next.x);
        this.y = ((1 - sqrt(r1)) * prev.y) + ((sqrt(r1) * (1 - r2)) * this.y) + ((sqrt(r1) * r2) * next.y);

        for (SynchVertex sp : triangleVerticesInLockAcquisitionOrder)
            sp.unlock();
    }
}

// simple data-passing class
class Vertex {
    public int x;
    public int y;
    public Vertex(int x, int y) {
        this.x = x; this.y = y;
    }
}
