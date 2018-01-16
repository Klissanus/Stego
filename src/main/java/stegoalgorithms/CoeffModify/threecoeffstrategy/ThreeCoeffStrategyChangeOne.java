package stegoalgorithms.CoeffModify.threecoeffstrategy;

import transforms.Dct;

/**
 * Created by Klissan on 28.01.2017.
 */
public class ThreeCoeffStrategyChangeOne implements ThreeCoeffStrategy {
  @Override
  public void modify(Dct dct, boolean bit, double diffValue,
                     int firstX, int firstY, int secondX, int secondY, int thirdX, int thirdY) {

    final double c = 0.0; //diffValue/5;
    double first = dct.getDctMatrix()[firstX][firstY];
    double absFirst = Math.abs(first);
    double second = dct.getDctMatrix()[secondX][secondY];
    double absSecond = Math.abs(second);
    double third = dct.getDctMatrix()[thirdX][thirdY];
    double absThird = Math.abs(third);
    double absMin = Math.min(absFirst, absSecond);
    double absMax = Math.max(absFirst, absSecond);
    double diffMin = absMin - absThird;
    double diffMax = absThird - absMax;
    if (bit) {
      if (diffMax < diffValue + c) {
        double partOfValue = diffValue / 2;
        partOfValue = (absMin < partOfValue) ? (absMin) : partOfValue;
        absFirst -= (absThird - absFirst < diffValue) ? partOfValue : 0;
        absSecond -= (absThird - absSecond < diffValue) ? partOfValue : 0;
        absThird += diffValue - partOfValue;
        dct.getDctMatrix()[firstX][firstY] = Math.signum(first) * absFirst;
        dct.getDctMatrix()[secondX][secondY] = Math.signum(second) * absSecond;
        dct.getDctMatrix()[thirdX][thirdY] = Math.signum(third) * absThird;
      }
    } else {
      if (diffMin < diffValue + c) {
        double partOfValue = diffValue / 2;
        partOfValue = (absThird < partOfValue) ? (absThird) : partOfValue;
        absFirst += (absFirst - absThird + diffValue - partOfValue);//todo
        absSecond += diffValue - partOfValue;
        absThird -= partOfValue;
        dct.getDctMatrix()[firstX][firstY] = Math.signum(first) * absFirst;
        dct.getDctMatrix()[secondX][secondY] = Math.signum(second) * absSecond;
        dct.getDctMatrix()[thirdX][thirdY] = Math.signum(third) * absThird;
      }
    }
  }
}
