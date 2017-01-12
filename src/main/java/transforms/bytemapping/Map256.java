package transforms.bytemapping;

/**
 * Created by Klissan on 16.12.2016.
 */ //0-127 to 0-127;; -128 - -1 to 128 - 255
public class Map256 implements ByteMapping {

  @Override
  public int forward(byte b) {
    return (b >= 0) ? (b) : (256 - Math.abs(b));
  }

  @Override
  public byte inverse(long i) {
    byte b;
    if (i > 255) {
      b = (byte) 0x80;
    }
    if (i < 0) {
      b = (byte) 0x00;
    } else {
      b = (byte) ((i < 128) ? (i) : (i - 256));
    }
    return b;
  }

  @Override
  public long normalize(double value, double min, double max) {
    double absMin = Math.abs(min);
    double signMin = Math.signum(min);
    //return Math.round((value - signMin * absMin) / (max - signMin * absMin) * 255);
    return Math.round(((value + absMin) / (max + absMin)) * 255);
  }
}
