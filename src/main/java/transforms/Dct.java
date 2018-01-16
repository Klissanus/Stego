package transforms;

import colorspace.ColorSpace;
import colorspace.Component;
import colorspace.bytemapping.ByteMapping;
import colorspace.bytemapping.Map256;

/**
 * Created by Klissan on 02.12.2016.
 * Блок ДКП
 */
public class Dct {
  private static int SIZE = 8;
  private double[][] dctMatrix;

  static {
    e = new double[SIZE][SIZE];
    cos_t = new double[SIZE][SIZE];
    calculateCoefficients();
    calculateCosins();
  }

  public Dct(ColorSpace[][] matrix, Component colorType) {
    dctMatrix = new double[matrix.length][matrix[0].length];
    for (int u = 0; u < matrix.length; u++) {
      for (int v = 0; v < matrix.length; v++) {
        double coeff = 0.0;
        for (int x = 0; x < matrix.length; x++) {
          for (int y = 0; y < matrix.length; y++) {
            double value = matrix[x][y].getComponent(colorType);
            coeff += cos_t[u][x] * cos_t[v][y] * value;
          }
        }
        dctMatrix[u][v] = e[u][v] * coeff;
      }
    }
  }

  public double[][] getDctMatrix() {
    return dctMatrix;
  }

  public double getDc(){
    return dctMatrix[0][0];
  }

  public double get(int zigZagIndex){
    int i = 1;
    int j = 1;
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
    return dctMatrix[i][j];
  }


  public double[][] idct() {
    double min = Double.MAX_VALUE;
    double max = Double.MIN_VALUE;
    double[][] idctCoeffs = new double[dctMatrix.length][dctMatrix.length];
    for (int x = 0; x < dctMatrix.length; x++) {
      for (int y = 0; y < dctMatrix.length; y++) {
        double coeff = 0.0;
        for (int u = 0; u < dctMatrix.length; u++) {
          for (int v = 0; v < dctMatrix.length; v++) {
            coeff += e[u][v] * dctMatrix[u][v] * cos_t[u][x] * cos_t[v][y];
          }
        }
        idctCoeffs[x][y] = coeff;/*
        min = (coeff < min) ? (coeff) : (min);
        max = (coeff > max) ? (coeff) : (max);
        result[x][y] = bm.inverse(Math.round(coeff));*/
      }
    }
    return idctCoeffs;
    //return normalisation(idctCoeffs, min, max);
  }

  public double getLFSummary(){
    double result = 0.0;
    for (int j = 0; j < 7; j++) {
      result += this.dctMatrix[0][j];
    }
    for (int i = 1; i < 7; i++) {
      for (int j = 0; j < 7 - i ; j++) {
        result += this.dctMatrix[i][j];
      }
    }
    return result;
  }

  public double getHFSummary(){
    double result = 0.0;
    for (int i = 2; i < 8; i++) {
      for (int j = 7; j > 7 - i + 1 ; j--) {
        result += this.dctMatrix[i][j];
      }
    }
    return result;
  }
/*
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
  }*/

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
      //System.out.println();
      for (int j = 0; j < size; j++) {
        cos_t[i][j] = Math.cos(Math.PI * i * (2.0 * j + 1.0) / (2.0 * size));
        //System.out.print(" " + cos_t[i][j]);
      }
    }
  }

  private static void calculateCoefficients() {
    int size = 8;
    for (int i = 0; i < size; i++) {
      //System.out.println();
      for (int j = 0; j < size; j++) {
        double x = (i != 0) ? (1.0) : (1.0 / Math.sqrt(2.0));
        double y = (j != 0) ? (1.0) : (1.0 / Math.sqrt(2.0));
        e[i][j] = x * y / Math.sqrt(2.0 * size);
        //System.out.print(" " + e[i][j]);
      }
    }
  }


  private static double[][] cos_t;

  private static double[][] e;
}


