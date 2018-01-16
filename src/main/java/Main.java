import colorspace.Component;
import colorspace.JavaByte;
import colorspace.Rgb;
import genetic.Genetic;
import stegoalgorithms.CoeffModify.TwoCoeffModify;
import stegoalgorithms.CoeffModify.twocoeffstrategy.TwoCoeffStrategyChangeOne;
import stegoalgorithms.StegoAlgorithmImpl;
import stegoalgorithms.blockcheck.NoneCheck;
import utils.PixelsMatrix;
import utils.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Klissan on 27.06.2017.
 */
public class Main {

    static int mode = 1;

    static BufferedImage img;
    static BufferedImage newImg;
    static byte[] stegoKey = new byte[]{(byte) 0xFF}; //, (byte) 0x00, (byte) 0x00
    static String filePath = "C:\\Users\\Klissan\\IdeaProjects\\Stego\\src\\main\\resources\\";
    static String message = "Make Stego Great Again"; //"V love Gr1Basiabcdefghijklmnopqrstuvwxyz";
    static Set<Component> colorsSet = new HashSet<>();
    static Genetic genetic = new Genetic(true);
    static Rgb[] rgbBefore;
    static int quality = 80;
    static int P = 25;
    static int iterations = 30;
    static PixelsMatrix mtr = null;

    public static void main(String[] args) {
        try {
            colorsSet.add(Component.Y);
            //открываем файл
            img = ImageIO.read(new File(filePath + "images/Lenna.png"));
            JavaByte[] pixels = Utils.readPixels(img);
            rgbBefore = Utils.javaByteToRgb(pixels);

            calculateFitnesses(pixels);
            for (int i = 0; i < iterations ; i++) {
                System.out.println("=== ITERATION NUMBER " + i + " ===");
                genetic.selection();
                genetic.mutation();
                genetic.crossover();
                genetic.forceMutation();
                calculateFitnesses(pixels);
            }

            int[] bestcoeffs = genetic.getBestCoeffs();
            int[] coefs1 = Utils.zigZagToIndexes(bestcoeffs[0]);
            int[] coefs2 = Utils.zigZagToIndexes(bestcoeffs[1]);
            PixelsMatrix mtrMdf = new PixelsMatrix(Utils.javaByteToYCbCr(pixels), img.getWidth(), img.getHeight());
            StegoAlgorithmImpl stegoAlg = new StegoAlgorithmImpl(new NoneCheck(),
                new TwoCoeffModify(new TwoCoeffStrategyChangeOne(),
                    P, coefs1[0], coefs1[1], coefs2[0], coefs2[1]));
            stegoAlg.hide(mtrMdf, colorsSet, message.getBytes(), stegoKey);

            JavaByte[] inMemory = Utils.convertToJavaByte(mtrMdf.toArray());
            newImg = Utils.createNewImage(img, inMemory);
            String format = "bmp";
            File outputfile = new File(filePath + "result." + format);
            ImageIO.write(newImg, format, outputfile);

        }catch (IOException e){
        }
    }

    public static void calculateFitnesses(JavaByte[] pixels) throws IOException {
        mtr = (mtr == null) ? new PixelsMatrix(Utils.javaByteToYCbCr(pixels), img.getWidth(), img.getHeight()) : mtr;
        for (int i = 0; i < genetic.getSize(); i++) {
            int[] chrom = genetic.get(i);
            int[] coefs1 = Utils.zigZagToIndexes(chrom[0]);
            int[] coefs2 = Utils.zigZagToIndexes(chrom[1]);
            System.out.println("-- Chrom number " + i + " --");
            System.out.println("Coef1 = " + coefs1[0] + ' ' + coefs1[1] + " zigzag= " + chrom[0]);
            System.out.println("Coef2 = " + coefs2[0] + ' ' + coefs2[1] + " zigzag= " + chrom[1]);

            PixelsMatrix mtrMdf = new PixelsMatrix(Utils.javaByteToYCbCr(pixels), img.getWidth(), img.getHeight());
            StegoAlgorithmImpl stegoAlg = new StegoAlgorithmImpl(new NoneCheck(),
                new TwoCoeffModify(new TwoCoeffStrategyChangeOne(),
                    P, coefs1[0], coefs1[1], coefs2[0], coefs2[1]));
            stegoAlg.hide(mtrMdf, colorsSet, message.getBytes(), stegoKey);

            JavaByte[] inMemory = Utils.convertToJavaByte(mtrMdf.toArray());
            Rgb[] rgbAfter = Utils.javaByteToRgb(inMemory);

            //сохраняем
            String format = "jpg";
            String pathName = filePath + i + "saved." + format;
            newImg = Utils.createNewImage(img, inMemory);
            File outputfile = new File(pathName);
            Utils.compressImage(outputfile, newImg, quality);

            //считываем из сохраненки
            BufferedImage savedImg = ImageIO.read(new File(pathName));
            JavaByte[] readPixels = Utils.readPixels(savedImg);
            PixelsMatrix readMtr = new PixelsMatrix(Utils.javaByteToYCbCr(readPixels), img.getWidth(), img.getHeight());
            byte[] readBytes = stegoAlg.read(readMtr, colorsSet, stegoKey);

            genetic.fitnessFunction(rgbBefore, rgbAfter, message.getBytes(), readBytes, i);
        }
    }
}