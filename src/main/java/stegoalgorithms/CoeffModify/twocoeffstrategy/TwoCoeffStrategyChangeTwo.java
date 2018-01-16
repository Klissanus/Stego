package stegoalgorithms.CoeffModify.twocoeffstrategy;

import transforms.Dct;

/**
 * Created by Klissan on 19.01.2017.
 */
public class TwoCoeffStrategyChangeTwo implements TwoCoeffStrategy {
  @Override
  public void modify(Dct dct, boolean bit, double diffValue, int firstX, int firstY, int secondX, int secondY) {
    final double c = 0.0; //diffValue/5;
    double first = dct.getDctMatrix()[firstX][firstY];
    double absFirst = Math.abs(first);
    double signFirst = Math.signum(first);
    double second = dct.getDctMatrix()[secondX][secondY];
    double absSecond = Math.abs(second);
    double signSecond = Math.signum(second);
    double diff = absFirst - absSecond;
    if (bit) {
      while (diff > -diffValue) {
        if (absFirst > 1) {
          dct.getDctMatrix()[firstX][firstY] += -signFirst;
          absFirst--;
        }
        dct.getDctMatrix()[secondX][secondY] += signSecond;
        absSecond++;
        diff = absFirst - absSecond;
      }
    } else {
      while (diff < diffValue) {
        if (absSecond > 1) {
          dct.getDctMatrix()[secondX][secondY] += -signSecond;
          absSecond--;
        }
        dct.getDctMatrix()[firstX][firstY] += signFirst;
        absFirst++;
        diff = absFirst - absSecond;
      }
    }
  }
}
