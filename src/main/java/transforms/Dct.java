package transforms;

import colorspace.Rgb;

import java.awt.*;

/**
 * Created by Klissan on 02.12.2016.
 */
public class Dct {
  private double[][] dctMatrix;
  private ByteMapping bm = new Map256();

  static {
    e = new double[8][8];
    cos_t = new double[8][8];
    calculateCoefficients();
    calculateCosins();
  }

  public Dct(Rgb[][] matrix, Color colorType) {
    dctMatrix = new double[matrix.length][matrix[0].length];
    for (int u = 0; u < matrix.length; u++) {
      for (int v = 0; v < matrix.length; v++) {
        double coeff = 0.0;
        for (int x = 0; x < matrix.length; x++) {
          for (int y = 0; y < matrix.length; y++) {
            byte colorByte = matrix[x][y].getColor(colorType);
            int value = bm.forward(colorByte);
            coeff += cos_t[u][x] * cos_t[v][y] * value;
          }
        }
        dctMatrix[u][v] = Math.round(e[u][v] * coeff);
      }
    }
  }

  public double[][] getDctMatrix() {
    return dctMatrix;
  }


  public byte[][] idct() {
    double min = Double.MAX_VALUE;
    double max = Double.MIN_VALUE;
    byte[][] result = new byte[dctMatrix.length][dctMatrix.length];
    double[][] idctCoeffs = new double[dctMatrix.length][dctMatrix.length];
    for (int x = 0; x < dctMatrix.length; x++) {
      for (int y = 0; y < dctMatrix.length; y++) {
        double coeff = 0.0;
        for (int u = 0; u < dctMatrix.length; u++) {
          for (int v = 0; v < dctMatrix.length; v++) {
            coeff += e[u][v] * dctMatrix[u][v] * cos_t[u][x] * cos_t[v][y];
          }
        }
        min = (coeff < min) ? (coeff) : (min);
        max = (coeff > max) ? (coeff) : (max);
        idctCoeffs[x][y] = coeff;
        result[x][y] = bm.inverse(Math.round(coeff));
      }
    }
    return result;
    //return normalisation(idctCoeffs, min, max);
  }

  private byte[][] normalisation(double[][] idctCoeffs, double min, double max) {
    byte[][] result = new byte[dctMatrix.length][dctMatrix.length];
    for (int i = 0; i < idctCoeffs.length; i++) {
      for (int j = 0; j < idctCoeffs[0].length; j++) {
        double c = idctCoeffs[i][j];
        long norm = bm.normalize(idctCoeffs[i][j], min, max);
        int k = result[i][j] = bm.inverse(norm);
        int t = bm.inverse(Math.round(idctCoeffs[i][j]));
        if (t != k && Math.signum(t) != Math.signum(k)) System.out.println("inv = " + t + " inv+norm= " + k);
        k++;
      }
    }
    return result;
  }

