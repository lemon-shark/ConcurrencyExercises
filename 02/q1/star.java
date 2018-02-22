import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Polygon;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.Arrays;
import static java.lang.Math.sqrt;

public class star {
    // the only constants that were useful to be static globals
    public static final int imgWidth = 1920;
    public static final int imgHeight = 1080;

    // command-line args
    public static int m; // how many threads
    public static int c; // how many times each thread must edit a vertex

    public static SynchPoint[] vertices = new SynchPoint[6];

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
        vertices = new SynchPoint[] {
            new SynchPoint(-1.0,  5.0, 0),
            new SynchPoint( 1.0,  2.0, 1),
            new SynchPoint( 5.0,  0  , 2),
            new SynchPoint( 1.0, -2.0, 3),
            new SynchPoint(-4.0, -4.0, 4),
            new SynchPoint(-3.0, -1.0, 5)
        };
        for (int i = 0; i < vertices.length; i++) {
            int prev = (i + vertices.length - 1) % vertices.length;
            int next = (i + 1) % vertices.length;

            vertices[i].next = vertices[(i + 1) % vertices.length];
            vertices[i].prev = vertices[(i + vertices.length - 1) % vertices.length];

            // global ordering to prevent deadlock
            int[] ordering = new int[] {prev, i, next};
            Arrays.sort(ordering);
            vertices[i].setOrdering(vertices[ordering[0]], vertices[ordering[1]], vertices[ordering[2]]);
        }

        // create m threads which, c times each, pick a random vertex and update it
        Thread[] ts = new Thread[m];
        for (int i = 0; i < m; i++) {
            ts[i] = new Thread() {
                @Override
                public void run() {
                    for (int j = 0; j < c; j++)
                        vertices[j].updateCoord();
                }
            };
        }

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
        for (int i = 0; i < vertices.length; i++) {
            System.out.println("(" + xCoords_d[i] + "," + yCoords_d[i] + ")");
        }
        Point newOrigin = rescaleCoords(xCoords_d, yCoords_d);

        // draw polygon on image
        int[] xCoords = new int[vertices.length];
        int[] yCoords = new int[vertices.length];
        for (int i = 0; i < vertices.length; i++)
            xCoords[i] = (int) xCoords_d[i];
        for (int i = 0; i < vertices.length; i++)
            yCoords[i] = (int) yCoords_d[i];
        Graphics imgGraphics = img.getGraphics();
        imgGraphics.setColor(Color.BLACK);
        imgGraphics.fillPolygon(xCoords, yCoords, vertices.length);
        imgGraphics.setColor(Color.RED);
        for (int i = 0; i < vertices.length; i++)
            imgGraphics.drawLine(newOrigin.x, newOrigin.y, xCoords[i], yCoords[i]);
        imgGraphics.setColor(Color.BLUE);
        for (int i = 0; i < vertices.length; i++)
            imgGraphics.fillOval(xCoords[i] - 10, yCoords[i] - 10, 20, 20);

        // write image to file
        File outputfile = new File("output.png");
        ImageIO.write(img, "png", outputfile);
    }


    /**
     * convenience functions
     */

    // rescale and translate coords to fit in 1920x1080.  return new origin
    public static Point rescaleCoords(double[] xs, double[] ys) {
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
        return new Point(newOriginX, newOriginY);
    }
}

class SynchPoint {
    public volatile double x;
    public volatile double y;
    public SynchPoint prev;
    public SynchPoint next;
    public ReentrantLock lock;

    public SynchPoint[] ordering = new SynchPoint[3];

    public SynchPoint(double x, double y, int id) {
        this.x = x;
        this.y = y;
        lock = new ReentrantLock();
    }

    public void setOrdering(SynchPoint first, SynchPoint second, SynchPoint third) {
        ordering[0] = first;
        ordering[1] = second;
        ordering[2] = third;
    }

    public void lock()
    { this.lock.lock(); }
    public void unlock()
    { this.lock.unlock(); }

    public void updateCoord() {
        for (SynchPoint sp : ordering)
            sp.lock();

        double r1 = ThreadLocalRandom.current().nextDouble();
        double r2 = ThreadLocalRandom.current().nextDouble();
        this.x = ((1 - sqrt(r1)) * prev.x) + ((sqrt(r1) * (1 - r2)) * this.x) + ((sqrt(r1) * r2) * next.x);
        this.y = ((1 - sqrt(r1)) * prev.y) + ((sqrt(r1) * (1 - r2)) * this.y) + ((sqrt(r1) * r2) * next.y);

        for (SynchPoint sp : ordering)
            sp.unlock();
    }
}

class Point {
    public int x;
    public int y;
    public Point(int x, int y) {
        this.x = x; this.y = y;
    }
}
