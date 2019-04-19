import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class q1 {

    // Number of threads to use
    public static int threads = 1;

    public static void main(String[] args) {
        try {
            if (args.length>0) {
                threads = Integer.parseInt(args[1]);
            }

            // read in an image from a file
            BufferedImage img = ImageIO.read(new File("image.jpg"));
            // store the dimensions locally for convenience
            int width  = img.getWidth();
            int height = img.getHeight();

            // create an output image
            BufferedImage outputimage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);

            // ------------------------------------
            // Your code would go here
            
            // The easiest mechanisms for getting and setting pixels are the
            // BufferedImage.setRGB(x,y,value) and getRGB(x,y) functions.
            // Note that setRGB is synchronized (on the BufferedImage object).
            // Consult the javadocs for other methods.

            // The getRGB/setRGB functions return/expect the pixel value in ARGB format, one byte per channel.  For example,
            //  int p = img.getRGB(x,y);
            // With the 32-bit pixel value you can extract individual colour channels by shifting and masking:
            //  int red = ((p>>16)&0xff);
            //  int green = ((p>>8)&0xff);
            //  int blue = (p&0xff);
            // If you want the alpha channel value it's stored in the uppermost 8 bits of the 32-bit pixel value
            //  int alpha = ((p>>24)&0xff);
            // Note that an alpha of 0 is transparent, and an alpha of 0xff is fully opaque.
            
            // ------------------------------------
            
            // Write out the image
            File outputfile = new File("outputimage.png");
            ImageIO.write(outputimage, "png", outputfile);

        } catch (Exception e) {
            System.out.println("ERROR " +e);
            e.printStackTrace();
        }
    }
}
