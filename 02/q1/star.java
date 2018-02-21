import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Polygon;
import java.lang.System;

import java.util.concurrent.ThreadLocalRandom;

public class star {
    // the only constants that were useful to be static globals
    public static final int imgWidth = 1920;
    public static final int imgHeight = 1080;

    // command-line args
    public static int m; // how many threads
    public static int c; // how many times each thread must edit a vertex

    public static SynchPoint[] vertices = new SynchPoint[] {
        new SynchPoint(-1, 5),
        new SynchPoint(1, 2),
        new SynchPoint(5, 0),
        new SynchPoint(1, -2),
        new SynchPoint(-4, -4),
        new SynchPoint(-3, -1)
    };

    public static void main(String[] args) throws Exception {
        // command-line arg parsing
        if (args.length != 2)
            throw new Exception("expected 2 arguments, got "+args.length);
        m = Integer.parseInt(args[0]);
        if (m > vertices.length)
            throw new Exception("polygon has 6 vertices, so max 6 threads allowed");
        c = Integer.parseInt(args[1]);
        if (m < 1 || c < 1)
            throw new Exception("both arguments must be positive");

        // TODO: the whole program
        // -----------------------

        // create an image and initialize it to all 0's
        BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
        for (int i=0;i<imgWidth;i++)
            for (int j=0;j<imgHeight;j++)
                img.setRGB(i,j,0);

        // extract points to xCoords and yCoords. rescale to fit img
        int[] xCoords = new int[vertices.length];
        int[] yCoords = new int[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            xCoords[i] = vertices[i].x;
            yCoords[i] = vertices[i].y;
        }
        int[] newOrigin = rescaleCoords(xCoords, yCoords);

        // draw polygon on image
        Graphics imgGraphics = img.getGraphics();
        imgGraphics.setColor(Color.BLACK);
        imgGraphics.fillPolygon(xCoords, yCoords, vertices.length);
        imgGraphics.setColor(Color.RED);
        for (int i = 0; i < vertices.length; i++)
            imgGraphics.drawLine(newOrigin[0], newOrigin[1], xCoords[i], yCoords[i]);
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

    public static int[] rescaleCoords(int[] xCoords, int[] yCoords) {
        assert(yCoords.length == vertices.length && xCoords.length == vertices.length);

        // scale coordinates to fit in imgWidth,imgHeight
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        for (int i = 0; i < vertices.length; i++) {
            if (xCoords[i] > maxX)
                maxX = xCoords[i];
            if (xCoords[i] < minX)
                minX = xCoords[i];
            if (yCoords[i] > maxY)
                maxY = yCoords[i];
            if (yCoords[i] < minY)
                minY = yCoords[i];
        }
        int maxXdiff = maxX - minX;
        int maxYdiff = maxY - minY;

        double widthRatio = ((double) imgWidth) / maxXdiff;
        double heightRatio = ((double) imgHeight) / maxYdiff;

        double ratio = (widthRatio < heightRatio)? widthRatio : heightRatio;

        for (int i = 0; i < vertices.length; i++) {
            int scaledX = (int)(xCoords[i] * ratio);
            int scaledY = (int)(yCoords[i] * ratio);
            xCoords[i] = scaledX;
            yCoords[i] = scaledY;
        }

        // translate coordinates to be in first quadrant
        minX = Integer.MAX_VALUE;
        minY = Integer.MAX_VALUE;
        for (int i = 0; i < vertices.length; i++) {
            if (xCoords[i] < minX)
                minX = xCoords[i];
            if (yCoords[i] < minY)
                minY = yCoords[i];
        }
        for (int i = 0; i < vertices.length; i++)
            xCoords[i] -= minX;
        for (int i = 0; i < vertices.length; i++)
            yCoords[i] -= minY;

        int newOriginX = -minX;
        int newOriginY = -minY;

        return new int[] {newOriginX, newOriginY};
    }


    public static double randomDouble() // save me typing later
    { return ThreadLocalRandom.current().nextDouble(); }
}

class SynchPoint {
    public int x;
    public int y;
    public SynchPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
