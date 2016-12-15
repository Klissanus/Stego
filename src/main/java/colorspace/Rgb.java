package colorspace;

import java.awt.*;

/**
 * Created by Klissan on 02.12.2016.
 */
public class Rgb {
  private byte r;
  private byte g;
  private byte b;

  public Rgb(byte r, byte g, byte b) {
    this.r = r;
    this.g = g;
    this.b = b;
  }


  public byte getColor(Color color) {
    if (color == Color.RED) {
      return r;
    }
    if (color == Color.GREEN) {
      return g;
    }
    if (color == Color.BLUE) {
      return b;
    }

    return r;
  }

  public void setColor(Color color, byte byt) {
    if (color == Color.RED) {
      this.r = byt;
    }
    if (color == Color.GREEN) {
      this.g = byt;
    }
    if (color == Color.BLUE) {
      this.b = byt;
    }
  }
}

