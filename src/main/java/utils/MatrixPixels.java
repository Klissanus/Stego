package utils;

import colorspace.ColorSpace;
import colorspace.Rgb;
import com.sun.istack.internal.NotNull;
import transforms.Dct;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Klissan on 02.12.2016.
 */
public class MatrixPixels {
  private int blockSize = 8;
  private int width;
  private int height;
  private int sizeX;
  private int sizeY;
  private Class type;
  private ArrayList<ArrayList<ColorSpace[][]>> matrixs = new ArrayList<>();
  private ArrayList<ColorSpace[][]> rightOffset = new ArrayList<>();
  private ArrayList<ColorSpace[][]> downOffset = new ArrayList<>();
  private ColorSpace[][] rightDownCorner;

  public MatrixPixels(MatrixPixels mp) {
    this.width = mp.width;
    this.height = mp.height;
    this.sizeX = mp.sizeX;
    this.sizeY = mp.sizeY;
    this.type = mp.type;
    this.matrixs = new ArrayList<>(mp.matrixs);
    this.rightOffset = new ArrayList<>(mp.rightOffset);
    this.downOffset = new ArrayList<>(mp.downOffset);
    this.rightDownCorner = mp.rightDownCorner;//todo new
//    for (int i = 0; i < mp.rightDownCorner.length; i++) {
//      for (int j = 0; j < mp.rightDownCorner[0].length; j++) {
//        this.rightDownCorner[i][j] = mp.rightDownCorner[i][j].;
//      }
//    }
  }

  public ArrayList<ArrayList<ColorSpace[][]>> getPrimaryMatrixs() {
    return matrixs;
  }

  public int getPrimaryMatrixCount() {
    return sizeX * sizeY;
  }

  public MatrixPixels(ColorSpace[] pixels, int width, int height) {
    this.type = pixels[0].getClass();
    this.width = width;
    this.height = height;
    sizeX = width / blockSize;
    sizeY = height / blockSize;
    int offsetX = width % blockSize;
    int offsetY = height % blockSize;
    //заполняем блоки blockSize х blockSize
    for (int j = 0; j < sizeY; j++) {
      matrixs.add(new ArrayList<ColorSpace[][]>());
      for (int i = 0; i < sizeX; i++) {
        ColorSpace[][] matrix = new ColorSpace[blockSize][blockSize];
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
      ColorSpace[][] matrix = new ColorSpace[blockSize][offsetX];
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
      ColorSpace[][] matrix = new ColorSpace[offsetY][blockSize];
      for (int j = 0; j < offsetY; j++) {
        for (int i = 0; i < blockSize; i++) {
          matrix[j][i] = pixels[offset + j * width + blockSize * t + i];
        }
      }
      downOffset.add(matrix);
    }

    //заполняем угол
    rightDownCorner = new ColorSpace[offsetY][offsetX];
    for (int j = 0; j < offsetY; j++) {
      for (int i = 0; i < offsetX; i++) {
        rightDownCorner[j][i] = pixels[sizeY * blockSize * width + j * width + sizeX * blockSize + i];
      }
    }
  }

  public ColorSpace[] toArray() {
    ColorSpace[] result = (ColorSpace[]) Array.newInstance(this.type, width * height);
    //ColorSpace[] result = new ColorSpace[width * height];
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
/*

  public HashMap<Color, MatrixPixels> changeBlocksColor(Rgb empty, Rgb correct, Rgb broken,
                                Set<Color> usedColors, @NotNull byte[] key, ArrayList<Integer> errpos) {

    HashMap<Color, MatrixPixels> map = new HashMap<>();
    map.put(Color.RED, new MatrixPixels(this));
    map.put(Color.GREEN, new MatrixPixels(this));
    map.put(Color.BLUE, new MatrixPixels(this));
    map.forEach( (k,v) -> v.recolorEdgeBlocks());

    Map<Color, Set<Color>> colorToChange = new HashMap<>();
    HashSet<Color> gb = new HashSet<>(); gb.add(Color.RED); gb.add(Color.GREEN); gb.add(Color.BLUE);
    HashSet<Color> rb = new HashSet<>(); rb.add(Color.RED); rb.add(Color.GREEN); rb.add(Color.BLUE);
    HashSet<Color> rg = new HashSet<>(); rg.add(Color.RED); rg.add(Color.GREEN); rg.add(Color.BLUE);
    colorToChange.put(Color.RED, gb);
    colorToChange.put(Color.GREEN, rb);
    colorToChange.put(Color.BLUE, rg);

    Set<Color> colorsSet = new HashSet<>();
    colorsSet.add(Color.RED);
    colorsSet.add(Color.GREEN);
    colorsSet.add(Color.BLUE);

    BitSet keyBits = BitSet.valueOf(key);
    int keySize = key.length * 8;
    int keyBitIndex = 0;

    int num = -1;
    int k=0;
    for (int i = 0; i < this.getPrimaryMatrixs().size(); i++) {
      for (int j = 0; j < this.getPrimaryMatrixs().get(i).size(); j++) {
        for (Color color : usedColors) {
          keyBitIndex = (keyBitIndex == keySize) ? 0 : keyBitIndex;
          if (!keyBits.get(keyBitIndex++)) {
            map.get(color).recolorBlock(map.get(color).getPrimaryMatrixs().get(i).get(j), empty, colorsSet);
            continue;
          }
          num++;
          if(errpos.size() != 0 && k < errpos.size() &&  num == errpos.get(k)){
            map.get(color).recolorBlock(map.get(color).getPrimaryMatrixs().get(i).get(j), broken, colorsSet);
            //map.get(color).recolorBlock(map.get(color).getPrimaryMatrixs().get(i).get(j), broken, colorToChange.get(color));
            k++;
          }else{
            map.get(color).recolorBlock(map.get(color).getPrimaryMatrixs().get(i).get(j), correct, colorsSet);
          }
        }
      }
    }
    System.out.println("max num = " + num);
    return map;
  }

  private void recolorEdgeBlocks(){
    Rgb color = new Rgb((byte) 0x00, (byte)0x00,(byte)0x00);
    Set<Color> colorsSet = new HashSet<>();
    colorsSet.add(Color.RED);
    colorsSet.add(Color.GREEN);
    colorsSet.add(Color.BLUE);

    this.downOffset.forEach(c -> recolorBlock(c, color, colorsSet));
    this.rightOffset.forEach(c -> recolorBlock(c, color, colorsSet));
    recolorBlock(this.rightDownCorner, color, colorsSet);
  }

  private void recolorBlock(Rgb[][] block, Rgb color, Set<Color> colors){
    for (Rgb[] aC : block) {
      for (Rgb anAC : aC) {
        colors.forEach(c -> anAC.setComponent(c, color.getComponent(c)));
      }
    }
  }
}
*/
