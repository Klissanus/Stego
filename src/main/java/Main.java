import colorspace.RgbPixels;
import stegoalgorithms.KochZhao;
import stegoalgorithms.StegoAlgorithm;
import utils.MatrixPixels;
import utils.Utils;

import javax.imageio.ImageIO;
import java.applet.Applet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Klissan on 22.11.2016.
 */
public class Main extends Applet {

  BufferedImage img;
  BufferedImage newImg;
  byte[] stegoKey = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
  String filePath = "C:\\Users\\Klissan\\IdeaProjects\\Stego\\src\\main\\resources\\";

  @Override
  public void init() {
    super.init();
    System.out.println(System.getProperty("user.dir"));

    String message = "Make St3g0 great aGaiN NRNU MEPhI, klissan_ h3llo world !1";

    try {
      Set<Color> colorsSet = new HashSet<>();
      colorsSet.add(Color.BLUE);
      //colorsSet.add(Color.GREEN);
      //colorsSet.add(Color.RED);

      //todo разобратьтся с альфа каналом png
      img = ImageIO.read(new File(filePath + "orlans.jpg"));
      RgbPixels pixels = Utils.readPixels(img);
      MatrixPixels mtr = new MatrixPixels(pixels.getPixels(), img.getWidth(), img.getHeight());
      System.out.println("Available bytes capacity = " + mtr.getPrimaryMatrixCount() / 8);

      StegoAlgorithm stegoAlg = new KochZhao(100, 4, 5, 5, 4, colorsSet);

      byte[] bytesBegin = stegoAlg.readMessage(mtr, stegoKey);
      String strBegin = new String(bytesBegin);

      System.out.println("message byte size = " + message.getBytes().length);
      stegoAlg.hide(mtr, message.getBytes(), stegoKey);

      String format = "bmp";
      RgbPixels f = new RgbPixels(mtr.toArray());
      newImg = Utils.createNewImage(img, f);
      File outputfile = new File(filePath + "saved." + format);
      //Utils.compressImage(outputfile, newImg, 100);
      ImageIO.write(newImg, format, outputfile);

      System.out.println("\nTry to read message from empty container : \n" + strBegin.substring(0, message.length()));
      String str1 = new String(stegoAlg.readMessage(mtr, stegoKey));
      System.out.println("\nTry to read message from in memory img : \n" + str1.substring(0, message.length() +4));

      BufferedImage img2 = ImageIO.read(new File(filePath + "saved.bmp"));
      RgbPixels pixels2 = Utils.readPixels(img2);
      MatrixPixels mtr2 = new MatrixPixels(pixels2.getPixels(), img.getWidth(), img.getHeight());
      byte[] bytes2 = stegoAlg.readMessage(mtr2, stegoKey);
      String str2 = new String(bytes2);
      System.out.println("\nTry to read message from new img : \n" + str2.substring(0, message.length()));
/*
            for(int i=0; i < pixels.getPixels().length; i++){
                if(pixels.getPixels()[i].getColor(Color.RED) != pixels2.getPixels()[i].getColor(Color.RED)){
                    System.out.println("Wrong pixel " + i);
                    System.out.println("R: " + pixels.getPixels()[i].getColor(Color.RED) + "!=" + pixels2.getPixels()[i].getColor(Color.RED) );
                }
                if(pixels.getPixels()[i].getColor(Color.GREEN) != pixels2.getPixels()[i].getColor(Color.GREEN)){
                    System.out.println("Wrong pixel " + i);
                    System.out.println("G: " + pixels.getPixels()[i].getColor(Color.GREEN) + "!=" + pixels2.getPixels()[i].getColor(Color.GREEN) );
                }
                if(pixels.getPixels()[i].getColor(Color.BLUE) != pixels2.getPixels()[i].getColor(Color.BLUE)){
                    System.out.println("Wrong pixel " + i);
                    System.out.println("B: " + pixels.getPixels()[i].getColor(Color.BLUE) + "!=" + pixels2.getPixels()[i].getColor(Color.BLUE) );
                }
            }*/

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
