package utils;

import colorspace.Rgb;

import java.util.ArrayList;

/**
 * Created by Klissan on 02.12.2016.
 */
public class MatrixPixels {
  int blockSize = 8;
  int width;
  int height;
  int sizeX;
  int sizeY;
  ArrayList<ArrayList<Rgb[][]>> matrixs = new ArrayList<ArrayList<Rgb[][]>>();
  ArrayList<Rgb[][]> rightOffset = new ArrayList<Rgb[][]>();
  ArrayList<Rgb[][]> downOffset = new ArrayList<Rgb[][]>();
  Rgb[][] rightDownCorner;

  public ArrayList<ArrayList<Rgb[][]>> getPrimaryMatrixs() {
    return matrixs;
  }

  public int getPrimaryMatrixCount() {
    return sizeX * sizeY;
  }

  public MatrixPixels(Rgb[] pixels, int width, int height) {
    this.width = width;
    this.height = height;
    sizeX = width / blockSize;
    sizeY = height / blockSize;
    int offsetX = width % blockSize;
    int offsetY = height % blockSize;
    //заполняем блоки blockSizeхblockSize
    for (int j = 0; j < sizeY; j++) {
      matrixs.add(new ArrayList<Rgb[][]>());
      for (int i = 0; i < sizeX; i++) {
        Rgb[][] matrix = new Rgb[blockSize][blockSize];
        for (int n = 0; n < blockSize; n++) {
          for (int m = 0; m < blockSize; m++) {
            matrix[n][m] = pixels[(blockSize * j + n) * width + blockSize * i + m];
          }
        }
        matrixs.get(j).add(matrix);
      }
    }
    //заполняем оставшиеся блоки справа blockSize х offX
    for (int t = 0; t < sizeY; t++) {
      Rgb[][] matrix = new Rgb[blockSize][offsetX];
      for (int j = 0; j < blockSize; j++) {
        for (int i = 0; i < offsetX; i++) {
          matrix[j][i] = pixels[t * blockSize * width + j * width + blockSize * sizeX + i];
        }
      }
      rightOffset.add(matrix);
    }

    //заполняем нижние блоки offy x blockSize
    int offset = blockSize * sizeY * width;
    for (int t = 0; t < sizeX; t++) {
      Rgb[][] matrix = new Rgb[offsetY][blockSize];
      for (int j = 0; j < offsetY; j++) {
        for (int i = 0; i < blockSize; i++) {
          matrix[j][i] = pixels[offset + j * width + blockSize * t + i];
        }
      }
      downOffset.add(matrix);
    }

    //заполняем угол
    rightDownCorner = new Rgb[offsetY][offsetX];
    for (int j = 0; j < offsetY; j++) {
      for (int i = 0; i < offsetX; i++) {
        rightDownCorner[j][i] = pixels[sizeY * blockSize * width + j * width + sizeX * blockSize + i];
      }
    }
  }

  public Rgb[] toArray() {
    Rgb[] result = new Rgb[width * height];
    for (int j = 0; j < sizeY; j++) {
      for (int i = 0; i < sizeX; i++) {
        for (int n = 0; n < blockSize; n++) {
          for (int m = 0; m < blockSize; m++) {
            result[(blockSize * j + n) * width + blockSize * i + m] = matrixs.get(j).get(i)[n][m];
          }
        }
      }
    }

    int offsetX = width % blockSize;
    int offsetY = height % blockSize;
    for (int t = 0; t < sizeY; t++) {
      for (int j = 0; j < blockSize; j++) {
        for (int i = 0; i < offsetX; i++) {
          result[t * blockSize * width + j * width + blockSize * sizeX + i] = rightOffset.get(t)[j][i];
        }
      }
    }

    int offset = blockSize * sizeY * width;
    for (int t = 0; t < sizeX; t++) {
      for (int j = 0; j < offsetY; j++) {
        for (int i = 0; i < blockSize; i++) {
          result[offset + j * width + blockSize * t + i] = downOffset.get(t)[j][i];
        }
      }
    }

    //заполняем угол
    for (int j = 0; j < offsetY; j++) {
      for (int i = 0; i < offsetX; i++) {
        result[sizeY * blockSize * width + j * width + sizeX * blockSize + i] = rightDownCorner[j][i];
      }
    }

    return result;
  }
}
