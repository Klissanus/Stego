package colorspace;

import java.awt.*;

/**
 * Created by Klissan on 12.01.2017.
 */
public class YCbCr
    implements  ColorSpace {
  private double y;
  private double cb;
  private double cr;

  public YCbCr(double y, double cb, double cr){
    this.y = y;
    this.cb = cb;
    this.cr = cr;
  }

  public YCbCr(Rgb pixel) {
    double r = pixel.getComponent(Component.RED);
    double g = pixel.getComponent(Component.GREEN);
    double b = pixel.getComponent(Component.BLUE);
    this.y = 0 + 0.299000 * r + 0.587000 * g + 0.114000 * b;
    this.cb = 128 - 0.168736 * r - 0.331264 * g + 0.500000 * b;
    this.cr = 128 + 0.500000 * r - 0.418688 * g - 0.081312 * b;
  }

  @Override
  public double getComponent(Component component) {
    switch (component) {
      case Y:
        return y;
      case CB:
        return cb;
      case CR:
        return cr;
      default:
        throw new IllegalArgumentException();
    }
  }

  @Override
  public void setComponent(Component component, double value) {
    switch (component) {
      case Y:
        this.y = value;
        break;
      case CB:
        this.cb = value;
        break;
      case CR:
        this.cr = value;
        break;
      default:
        throw new IllegalArgumentException();
    }
  }
}
