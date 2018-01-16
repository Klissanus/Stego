package stegoalgorithms.blockcheck;

import transforms.Dct;

/**
 * Created by Klissan on 19.01.2017.
 */
public class CheckLfHf
    implements CheckBlock
{
  private double lfCoeff;
  private double hfCoeff;

  public CheckLfHf(double lf, double hf){
    this.lfCoeff = lf;
    this.hfCoeff = hf;
  }

  @Override
  public boolean isSatisfy(Dct dctMatrix) {
    return dctMatrix.getLFSummary() < lfCoeff && dctMatrix.getHFSummary() > hfCoeff;
  }
}
