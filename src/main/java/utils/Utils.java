package utils;

import colorspace.RgbPixels;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by Klissan on 02.12.2016.
 */
public class Utils {

  public static RgbPixels readPixels(BufferedImage img) {
    int w = img.getWidth();
    int h = img.getHeight();
    int l = 3 * w * h;
    return new RgbPixels(img.getRaster().getPixels(0, 0, w, h, new int[l]));
  }

  public static BufferedImage createNewImage(BufferedImage source, RgbPixels pixels) {
    BufferedImage img = new BufferedImage(
        source.getWidth(),
        source.getHeight(),
        source.getType()
    );

    Raster r = source.getRaster();

    img.setData(
        Raster.createRaster(
            r.getSampleModel(),
            new DataBufferByte(Utils.rgbToBgr(pixels), pixels.getPixels().length),
            new Point(0, 0)
        )
    );
    return img;
  }

  public static void compressImage(File file, BufferedImage image, int qualityPercent) {

    if ((qualityPercent < 0) || (qualityPercent > 100)) {
      throw new IllegalArgumentException("Quality out of bounds!");
    }
    float quality = qualityPercent / 100f;
    ImageWriter writer = null;
    Iterator iter = ImageIO.getImageWritersByFormatName("jpg");
    if (iter.hasNext()) {
      writer = (ImageWriter) iter.next();
    }
    try {
      ImageOutputStream ios = ImageIO.createImageOutputStream(new FileOutputStream(file));
      writer.setOutput(ios);
      ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
      iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
      iwparam.setCompressionQuality(quality);
      writer.write(null, new IIOImage(image, null, null), iwparam);
      ios.flush();
      writer.dispose();
      ios.close();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  private static byte[] rgbToBgr(RgbPixels pixels) {
    byte[] result = new byte[pixels.getPixels().length * 3];
    for (int i = 0; i < pixels.getPixels().length; ++i) {
      result[3 * i + 0] = (byte) (0xFF & pixels.getPixels()[i].getColor(Color.BLUE));
      result[3 * i + 1] = (byte) (0xFF & pixels.getPixels()[i].getColor(Color.GREEN));
      result[3 * i + 2] = (byte) (0xFF & pixels.getPixels()[i].getColor(Color.RED));
    }
    return result;
  }
}
