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

  public YCbCr(Rgb pixel) {
    byte r = (byte) pixel.getComponent(Component.RED);
    byte g = (byte) pixel.getComponent(Component.GREEN);
    byte b = (byte) pixel.getComponent(Component.BLUE);
    this.y = 0 + 0.299000 * r + 0.587000 * g + 0.114000 * b;
    this.cb = 128 - 0.168736 * r - 0.331264 * g + 0.500000 * b;
    this.cr = 128 + 0.500000 * r - 0.418688 * g - 0.081312 * b;
  }

  public Rgb getRgb() {
    byte r = (byte) (y + 1.402000 * (cr - 128));
    byte g = (byte) (y - 0.344136 * (cb - 128) - 0.714136 * (cr - 128));
    byte b = (byte) (y + 1.772000 * (cb - 128));
    return new Rgb(r, g, b);
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
      case CB:
        this.cb = value;
      case CR:
        this.cr = value;
      default:
        throw new IllegalArgumentException();
    }
  }
}
