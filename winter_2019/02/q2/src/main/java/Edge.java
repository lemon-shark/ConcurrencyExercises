import java.util.concurrent.atomic.AtomicLong;

public class Edge {
    private static final AtomicLong NEXT_ID = new AtomicLong(0);

    public final Point p1, p2;
    public final long id;

    public Edge(Point p1, Point p2) {
        this.id = NEXT_ID.getAndIncrement();
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Edge)) {
            return false;
        }

        Edge other = (Edge)o;
        return this.id == other.id;
    }

    public String toString() {
        return "Edge_"+this.id+"{"+
            "\n\t\t"+p1.toString()+","+
            "\n\t\t"+p2.toString()+","+
        "}";
    }

    /**
     * Returns true if Edge other intersects with Edge this.
     *
     * Note that this computes a 'strict' intersection. That is, edges which share
     * a point will not be considered as intersecting. Parallel and overlapping segments
     * are also not considered to be intersecting.
     */
    public boolean intersects(Edge other) {
        if (this.p1.equals(other.p1) || this.p1.equals(other.p2) ||
            this.p2.equals(other.p1) || this.p2.equals(other.p2)) {
                return false;
        }

        // get line equation for this edge
        double x1_1 = this.p1.getX();
        double x2_1 = this.p2.getX();
        double y1_1 = this.p1.getY();
        double y2_1 = this.p2.getY();

        double a1 = (y2_1 - y1_1) / (x2_1 - x1_1);
        double b1 = y1_1 - a1 * x1_1;

        // get line equation for other edge
        double x1_2 = other.p1.getX();
        double x2_2 = other.p2.getX();
        double y1_2 = other.p1.getY();
        double y2_2 = other.p2.getY();

        double a2 = (y2_2 - y1_2) / (x2_2 - x1_2);
        double b2 = y1_2 - a2 * x1_2;

        // get intersection point
        double x_intersect = (b2 - b1) / (a1 - a2);
        double y_intersect = a1 * x_intersect + b1;

        // return true if intersection is in bounds of both edges/line segments
        boolean in_bounds_1 =
            x_intersect > Math.min(x1_1,x2_1) &&
            x_intersect < Math.max(x1_1,x2_1) &&
            y_intersect > Math.min(y1_1,y2_1) &&
            y_intersect < Math.max(y1_1,y2_1);
        boolean in_bounds_2 =
            x_intersect > Math.min(x1_2,x2_2) &&
            x_intersect < Math.max(x1_2,x2_2) &&
            y_intersect > Math.min(y1_2,y2_2) &&
            y_intersect < Math.max(y1_2,y2_2);

        return in_bounds_1 && in_bounds_2;
    }

    public double innerAngleWith(Edge other) {
        Point sharedPoint = getSharedPoint(other);

        Point pThis = this.p1 != sharedPoint? this.p1: this.p2;
        Point pOther = other.p1 != sharedPoint? other.p1: other.p2;

        double x1, y1, x2, y2;
        x1 = pThis.getX() - sharedPoint.getX();
        y1 = pThis.getY() - sharedPoint.getY();
        x2 = pOther.getX() - sharedPoint.getX();
        y2 = pOther.getY() - sharedPoint.getY();

        double dotProd = x1*x2 + y1*y2;
        double mag1 = Math.sqrt(x1*x1 + y1*y1);
        double mag2 = Math.sqrt(x2*x2 + y2*y2);
        double cos = dotProd / (mag1 * mag2);

        return Math.acos(cos);
    }

    public boolean containsPoint(Point p) {
        return this.p1 == p || this.p2 == p;
    }

    private Point getSharedPoint(Edge other) {
        if (this.p1 == other.p1 || this.p1 == other.p2) {
            return this.p1;
        } else if (this.p2 == other.p1 || this.p2 == other.p2) {
            return this.p2;
        } else {
            return null;
        }
    }
}
