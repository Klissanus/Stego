package stegoalgorithms.blockcheck;

import transforms.Dct;

/**
 * Created by Klissan on 19.01.2017.
 */
public class CheckHf
    implements CheckBlock
{
  private double hfCoeff;

  public CheckHf(double hfCoeff){
    this.hfCoeff = hfCoeff;
  }

  @Override
  public boolean isSatisfy(Dct dctMatrix) {
    return dctMatrix.getHFSummary() > hfCoeff;
  }
}
