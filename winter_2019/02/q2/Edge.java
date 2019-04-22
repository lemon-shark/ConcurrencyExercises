public class Edge {
    public final Point p1, p2;

    public Edge(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Edge)) {
            return false;
        }

        Edge other = (Edge)o;

        boolean same1 = this.p1 == other.p1 && this.p2 == other.p2;
        boolean same2 = this.p1 == other.p2 && this.p2 == other.p1;

        return same1 || same2;
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
        System.out.println("intersect("+x_intersect+","+y_intersect+")");

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
}
