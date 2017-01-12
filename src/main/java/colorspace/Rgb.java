package colorspace;

import java.awt.*;

/**
 * Created by Klissan on 02.12.2016.
 */
public class Rgb
    implements ColorSpace{
  private byte r;
  private byte g;
  private byte b;

  public Rgb(byte r, byte g, byte b) {
    this.r = r;
    this.g = g;
    this.b = b;
  }

  @Override
  public double getComponent(Component component) {
    switch(component){
      case RED:
        return r;
      case GREEN:
        return g;
      case BLUE:
        return b;
      default:
        throw new IllegalArgumentException();
    }
  }

  @Override
  public void setComponent(Component component, double value) {
    byte byt = (byte) value;
    switch(component){
      case RED:
        this.r = byt;
      case GREEN:
        this.g = byt;
      case BLUE:
        this.b = byt;
      default:
        throw new IllegalArgumentException();
    }
  }

}

