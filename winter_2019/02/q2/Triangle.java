public class Triangle {
    volatile public Edge e1, e2, e3;        // edges of the triangle
    volatile public Triangle nt1, nt2, nt3; // nt - Neighbouring Triangles

    public Triangle(Edge e1, Edge e2, Edge e3) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }
}
