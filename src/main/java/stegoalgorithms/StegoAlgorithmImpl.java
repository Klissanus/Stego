package stegoalgorithms;

import colorspace.ColorSpace;
import colorspace.Component;
import stegoalgorithms.CoeffModify.CoeffModify;
import stegoalgorithms.blockcheck.CheckBlock;
import transforms.Dct;
import utils.PixelsMatrix;

import java.util.BitSet;
import java.util.Set;

/**
 * Created by Klissan on 08.12.2016.
 */
public class StegoAlgorithmImpl implements StegoAlgorithm
{
  private CheckBlock checker;
  private CoeffModify coeffModify;

  public StegoAlgorithmImpl(CheckBlock checker, CoeffModify coeffModify){
    this.checker = checker;
    this.coeffModify = coeffModify;
  }

  public void hide(PixelsMatrix where, Set<Component> usedColors, byte[] msg, byte[] key){
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
          Dct dct = new Dct(where.getPrimaryMatrixs().get(i).get(j), component);
          if (!checker.isSatisfy(dct)){
            System.out.println("not satisfy i, j = " +i+j);
            break;
          }

          keyBitIndex = (keyBitIndex == keySize) ? 0 : keyBitIndex;
          if (!keyBits.get(keyBitIndex++)) {
            continue;
          }
          coeffModify.insertBit(dct, msgBits.get(msgBitIndex++));

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

  public byte[] read(PixelsMatrix from, Set<Component> usedColors, byte[] key){
    BitSet bits = new BitSet(from.getPrimaryMatrixCount());
    int bitIndex = 0;
    BitSet keyBits = BitSet.valueOf(key);
    int keySize = key.length * 8;
    int keyBitIndex = 0;


    for (int i = 0; i < from.getPrimaryMatrixs().size(); i++) {
      for (int j = 0; j < from.getPrimaryMatrixs().get(i).size(); j++) {
        for (Component component : usedColors) {
          Dct dct = new Dct(from.getPrimaryMatrixs().get(i).get(j), component);
          if (!checker.isSatisfy(dct)){
            break;
          }

          keyBitIndex = (keyBitIndex == keySize) ? 0 : keyBitIndex;
          if (!keyBits.get(keyBitIndex++)) {
            continue;
          }
          bits.set(bitIndex++, coeffModify.extractBit(dct));
        }
      }
    }
    bits.set(bitIndex, bitIndex + 8);
    return bits.toByteArray();

  }

  private void replaceBytes(ColorSpace[][] source, double[][] idct, Component color) {
    int i = 0;
    for (int m = 0; m < source.length; m++) {
      for (int n = 0; n < source[0].length; n++) {
        source[m][n].setComponent(color, idct[m][n]);
      }
    }
  }

//  default BitSet getBits(String string) {
//    return BitSet.valueOf(string.getBytes());
//  }
}
