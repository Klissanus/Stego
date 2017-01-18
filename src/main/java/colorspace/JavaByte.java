package colorspace;

/**
 * Created by Klissan on 12.01.2017.
 */
public class JavaByte
    implements ColorSpace
{
  private byte r;
  private byte g;
  private byte b;

  public JavaByte(byte r, byte g, byte b) {
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
    byte byt = (byte) ((v > 127) ? 0x7F : ((v < -128) ? 0x80 : v));
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
