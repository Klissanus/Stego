package colorspace.bytemapping;

/**
 * Created by Klissan on 16.12.2016.
 */ //0-127 to 128-255;; -128 - -1 to 0 - 127
class Plus128 implements ByteMapping {

  @Override
  public int forward(byte b) {
    return b + 128;
  }

  @Override
  public byte inverse(long i) {
    return (byte) ((i > 255) ? 0x7F : ((i < 0) ? 0x80 : i - 128));
  }

  @Override
  public long normalize(double value, double min, double max) {
    double absMin = Math.abs(min);
    double signMin = Math.signum(min);
    return Math.round(((value + absMin) / (max + absMin)) * 255);
  }
}
