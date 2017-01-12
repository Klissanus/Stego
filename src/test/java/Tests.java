import colorspace.RgbPixels;
import org.junit.Test;
import stegoalgorithms.KochZhao;
import stegoalgorithms.StegoAlgorithm;
import transforms.bytemapping.ByteMapping;
import transforms.bytemapping.Map256;
import utils.MatrixPixels;
import utils.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Klissan on 16.12.2016.
 */
public class Tests {

  BufferedImage img;
  BufferedImage newImg;
  String filePath = "C:\\Users\\Klissan\\IdeaProjects\\Stego\\src\\main\\resources\\";
  String outputPath = "D:\\Stego\\";
  String fileName = "images/orlans.jpg";

  byte[] stegoKey = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
  String message = "H3ll0 W0r1d, this message wants to make stego great again";
  Set<Color> colorsSet = new HashSet<>();

  private MatrixPixels initTest() {
    colorsSet.add(Color.BLUE);
    //colorsSet.add(Color.GREEN);
    //colorsSet.add(Color.RED);
/*
    stegoKey = new byte[2048];
    for (int i = 0; i < 2048; i++) {
      stegoKey[i] = (byte) (Math.random() * 256 - 128);
    }*/
    MatrixPixels mtr = null;
    try {
      //open source file
      img = ImageIO.read(new File(filePath + fileName));
      RgbPixels pixels = Utils.readPixels(img);
      mtr = new MatrixPixels(pixels.getPixels(), img.getWidth(), img.getHeight());


    } catch (IOException e) {
      System.out.println(e);
    }
    return mtr;
  }

