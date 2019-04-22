import java.util.concurrent.ThreadLocalRandom;

public class Point {
    private final double x, y;

    public Point(double x, double y) {
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

    public static Point createRandomPoint() {
        double x = ThreadLocalRandom.current().nextDouble();
        double y = ThreadLocalRandom.current().nextDouble();

        return new Point(x, y);
    }
}
