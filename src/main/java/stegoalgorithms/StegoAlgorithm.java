package stegoalgorithms;

import utils.MatrixPixels;

import java.util.BitSet;

/**
 * Created by Klissan on 08.12.2016.
 */
public interface StegoAlgorithm {
  void hide(MatrixPixels where, byte[] msg, byte[] key);

  byte[] readMessage(MatrixPixels from, byte[] key);

  default BitSet getBits(String string) {
    return BitSet.valueOf(string.getBytes());
  }
}
