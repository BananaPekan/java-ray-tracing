package banana.pekan.raytracing.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

public class Image extends BufferedImage {
    public Image(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    public Image(int width, int height, int imageType, IndexColorModel cm) {
        super(width, height, imageType, cm);
    }

    public Image(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied, Hashtable<?, ?> properties) {
        super(cm, raster, isRasterPremultiplied, properties);
    }

    public void test() {
        int width = getWidth();
        int height = getHeight();
        Image newImage = new Image(width, height, getType());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int r = 0;
                int g = 0;
                int b = 0;
                int pixels = 0;
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        int pX = x + i;
                        if (pX >= width || pX < 0) continue;
                        int pY = y + j;
                        if (pY >= height || pY < 0) continue;

                        Color color = new Color(getRGB(pX, pY));
                        r += color.getRed();
                        g += color.getGreen();
                        b += color.getBlue();
                        pixels++;
                    }
                }
                r /= pixels;
                g /= pixels;
                b /= pixels;
                newImage.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }
        this.setData(newImage.getData());
    }

}
