import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Point {
    private static final AtomicLong NEXT_ID = new AtomicLong(0);

    private final double x, y;
    public final long id;

    public Point(double x, double y) {
        this.id = NEXT_ID.getAndIncrement();
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public boolean equals(Point other) {
        return this.x == other.getX() && this.y == other.getY();
    }

    public double distanceTo(Point other) {
        double xDist = other.getX() - this.x;
        double yDist = other.getY() - this.y;
        return Math.sqrt( xDist*xDist + yDist*yDist );
    }

    @Override
    public String toString() {
        return "Point_"+this.id+"{"+
            this.x+","+
            this.y+","+
        "}";
    }
}
