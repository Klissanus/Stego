package colorspace;

/**
 * Created by Klissan on 02.12.2016.
 */
public class Rgb
    implements ColorSpace{
  private int r;
  private int g;
  private int b;

  public Rgb(int r, int g, int b) {
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
    long v = Math.round(value);
    int byt = (int) ((v > 255) ? 0xFF : (v < 0) ? 0x00 : v);
    switch(component){
      case RED:
        this.r = byt;
        break;
      case GREEN:
        this.g = byt;
        break;
      case BLUE:
        this.b = byt;
        break;
      default:
        throw new IllegalArgumentException();
    }
  }

}

