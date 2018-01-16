package stegoalgorithms.CoeffModify;

import stegoalgorithms.CoeffModify.threecoeffstrategy.ThreeCoeffStrategy;
import stegoalgorithms.CoeffModify.twocoeffstrategy.TwoCoeffStrategy;
import transforms.Dct;

/**
 * Created by Klissan on 19.01.2017.
 */
public class ThreeCoeffModify
    implements CoeffModify
{
  private ThreeCoeffStrategy strategy;
  private double p;
  private int firstX;
  private int firstY;
  private int secondX;
  private int secondY;
  private int thirdX;
  private int thirdY;

  public ThreeCoeffModify(ThreeCoeffStrategy strategy, double p,
                          int firstX, int firstY, int secondX, int secondY, int thirdX, int thirdY){
    this.strategy = strategy;
    this.p = p;
    this.firstX = firstX;
    this.firstY = firstY;
    this.secondX = secondX;
    this.secondY = secondY;
    this.thirdX = thirdX;
    this.thirdY = thirdY;
  }
  @Override
  public void insertBit(Dct dctMatrix, boolean bit) {

  }

  @Override
  public boolean extractBit(Dct dctMatrix) {
    return false;
  }
}
