package stegoalgorithms.blockcheck;

import transforms.Dct;

/**
 * Created by Klissan on 19.01.2017.
 */
public class CheckLf
    implements CheckBlock
{
  private double lfCoeff;

  public CheckLf(double lfCoeff){
    this.lfCoeff = lfCoeff;
  }

  @Override
  public boolean isSatisfy(Dct dctMatrix) {
    return dctMatrix.getLFSummary() < lfCoeff;//Sum < coeff
  }
}
