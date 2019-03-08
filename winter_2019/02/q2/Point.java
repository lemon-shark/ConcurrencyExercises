import java.util.concurrent.ThreadLocalRandom;

public class Point {
    private float x, y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public static Point createRandomPoint() {
        float x = ThreadLocalRandom.current().nextFloat();
        float y = ThreadLocalRandom.current().nextFloat();

        System.out.println(x + ", " + y);

        return new Point(x, y);
    }
}
