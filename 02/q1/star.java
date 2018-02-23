import java.awt.image.*; // for writing image to disk
import java.io.*;
import javax.imageio.*;

import java.awt.Graphics; // for drawing shapes on images
import java.awt.Color;
import java.awt.Polygon;

import java.util.concurrent.ThreadLocalRandom; // for random doubles
import java.util.Arrays; // general utilities

public class star {
    // image dimensions
    public static final int imgWidth = 1920;
    public static final int imgHeight = 1080;

    // command-line args
    public static int m; // how many threads
    public static int c; // how many times each thread must edit a vertex

    // points of the polygon to be drawn after multithreaded manipulation
    public static SynchVertex[] vertices = new SynchVertex[] {
        new SynchVertex(-1.0,  5.0),
        new SynchVertex( 1.0,  2.0),
        new SynchVertex( 5.0,  0  ),
        new SynchVertex( 1.0, -2.0),
        new SynchVertex(-4.0, -4.0),
        new SynchVertex(-3.0, -1.0)
    };


    /**
     * main method
     */
    public static void main(String[] args) throws Exception {
        // command-line arg parsing
        if (args.length != 2)
            throw new Exception("expected 2 arguments, got "+args.length);
        m = Integer.parseInt(args[0]);
        if (m > vertices.length)
            throw new Exception("polygon has 6 vertices, so max 6 threads allowed");
        c = Integer.parseInt(args[1]);
        if (m < 1 || c < 0)
            throw new Exception("both arguments must be positive");

        // set up linked list
        for (int i = 0; i < vertices.length; i++) {
            // prev and next SynchVertex in linked-list using modular arithmetic
            int prev = (i + vertices.length - 1) % vertices.length;
            int next = (i + 1) % vertices.length;

            SynchVertex v = vertices[i];
            v.prev = vertices[prev];
            v.next = vertices[next];

            Integer[] ordering = new Integer[] {prev, i, next};
            // becaues every triplet of SynchVertexes that are adjacent in linked list must be locked
            // at once, create a global ordering that is internally consistent for each such triplet
            Arrays.sort(ordering, (a, b) -> (a % 3) - (b % 3));

            v.setTriangleVerticesInLockAcquisitionOrder(
                vertices[ordering[0]],
                vertices[ordering[1]],
                vertices[ordering[2]]
            );
        }

        // create m threads which, c times each, pick a random vertex and update it
        Thread[] ts = new Thread[m];
        for (int i = 0; i < m; i++)
            ts[i] = new Thread(() -> {
                for (int j = 0; j < c; j++) {
                    vertices[ (int)randomDouble(0, vertices.length) ].updateCoord();
                }
            });

        // run the m threads and wait for them to finish
        for (Thread t : ts)
            t.start();
        for (Thread t : ts)
            t.join();

        // create an image and initialize it to all 0's
        BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
        for (int i=0;i<imgWidth;i++)
            for (int j=0;j<imgHeight;j++)
                img.setRGB(i,j,0);

        // extract points to xCoords_d and yCoords_d. rescale to fit img
        double[] xCoords_d = new double[vertices.length];
        double[] yCoords_d = new double[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            xCoords_d[i] = vertices[i].x;
            yCoords_d[i] = vertices[i].y;
        }
        System.out.println("final points: ");
        for (int i = 0; i < vertices.length; i++)
            System.out.println("(" + xCoords_d[i] + "," + yCoords_d[i] + ")");
        Vertex newOrigin = rescaleCoords(xCoords_d, yCoords_d);

        // draw polygon on image
        int[] xCoords = new int[vertices.length];
        int[] yCoords = new int[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            xCoords[i] = (int) xCoords_d[i];
            yCoords[i] = (int) yCoords_d[i];
        }
        Graphics imgGraphics = img.getGraphics();
        imgGraphics.setColor(Color.BLACK);
        imgGraphics.fillPolygon(xCoords, yCoords, vertices.length);
        imgGraphics.setColor(Color.RED);
        for (int i = 0; i < vertices.length; i++)
            imgGraphics.drawLine(newOrigin.x, newOrigin.y, xCoords[i], yCoords[i]);
        imgGraphics.setColor(Color.BLUE);
        int diameter = 20;
        for (int i = 0; i < vertices.length; i++)
            imgGraphics.fillOval(xCoords[i] - diameter/2, yCoords[i] - diameter/2, diameter, diameter);

        // write image to file
        File outputfile = new File("output.png");
        ImageIO.write(img, "png", outputfile);
    }


    /**
     * convenience functions
     */

    // rescale and translate coords to fit in 1920x1080.  return new origin
    public static Vertex rescaleCoords(double[] xs, double[] ys) {
        assert(ys.length == vertices.length && xs.length == vertices.length);

        /**
         * scale coordinates to fit in imgWidth,imgHeight
         */
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        for (int i = 0; i < vertices.length; i++) {
            if (xs[i] > maxX)
                maxX = xs[i];
            if (xs[i] < minX)
                minX = xs[i];
            if (ys[i] > maxY)
                maxY = ys[i];
            if (ys[i] < minY)
                minY = ys[i];
        }
        double maxXdiff = maxX - minX;
        double maxYdiff = maxY - minY;

        double widthRatio = imgWidth / maxXdiff;
        double heightRatio = imgHeight / maxYdiff;

        double ratio = (widthRatio < heightRatio)? widthRatio : heightRatio;

        for (int i = 0; i < vertices.length; i++) {
            double scaledX = xs[i] * ratio;
            double scaledY = ys[i] * ratio;
            xs[i] = scaledX;
            ys[i] = scaledY;
        }

        /**
         * translate coordinates to be in first quadrant
         */
        minX = Double.MAX_VALUE;
        minY = Double.MAX_VALUE;
        for (int i = 0; i < vertices.length; i++) {
            if (xs[i] < minX)
                minX = xs[i];
            if (ys[i] < minY)
                minY = ys[i];
        }
        for (int i = 0; i < vertices.length; i++)
            xs[i] -= minX;
        for (int i = 0; i < vertices.length; i++)
            ys[i] -= minY;

        /**
         * return the point to which the origin is rescaled
         */
        int newOriginX = (int)-minX;
        int newOriginY = (int)-minY;
        return new Vertex(newOriginX, newOriginY);
    }

    // save me typing later
    public static double randomDouble(int lowerBoundInclusive, int upperBoundExclusive)
    { return ThreadLocalRandom.current().nextDouble(lowerBoundInclusive, upperBoundExclusive); }
}
