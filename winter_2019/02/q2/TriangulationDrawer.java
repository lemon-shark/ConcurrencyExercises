import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Polygon;

import java.util.Collection;

import java.io.File;

public class TriangulationDrawer {
    public static final int width = 800;
    public static final int height = 800;
    public static final int vertexSize = 10;

    public static void make(Collection<Triangle> triangles, String filename) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // all white background
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                img.setRGB(i,j,0xFFFFFFFF);

        // get ready to draw
        Graphics g = img.getGraphics();
        g.setColor(Color.BLACK);

        // draw triangles
        for (Triangle t : triangles) {
            Edge[] edges = new Edge[] {t.e1, t.e2, t.e3};
            for (Edge e : edges) {
                int x1 = (int)Math.round(e.p1.getX() * width);
                int x2 = (int)Math.round(e.p2.getX() * width);
                int y1 = (int)Math.round(e.p1.getY() * height);
                int y2 = (int)Math.round(e.p2.getY() * height);

                g.drawLine(x1, y1, x2, y2);

                g.fillOval(x1 - (vertexSize/2), y1 - (vertexSize/2), vertexSize, vertexSize);
                g.fillOval(x2 - (vertexSize/2), y2 - (vertexSize/2), vertexSize, vertexSize);
            }
        }

        File outputFile = new File(filename);
        try {
            ImageIO.write(img, "png", outputFile);
        }
        catch (Exception e) {
            System.out.println("failed to write " + filename + " file");
        }
    }

    public static void make2(Collection<Edge> edges, String filename) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // all white background
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                img.setRGB(i,j,0xFFFFFFFF);

        // get ready to draw
        Graphics g = img.getGraphics();
        g.setColor(Color.BLACK);

        for (Edge e : edges) {
            int x1 = (int)Math.round(e.p1.getX() * width);
            int x2 = (int)Math.round(e.p2.getX() * width);
            int y1 = (int)Math.round(e.p1.getY() * height);
            int y2 = (int)Math.round(e.p2.getY() * height);

            g.drawLine(x1, y1, x2, y2);

            g.fillOval(x1 - (vertexSize/2), y1 - (vertexSize/2), vertexSize, vertexSize);
            g.fillOval(x2 - (vertexSize/2), y2 - (vertexSize/2), vertexSize, vertexSize);
        }

        File outputFile = new File(filename);
        try {
            ImageIO.write(img, "png", outputFile);
        }
        catch (Exception e) {
            System.out.println("failed to write " + filename + " file");
        }
    }
}
