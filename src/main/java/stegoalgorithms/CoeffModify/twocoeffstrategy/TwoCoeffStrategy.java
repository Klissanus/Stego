package stegoalgorithms.CoeffModify.twocoeffstrategy;

import transforms.Dct;

/**
 * Created by Klissan on 19.01.2017.
 */
public interface TwoCoeffStrategy {
  void modify(Dct dct, boolean bit, double p, int firstX, int firstY, int secondX, int secondY);
}