  void testAll(MatrixPixels sourceMtr) throws IOException {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < i; j++) {
        int index1 = i;
        int index2 = j;
        System.out.println("\n xxx xxx xxx INDEXES xxx xxx xxx " + i + " " + j);

        double startP = 10;
        double stepP = 15;
        double limitP = 150;
        for (double coeffP = startP; coeffP < limitP + 1; coeffP += stepP) {
          System.out.println("\n----Coeff P ----- = " + coeffP);

          File dir = new File(outputPath + "Index_" + i + j +"\\P_" + coeffP);
          dir.mkdirs();

          int startComp = 100;
          int stepComp = 5;
          int limitComp = 60;
          for (int comp = startComp; comp > limitComp - 1; comp -= stepComp) {
            System.out.println("\n----Compression = " + comp);
            MatrixPixels mp = new MatrixPixels(sourceMtr);
            StegoAlgorithm stegoAlg = new KochZhao(coeffP, index1, index2, index2, index1, colorsSet);

            stegoAlg.hide(mp, message.getBytes(), stegoKey);

            RgbPixels f = new RgbPixels(mp.toArray());
            newImg = Utils.createNewImage(img, f);
            String filename = "Index_" + i + j +"\\P_" + coeffP + "\\comp_" + comp + ".";
            String format = "jpg";
            File outputfile = new File(outputPath + filename + format);
            Utils.compressImage(outputfile, newImg, comp);

            newImg = ImageIO.read(new File(outputPath + filename + format));
            MatrixPixels nmp = new MatrixPixels(Utils.readPixels(newImg).getPixels(), newImg.getWidth(), newImg.getHeight());

            compareImages(img, newImg);
            ArrayList<Integer> errs = compareMessages(message.getBytes(), stegoAlg.readMessage(nmp, stegoKey));

          }
        }
      }
    }
  }


 /* void testStegoIndexQuality(MatrixPixels sourceMtr) throws IOException{
    System.out.println("message byte size = " + message.getBytes().length);
    int start = 40;
    double step = 10;
    double limit = 70;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < i; j++) {
        int index1 = i;
        int index2 = j;
      }
  }*/



 @Test
  public void testStegoCompressionQuality() throws IOException{
   MatrixPixels sourceMtr = initTest();
    System.out.println("message byte size = " + message.getBytes().length);
    int[] indexes = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
    int index1 = 4;
    int index2 = 5;
    int start = 100;
    int step = 5;
    int limit = 60;
    double coeff = 100;
    for (int comp = start; comp > limit; comp -= step) {
      System.out.println("\n--Compression = " + comp);
      MatrixPixels mp = new MatrixPixels(sourceMtr);
      StegoAlgorithm stegoAlg = new KochZhao(coeff, index1, index2, index2, index1, colorsSet);

      stegoAlg.hide(mp, message.getBytes(), stegoKey);

      RgbPixels f = new RgbPixels(mp.toArray());
      newImg = Utils.createNewImage(img, f);
      String filename = "CompQuality\\coeff_" + comp + ".";
      String format = "jpg";
      File outputfile = new File(filePath + filename + format);
      Utils.compressImage(outputfile, newImg, comp);


      newImg = ImageIO.read(new File(filePath + filename + format));
      MatrixPixels nmp = new MatrixPixels(Utils.readPixels(newImg).getPixels(), newImg.getWidth(), newImg.getHeight());

      compareImages(img, newImg);
      System.out.println("blocks = " + nmp.getPrimaryMatrixCount());
      ArrayList<Integer> errs = compareMessages(message.getBytes(), stegoAlg.readMessage(nmp, stegoKey));
/*

      Rgb empty = new Rgb((byte) 0, (byte) 0, (byte) 0);
      Rgb correct = new Rgb((byte) -128, (byte) -128, (byte) -128);
      Rgb error = new Rgb((byte) -1, (byte) -1, (byte) -1);

      HashMap<Color, MatrixPixels> matrImgs = mp.changeBlocksColor(empty, correct, error, colorsSet, stegoKey, errs);
      double fc = coeff;
      matrImgs.forEach((k,v) -> {
        try {
          RgbPixels f1 = new RgbPixels(v.toArray());
          newImg = Utils.createNewImage(img, f1);
          String filename1 = "CompQuality\\coeff_" + fc + "_bitmap_" + k + ".";
          File outputfile1 = new File(filePath + filename1 + "bmp");
          ImageIO.write(newImg, format, outputfile1);
        }catch (IOException e){
          System.err.println(e);
        }
      });*/

    }
  }

  @Test
  public void testStegoCoeffQuality() throws IOException{
    MatrixPixels sourceMtr = initTest();
    System.out.println("message byte size = " + message.getBytes().length);
    int[] indexes = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
    int index1 = 4;
    int index2 = 5;
    double start = 20;
    double step = 20;
    double limit = 100;
    for (double coeff = start; coeff < limit + 1; coeff+= step) {
      System.out.println("\n--Coeff = " + coeff);
      MatrixPixels mp = new MatrixPixels(sourceMtr);
      StegoAlgorithm stegoAlg = new KochZhao(coeff, index1, index2, index2, index1, colorsSet);

      stegoAlg.hide(mp, message.getBytes(), stegoKey);

      RgbPixels f = new RgbPixels(mp.toArray());
      newImg = Utils.createNewImage(img, f);
      String filename = "CoeffQuality\\coeff_" + coeff + ".";
      String format = "jpg";
      File outputfile = new File(filePath + filename + format);
      if( format.equals("bmp")){
        ImageIO.write(newImg, format, outputfile);
      }
      if (format.equals("jpg")){
        Utils.compressImage(outputfile, newImg, 100);
      }

      newImg = ImageIO.read(new File(filePath + filename + format));
      MatrixPixels nmp = new MatrixPixels(Utils.readPixels(newImg).getPixels(), newImg.getWidth(), newImg.getHeight());

      compareImages(img, newImg);
      System.out.println("blocks = " + nmp.getPrimaryMatrixCount());
      ArrayList<Integer> errs = compareMessages(message.getBytes(), stegoAlg.readMessage(nmp, stegoKey));
/*

      Rgb empty = new Rgb((byte) 0, (byte) 0, (byte) 0);
      Rgb correct = new Rgb((byte) -128, (byte) -128, (byte) -128);
      Rgb error = new Rgb((byte) -1, (byte) -1, (byte) -1);

      HashMap<Color, MatrixPixels> matrImgs = mp.changeBlocksColor(empty, correct, error, colorsSet, stegoKey, errs);
      double fc = coeff;
      matrImgs.forEach((k,v) -> {
        try {
          RgbPixels f1 = new RgbPixels(v.toArray());
          newImg = Utils.createNewImage(img, f1);
          String filename1 = "CoeffQuality\\coeff_" + fc + "_bitmap_" + k + ".";
          File outputfile1 = new File(filePath + filename1 + "bmp");
          ImageIO.write(newImg, format, outputfile1);
        }catch (IOException e){
          System.err.println(e);
        }
      });
*/

    }
  }



  private void compareImages(BufferedImage source, BufferedImage stegoResult){
    ByteMapping bmap = new Map256();
    int w = source.getWidth();
    int h = source.getHeight();
    int size =  w * h * 3;
    int[] sourceBytes = source.getData().getPixels(0, 0, w, h, new int[size]);
    int[] stegoBytes = stegoResult.getData().getPixels(0, 0, w, h, new int[size]);
    long sumDiff = 0;

    int maxDiff = 0;
    for(int i = 0; i < size; ++i){
      int sourceb = bmap.forward((byte) sourceBytes[i]);
      int stegob = bmap.forward((byte) stegoBytes[i]);
      int diff = Math.abs(sourceb - stegob);
      sumDiff += diff;
      maxDiff =  diff > maxDiff ? diff : maxDiff;
    }
    double avgDiff = (double) sumDiff / size;
    System.out.println("Max abs diff = " + maxDiff);
    System.out.println("Average abs diff = " + avgDiff);
  }

  ArrayList<Integer> compareMessages(byte[] original, byte[] read){
    ArrayList<Integer> result = new ArrayList();
    int miscount = 0;
    int originalSize = original.length * 8;
    int size = read.length * 8;
    size = size - 8;
    BitSet originalBits = BitSet.valueOf(original);
    BitSet readBits = BitSet.valueOf(read);
    int k = 0;
    for(int i = 0; i < size; i++){
      if(originalBits.get(k) != readBits.get(i)){
        miscount++;
        result.add(i);
      }
      if(++k == originalSize )
        k = 0;
    }
    System.out.println("message bit size = " + size);
    System.out.println("miscounts = " + miscount);
    System.out.println("error percent = " + 100.0 * miscount / size);
    return result;
  }
}
