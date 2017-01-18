import colorspace.ColorSpace;
import colorspace.Component;
import colorspace.JavaByte;
import colorspace.Rgb;
import stegoalgorithms.KochZhao;
import stegoalgorithms.StegoAlgorithm;
import utils.MatrixPixels;
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
public class Main extends Applet {

  BufferedImage img;
  BufferedImage newImg;
  byte[] stegoKey = new byte[]{(byte) 0xFF, (byte) 0x00, (byte) 0x00};
  String filePath = "C:\\Users\\Klissan\\IdeaProjects\\Stego\\src\\main\\resources\\";

  @Override
  public void init() {
    super.init();
    System.out.println(System.getProperty("user.dir"));

    String message = "V love Gr1Basi";

    try {
      Set<Component> colorsSet = new HashSet<>();
      colorsSet.add(Component.BLUE);
      //colorsSet.add(Component.GREEN);
      //colorsSet.add(Component.RED);

      //todo разобратьтся с альфа каналом png
      img = ImageIO.read(new File(filePath + "images/Lenna.png"));
      JavaByte[] pixels = Utils.readPixels(img);
      /*Rgb[] rgbs = Utils.JavaByteToRgb(pixels);
      JavaByte[] jbs = Utils.convertToJavaByte(rgbs);

      for (int i = 0; i < jbs.length; i++) {
        for(Component c : colorsSet) {
          if (Math.round(pixels[i].getComponent(c)) != Math.round(jbs[i].getComponent(c))){
            System.out.println("Wrong i = " + i);
          }
        }
      }*/

      MatrixPixels mtr = new MatrixPixels(Utils.javaByteToRgb(pixels), img.getWidth(), img.getHeight());
      System.out.println("Available bytes capacity = " + mtr.getPrimaryMatrixCount() / 8);

      StegoAlgorithm stegoAlg = new KochZhao(400, 4, 5, 5, 4, colorsSet);

      byte[] bytesBegin = stegoAlg.readMessage(mtr, stegoKey);
      String strBegin = new String(bytesBegin);

      //System.out.println("message byte size = " + message.getBytes().length);
      stegoAlg.hide(mtr, message.getBytes(), stegoKey);

      String format = "bmp";
      JavaByte[] f = Utils.convertToJavaByte(mtr.toArray());
      newImg = Utils.createNewImage(img, f);
      File outputfile = new File(filePath + "saved." + format);
      //Utils.compressImage(outputfile, newImg, 80);
      ImageIO.write(newImg, format, outputfile);

      System.out.println("\nTry to read message from empty container : \n" + strBegin.substring(0, message.length()));
      String str1 = new String(stegoAlg.readMessage(mtr, stegoKey));
      System.out.println("\nTry to read message from in memory img : \n" + str1.substring(0, message.length() +4));

      BufferedImage img2 = ImageIO.read(new File(filePath + "saved." + format));
      JavaByte[] pixels2 = Utils.readPixels(img2);
      MatrixPixels mtr2 = new MatrixPixels(Utils.javaByteToRgb(pixels2), img.getWidth(), img.getHeight());
      byte[] bytes2 = stegoAlg.readMessage(mtr2, stegoKey);
      String str2 = new String(bytes2);
      System.out.println("\nTry to read message from new img : \n" + str2.substring(0, message.length()));

    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(e);
    }
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
