package utils;

import colorspace.*;
import colorspace.Component;
import colorspace.bytemapping.Map256;

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
  static Map256 map256 = new Map256();

  public static int[] zigZag(int[][] data){
    int size1 = data.length;
    int size2 = data[0].length;
    int[] result = new int[size1 * size2];
    int i = 1;
    int j = 1;
    for (int k = 0; k < size1 * size2; k++)
    {
      result[k] = data[i - 1][j - 1];
      if ((i + j) % 2 == 0) { // Even stripes
        if (j < size2) j++; else i+= 2;
        if (i > 1) i--;
      }
      else {// Odd stripes
        if (i < size1) i++; else j+= 2;
        if (j > 1) j--;
      }
    }
    return result;
  }

  public static int[] zigZagToIndexes(int zigZagIndex){
    int i = 1;
    int j = 1;
    int SIZE = 8;
    for (int k = 0; k < zigZagIndex; k++)
    {
      if ((i + j) % 2 == 0) { // Even stripes
        if (j < SIZE) j++; else i+= 2;
        if (i > 1) i--;
      }
      else {// Odd stripes
        if (i < SIZE) i++; else j+= 2;
        if (j > 1) j--;
      }
    }
    int[] res = {i-1, j-1};
    return res;
  }

  public static double[] zigZag(double[][] data){
    int size1 = data.length;
    int size2 = data[0].length;
    double[] result = new double[size1 * size2];
    int i = 1;
    int j = 1;
    for (int k = 0; k < size1 * size2; k++)
    {
      result[k] = data[i - 1][j - 1];
      if ((i + j) % 2 == 0) { // Even stripes
        if (j < size2) j++; else i+= 2;
        if (i > 1) i--;
      }
      else {// Odd stripes
        if (i < size1) i++; else j+= 2;
        if (j > 1) j--;
      }
    }
    return result;
  }


  public static JavaByte[] readPixels(BufferedImage img) {
    int w = img.getWidth();
    int h = img.getHeight();
    int l = 3 * w * h;
    int[] source = img.getRaster().getPixels(0, 0, w, h, new int[l]);
    JavaByte[] pixels = new JavaByte[source.length / 3];
    for (int i = 0; i < source.length / 3; ++i) {
      pixels[i] = new JavaByte(
          (byte) (0xFF & source[3 * i + 0]),
          (byte) (0xFF & source[3 * i + 1]),
          (byte) (0xFF & source[3 * i + 2]));
    }
    return pixels;
  }

  public static YCbCr RgbToYCbCr(Rgb pixel){
    return new YCbCr(pixel);
  }


  public static YCbCr[] RgbToYCbCr(Rgb[] pixels){
    YCbCr[] result = new YCbCr[pixels.length];
    for (int i = 0; i < pixels.length; i++) {
      result[i] = RgbToYCbCr(pixels[i]);
    }
    return result;
  }

  public static YCbCr javaByteToYCbCr(JavaByte pixel){
    return new YCbCr(javaByteToRgb(pixel));
  }

  public static YCbCr[] javaByteToYCbCr(JavaByte[] pixels){
    YCbCr[] result = new YCbCr[pixels.length];
    for (int i = 0; i < pixels.length; i++) {
      result[i] = javaByteToYCbCr(pixels[i]);
    }
    return result;
  }

  public static Rgb YCbCrToRgb(YCbCr pixel){
    double y = pixel.getComponent(Component.Y);
    double cb = pixel.getComponent(Component.CB);
    double cr = pixel.getComponent(Component.CR);
    int r = (int) Math.round(y                         + 1.402000 * (cr - 128));
    int g = (int) Math.round(y - 0.344136 * (cb - 128) - 0.714136 * (cr - 128));
    int b = (int) Math.round(y + 1.772000 * (cb - 128)                        );
    return new Rgb(r, g, b);
  }

  public static Rgb[] YCbCrToRgb(YCbCr[] pixels) {
    Rgb[] result = new Rgb[pixels.length];
    for (int i = 0; i < pixels.length; i++) {
      result[i] = YCbCrToRgb(pixels[i]);
    }
    return result;
  }

  public static Rgb javaByteToRgb(JavaByte pixel){
    byte jr = (byte) Math.round(pixel.getComponent(Component.RED));
    byte jg = (byte) Math.round(pixel.getComponent(Component.GREEN));
    byte jb = (byte) Math.round(pixel.getComponent(Component.BLUE));
    int r = map256.forward(jr);
    int g = map256.forward(jg);
    int b = map256.forward(jb);
    return new Rgb(r, g, b);
  }

  public static Rgb[] javaByteToRgb(JavaByte[] pixels){
    Rgb[] result = new Rgb[pixels.length];
    for (int i = 0; i < pixels.length; i++) {
      result[i] = javaByteToRgb(pixels[i]);
    }
    return result;
  }

  public static JavaByte RgbToJavaByte(Rgb pixel){
    long r = Math.round(pixel.getComponent(Component.RED));
    long g = Math.round(pixel.getComponent(Component.GREEN));
    long b = Math.round(pixel.getComponent(Component.BLUE));
    byte jr = map256.inverse(r);
    byte jg = map256.inverse(g);
    byte jb = map256.inverse(b);
    return new JavaByte(jr, jg, jb);
  }

  public static JavaByte[] RgbToJavaByte(Rgb[] pixels){
    JavaByte[] result = new JavaByte[pixels.length];
    for (int i = 0; i < pixels.length; i++) {
      result[i] = RgbToJavaByte(pixels[i]);
    }
    return result;
  }

  public static JavaByte[] convertToJavaByte(ColorSpace[] pixels){
    Class aClass = pixels[0].getClass();
    if( pixels[0] instanceof JavaByte ){
      return (JavaByte[]) pixels;
    } else if (pixels[0] instanceof Rgb){
      return RgbToJavaByte((Rgb[]) pixels);
    } else if( pixels[0] instanceof YCbCr){
      return RgbToJavaByte(YCbCrToRgb((YCbCr[]) pixels));
    }else{
      throw new ClassCastException(pixels.getClass().toString());
    }
  }

  public static BufferedImage createNewImage(BufferedImage source, JavaByte[] pixels) {
    BufferedImage img = new BufferedImage(
        source.getWidth(),
        source.getHeight(),
        source.getType()
    );

    Raster r = source.getRaster();

    img.setData(
        Raster.createRaster(
            r.getSampleModel(),
            new DataBufferByte(Utils.rgbToBgr(pixels), pixels.length),
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

  private static byte[] rgbToBgr(JavaByte[] pixels) {
    byte[] result = new byte[pixels.length * 3];
    for (int i = 0; i < pixels.length; ++i) {
      result[3 * i + 0] = (byte) (0xFF & (int) pixels[i].getComponent(Component.BLUE));
      result[3 * i + 1] = (byte) (0xFF & (int) pixels[i].getComponent(Component.GREEN));
      result[3 * i + 2] = (byte) (0xFF & (int) pixels[i].getComponent(Component.RED));
    }
    return result;
  }


}