  void print() {
    System.out.println("DCT: ");
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        System.out.print(" " + this.dctMatrix[i][j]);
      }
      System.out.println();
    }
  }


  private static void calculateCosins() {
    int size = 8;
    for (int i = 0; i < size; i++) {
      System.out.println();
      for (int j = 0; j < size; j++) {
        cos_t[i][j] = Math.cos(Math.PI * i * (2.0 * j + 1.0) / (2.0 * size));
        System.out.print(" " + cos_t[i][j]);
      }
    }
  }

  private static void calculateCoefficients() {
    int size = 8;
    for (int i = 0; i < size; i++) {
      System.out.println();
      for (int j = 0; j < size; j++) {
        double x = (i != 0) ? (1.0) : (1.0 / Math.sqrt(2.0));
        double y = (j != 0) ? (1.0) : (1.0 / Math.sqrt(2.0));
        e[i][j] = x * y / Math.sqrt(2.0 * size);
        System.out.print(" " + e[i][j]);
      }
    }
  }

  private static double[][] cos_t /*= {
            {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
            {0.9807853, 0.8314696, 0.5555702, 0.1950903, -0.1950903,-0.5555702,-0.8314696,-0.9807853},
            {0.9238795, 0.3826834,-0.3826834,-0.9238795, -0.9238795,-0.3826834, 0.3826834, 0.9238795},
            {0.8314696,-0.1950903,-0.9807853,-0.5555702, 0.5555702, 0.9807853, 0.1950903,-0.8314696},
            {0.7071068,-0.7071068,-0.7071068, 0.7071068, 0.7071068,-0.7071068,-0.7071068, 0.7071068},
            {0.5555702,-0.9807853, 0.1950903, 0.8314696, -0.8314696,-0.1950903, 0.9807853,-0.5555702},
            {0.3826834,-0.9238795, 0.9238795,-0.3826834, -0.3826834, 0.9238795,-0.9238795, 0.3826834},
            {0.1950903,-0.5555702, 0.8314696,-0.9807853, 0.9807853,-0.8314696, 0.5555702,-0.1950903}
    }*/;

  private static double[][] e /*= {
            {0.125, 0.176777777, 0.176777777, 0.176777777, 0.176777777, 0.176777777, 0.176777777, 0.176777777},
            {0.176777777, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25},
            {0.176777777, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25},
            {0.176777777, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25},
            {0.176777777, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25},
            {0.176777777, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25},
            {0.176777777, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25},
            {0.176777777, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25}
    }*/;
}


interface ByteMapping {
  int forward(byte b);

  byte inverse(long i);

  long normalize(double value, double min, double max);
}

//0-127 to 128-255;; -128 - -1 to 0 - 127
class Plus128 implements ByteMapping {

  @Override
  public int forward(byte b) {
    return b + 128;
  }

  @Override
  public byte inverse(long i) {
    return (byte) ((i > 255) ? 0x7F : ((i < 0) ? 0x80 : i - 128));
  }

  @Override
  public long normalize(double value, double min, double max) {
    double absMin = Math.abs(min);
    double signMin = Math.signum(min);
    return Math.round(((value + absMin) / (max + absMin)) * 255);
  }
}


//0-127 to 0-127;; -128 - -1 to 128 - 255
class Map256 implements ByteMapping {

  @Override
  public int forward(byte b) {
    return (b >= 0) ? (b) : (256 - Math.abs(b));
  }

  @Override
  public byte inverse(long i) {
    byte b;
    if (i > 255) {
      b = (byte) 0x80;
    }
    if (i < 0) {
      b = (byte) 0x00;
    } else {
      b = (byte) ((i < 128) ? (i) : (i - 256));
    }
    return b;
  }

  @Override
  public long normalize(double value, double min, double max) {
    double absMin = Math.abs(min);
    double signMin = Math.signum(min);
    //return Math.round((value - signMin * absMin) / (max - signMin * absMin) * 255);
    return Math.round(((value + absMin) / (max + absMin)) * 255);
  }
}

//0-127 to 127 - 0;; -128 - -1 to 255 - 128
class Minus127Abs implements ByteMapping {

  @Override
  public int forward(byte b) {
    return Math.abs(b - 127);
  }

  @Override
  public byte inverse(long i) {
    i -= 127;//not impl
    return (byte) ((i > 255) ? 0x7F : ((i < 0) ? 0x80 : i));
  }

  @Override
  public long normalize(double value, double min, double max) {
    double absMin = Math.abs(min);
    double signMin = Math.signum(min);
    return Math.round((value - signMin * absMin) / (max - signMin * absMin) * 255);
  }
}

class JavaByte implements ByteMapping {

  @Override
  public int forward(byte b) {
    return b;
  }

  @Override
  public byte inverse(long i) {
    return (byte) ((i > 127) ? 0x7F : ((i < -128) ? 0x80 : i));
  }

  @Override
  public long normalize(double value, double min, double max) {
    double absMin = Math.abs(min);
    double signMin = Math.signum(min);
    if (max > 127 && value > 0) {
      return Math.round((value / max) * 127);
    }
    if (min < -128 && value < 0) {
      return -Math.round((value / min) * 128);
    }
    return Math.round(value);
  }

}