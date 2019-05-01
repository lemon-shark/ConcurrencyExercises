import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.atomic.AtomicLong;

public class Triangle {
    public static final AtomicLong NEXT_ID = new AtomicLong(0);

    volatile public Edge e1, e2, e3;        // e -  Edges of the triangle
    volatile public Triangle nt1, nt2, nt3; // nt - Neighbouring Triangles
    public final long id;

    private Lock lock;

    public Triangle(Edge e1, Edge e2, Edge e3) {
        this.id = NEXT_ID.getAndIncrement();
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;

        this.lock = new ReentrantLock(true);
    }

    public void lock() {
        this.lock.lock();
    }
    public void unlock() {
        this.lock.unlock();
    }

    public String toString() {
        String s1 = this.e1 != null? this.e1.toString(): "null";
        String s2 = this.e2 != null? this.e2.toString(): "null";
        String s3 = this.e3 != null? this.e3.toString(): "null";
        String s4 = this.nt1 != null? this.nt1.toName(): "null";
        String s5 = this.nt2 != null? this.nt2.toName(): "null";
        String s6 = this.nt3 != null? this.nt3.toName(): "null";
        return "Triangle_"+this.id+"{"+
            "\n\t"+s1+","+
            "\n\tneighbour: "+s4+","+
            "\n\t"+s2+","+
            "\n\tneighbour: "+s5+","+
            "\n\t"+s3+","+
            "\n\tneighbour: "+s6+","+
        "}";
    }

    public String toName() {
        return "Triangle_"+this.id;
    }
}
