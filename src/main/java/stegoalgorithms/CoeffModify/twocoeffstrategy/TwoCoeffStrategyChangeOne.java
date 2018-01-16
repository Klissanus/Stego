package stegoalgorithms.CoeffModify.twocoeffstrategy;

import transforms.Dct;

/**
 * Created by Klissan on 19.01.2017.
 */
public class TwoCoeffStrategyChangeOne implements TwoCoeffStrategy {
  @Override
  public void modify(Dct dct, boolean bit, double diffValue, int firstX, int firstY, int secondX, int secondY) {

    final double c = 0.0; //diffValue/5;
    double first = dct.getDctMatrix()[firstX][firstY];
    double absFirst = Math.abs(first);
    double second = dct.getDctMatrix()[secondX][secondY];
    double absSecond = Math.abs(second);
    double diff = absFirst - absSecond;
    if (bit) {
      if (diff > -(diffValue + c)) {
        double newValue = absFirst + diffValue - c;
        dct.getDctMatrix()[secondX][secondY] = (second >= 0) ? newValue : -newValue;
      }
    } else {
      if (diff < diffValue + c) {
        double newValue = absSecond + diffValue + c;
        dct.getDctMatrix()[firstX][firstY] = (first >= 0) ? newValue : -newValue;
      }
    }
  }
}
