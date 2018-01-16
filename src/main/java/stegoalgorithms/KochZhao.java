package stegoalgorithms;

import colorspace.*;
import com.sun.istack.internal.NotNull;
import transforms.Dct;
import utils.PixelsMatrix;

import java.util.BitSet;
import java.util.Set;

/**
 * Created by Klissan on 08.12.2016.
 */

public class KochZhao{
  private double diffValue;
  private int firstCoeffX;
  private int firstCoeffY;
  private int secondCoeffX;
  private int secondCoeffY;
  private Set<Component> usedColors;

  public KochZhao(double diffValue, int firstCoeffX, int firstCoeffY, int secondCoeffX, int secondCoeffY, Set<Component> usedColors) {
    this.diffValue = diffValue;
    this.firstCoeffX = firstCoeffX;
    this.firstCoeffY = firstCoeffY;
    this.secondCoeffX = secondCoeffX;
    this.secondCoeffY = secondCoeffY;
    this.usedColors = usedColors;
  }


  public void hide(PixelsMatrix where, @NotNull byte[] msg, @NotNull byte[] key) {
    int bitCount = msg.length * 8;
    //System.out.println("Message bits capacity = " + bitCount);

    BitSet msgBits = BitSet.valueOf(msg);
    int msgBitIndex = 0;
    BitSet keyBits = BitSet.valueOf(key);
    int keySize = key.length * 8;
    int keyBitIndex = 0;

    for (int i = 0; i < where.getPrimaryMatrixs().size(); i++) {
      for (int j = 0; j < where.getPrimaryMatrixs().get(i).size(); j++) {
        for (Component component : usedColors) {

          keyBitIndex = (keyBitIndex == keySize) ? 0 : keyBitIndex;
          if (!keyBits.get(keyBitIndex++)) {
            continue;
          }

          Dct dct = new Dct(where.getPrimaryMatrixs().get(i).get(j), component);

          insertBitByChangeOneCoeff(dct, msgBits.get(msgBitIndex++));

          double[][] idct = dct.idct();
          replaceBytes(where.getPrimaryMatrixs().get(i).get(j), idct, component);

          if (msgBitIndex == bitCount) {
            msgBitIndex=0;
            //return;
          }
        }
      }
    }
  }

  public byte[] readMessage(PixelsMatrix from, byte[] key) {
    BitSet bits = new BitSet(from.getPrimaryMatrixCount());
    int bitIndex = 0;
    BitSet keyBits = BitSet.valueOf(key);
    int keySize = key.length * 8;
    int keyBitIndex = 0;


    for (int i = 0; i < from.getPrimaryMatrixs().size(); i++) {
      for (int j = 0; j < from.getPrimaryMatrixs().get(i).size(); j++) {
        for (Component component : usedColors) {
          keyBitIndex = (keyBitIndex == keySize) ? 0 : keyBitIndex;
          if (!keyBits.get(keyBitIndex++)) {
            continue;
          }
          Dct dct = new Dct(from.getPrimaryMatrixs().get(i).get(j), component);
          bits.set(bitIndex++, extractBit(dct));
        }
      }
    }
    bits.set(bitIndex, bitIndex + 8);
    return bits.toByteArray();
  }

  private void insertBitByChangeTwoCoeff(Dct dct, boolean bit) {
    final double c = 0.0; //diffValue/5;
    double first = dct.getDctMatrix()[firstCoeffX][firstCoeffY];
    double absFirst = Math.abs(first);
    double signFirst = Math.signum(first);
    double second = dct.getDctMatrix()[secondCoeffX][secondCoeffY];
    double absSecond = Math.abs(second);
    double signSecond = Math.signum(second);
    double diff = absFirst - absSecond;
    if (bit) {
      while (diff > -diffValue) {
        if (absFirst > 1) {
          dct.getDctMatrix()[firstCoeffX][firstCoeffY] += -signFirst;
          absFirst--;
        }
        dct.getDctMatrix()[secondCoeffX][secondCoeffY] += signSecond;
        absSecond++;
        diff = absFirst - absSecond;
      }
    } else {
      while (diff < diffValue) {
        if (absSecond > 1) {
          dct.getDctMatrix()[secondCoeffX][secondCoeffY] += -signSecond;
          absSecond--;
        }
        dct.getDctMatrix()[firstCoeffX][firstCoeffY] += signFirst;
        absFirst++;
        diff = absFirst - absSecond;
      }
    }
  }

  private void insertBitByChangeOneCoeff(Dct dct, boolean bit) {
    final double c = 0.0; //diffValue/5;
    double first = dct.getDctMatrix()[firstCoeffX][firstCoeffY];
    double absFirst = Math.abs(first);
    double second = dct.getDctMatrix()[secondCoeffX][secondCoeffY];
    double absSecond = Math.abs(second);
    double diff = absFirst - absSecond;
    if (bit) {
      if (diff > -(this.diffValue + c)) {
        double newValue = absFirst + diffValue + c;
        dct.getDctMatrix()[secondCoeffX][secondCoeffY] = (second >= 0) ? newValue : -newValue;
      }
    } else {
      if (diff < this.diffValue + c) {
        double newValue = absSecond + diffValue + c;
        dct.getDctMatrix()[firstCoeffX][firstCoeffY] = (first >= 0) ? newValue : -newValue;
      }
    }
  }

  private boolean extractBit(Dct dct) {
    double first = dct.getDctMatrix()[firstCoeffX][firstCoeffY];
    double absFirst = Math.abs(first);
    double second = dct.getDctMatrix()[secondCoeffX][secondCoeffY];
    double absSecond = Math.abs(second);
    double diff = absFirst - absSecond;
    return (diff < 0) ? true : false;
  }


  private void replaceBytes(ColorSpace[][] source, double[][] idct, Component color) {
    int i = 0;
    for (int m = 0; m < source.length; m++) {
      for (int n = 0; n < source[0].length; n++) {
                /*if(Math.round(source[m][n].getComponent(color)) != Math.round(idct[m][n])){
                    System.out.println("\n PIXEL NUM = " + i++);
                    System.out.println("source byte = " + source[m][n].getComponent(color));
                    System.out.println("replace byte = " + idct[m][n]);
                }*/
        /*if (source[m][n].getComponent(color) >= 0
            && bytes[m][n] < 0
            || source[m][n].getComponent(color) < 0
            && bytes[m][n] > 0) {
          System.out.println("source byte = " + source[m][n].getComponent(color));
          System.out.println("replace byte = " + bytes[m][n]);
          //source[m][n].setComponent(color,(byte) source[m][n].getComponent(color)); //(Math.abs(bytes[m][n]))); // -128
          //System.out.println("replaced byte = " + source[m][n].getComponent(color));
          //byte[] b = new byte[1];
          //b[0] = source[m][n].getComponent(color);
          //System.out.println("" + BitSet.valueOf(b));
        }*/
        source[m][n].setComponent(color, idct[m][n]);
      }
    }
  }
}
