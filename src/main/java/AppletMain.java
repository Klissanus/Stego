import colorspace.Component;
import colorspace.JavaByte;
import colorspace.Rgb;
import metrics.Metrics;
import stegoalgorithms.CoeffModify.TwoCoeffModify;
import stegoalgorithms.CoeffModify.twocoeffstrategy.TwoCoeffStrategyChangeOne;
import stegoalgorithms.StegoAlgorithmImpl;
import stegoalgorithms.blockcheck.NoneCheck;
import utils.PixelsMatrix;
import utils.Utils;

import javax.imageio.ImageIO;
import java.applet.Applet;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Klissan on 22.11.2016.
 * todo hamming code
 * todo rid-solomon code
 * todo BMYY
 * todo YCbCr
 */
public class AppletMain extends java.applet.Applet {

  BufferedImage img;
  BufferedImage newImg;
  byte[] stegoKey = new byte[]{(byte) 0xFF}; //, (byte) 0x00, (byte) 0x00
  String filePath = "C:\\Users\\Klissan\\IdeaProjects\\Stego\\src\\main\\resources\\";
  String message = "V love Gr1Basiabcdefghijklmnopqrstuvwxyz";

   @Override
  public void init() {
    super.init();
    System.out.println(System.getProperty("user.dir"));

    runStego();

     //testMse();

  }

  private void testMse(){
    try {
      img = ImageIO.read(new File(filePath + "images/Lenna.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    JavaByte[] pixels = Utils.readPixels(img);
    Rgb[] rgb = Utils.javaByteToRgb(pixels);
    double mse = Metrics.mse(rgb, rgb);
    System.out.println(mse);
  }



  private void runStego(){


    try {
      Set<Component> colorsSet = new HashSet<>();
      colorsSet.add(Component.Y);
      //colorsSet.add(Component.GREEN);
      //colorsSet.add(Component.RED);

      //todo разобратьтся с альфа каналом png
      img = ImageIO.read(new File(filePath + "images/Lenna.png"));
      JavaByte[] pixels = Utils.readPixels(img);
      Rgb[] rgbBefore = Utils.javaByteToRgb(pixels);

      PixelsMatrix mtr = new PixelsMatrix(Utils.javaByteToYCbCr(pixels), img.getWidth(), img.getHeight());
      //System.out.println("Available bytes capacity = " + mtr.getPrimaryMatrixCount() / 8);

      //StegoAlgorithmImpl stegoAlg = new KochZhao(400, 4, 5, 5, 4, colorsSet);
      StegoAlgorithmImpl stegoAlg = new StegoAlgorithmImpl(new NoneCheck(), //CheckLfHf(1000, -100)
          new TwoCoeffModify(new TwoCoeffStrategyChangeOne(), 30, 1, 1, 5, 1));
      byte[] bytesBegin = stegoAlg.read(mtr, colorsSet, stegoKey);
      String strBegin = new String(bytesBegin);

      stegoAlg.hide(mtr, colorsSet, message.getBytes(), stegoKey);

      String format = "jpg";
      JavaByte[] f = Utils.convertToJavaByte(mtr.toArray());
      Rgb[] rgbAfter = Utils.javaByteToRgb(f);
      //System.out.println("MSE " + Metrics.mse(rgbBefore, rgbAfter));
      System.out.println("PSNR " + Metrics.psnr(rgbBefore, rgbAfter));

      newImg = Utils.createNewImage(img, f);
      File outputfile = new File(filePath + "saved." + format);
      Utils.compressImage(outputfile, newImg, 80);
      //ImageIO.write(newImg, format, outputfile);

      System.out.println("\nTry to read message from empty container : \n" + strBegin.substring(0, message.length()));
      String str1 = new String(stegoAlg.read(mtr, colorsSet, stegoKey));
      System.out.println("\nTry to read message from in memory img : \n" + str1.substring(0, message.length() +4));

      BufferedImage img2 = ImageIO.read(new File(filePath + "saved." + format));
      JavaByte[] pixels2 = Utils.readPixels(img2);
      PixelsMatrix mtr2 = new PixelsMatrix(Utils.javaByteToYCbCr(pixels2), img.getWidth(), img.getHeight());
      byte[] bytes2 = stegoAlg.read(mtr2, colorsSet, stegoKey);
      String str2 = new String(bytes2, "UTF-8");
      String readMessage = str2.substring(0, message.length());
      System.out.println("NC " + Metrics.nc(message.getBytes(), bytes2));
      System.out.println("\nTry to read message from new img : \n" + readMessage);

    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(e);
    }
  }

  private void smth(Set<Component> colorsSet ){
    int size = 8;
    int[][] data = new int[size][size];
    for (int k = 0; k < size; k++) {
      for (int l = 0; l < size; l++) {
        data[k][l] = k*size + l;
      }
    }
    int[] res = Utils.zigZag(data);
/*
    Rgb[] rgbs = Utils.javaByteToRgb(pixels);
    JavaByte[] jbs = Utils.convertToJavaByte(rgbs);

      for (int i = 0; i < jbs.length; i++) {
        for(Component c : colorsSet) {
          if (Math.round(pixels[i].getComponent(c)) != Math.round(jbs[i].getComponent(c))){
            System.out.println("Wrong i = " + i);
          }
        }
      }*/
  }

  @Override
  public void start() {
    super.start();
  }

  @Override
  public void paint(Graphics g) {
    g.drawImage(newImg, 0, 0, null);
  }
}
