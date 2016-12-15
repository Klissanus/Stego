package stegoalgorithms;

import utils.MatrixPixels;

/**
 * Created by Klissan on 10.12.2016.
 */
public class BenhamMemonYeoYeung implements StegoAlgorithm {

  BenhamMemonYeoYeung() {

  }

  @Override
  public void hide(MatrixPixels where, byte[] msg, byte[] key) {

  }

  @Override
  public byte[] readMessage(MatrixPixels from, byte[] key) {

    return new byte[0];
  }
}
