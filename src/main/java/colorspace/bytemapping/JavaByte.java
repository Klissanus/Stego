package colorspace.bytemapping;


/**
 * Created by Klissan on 16.12.2016.
 */
class JavaByte implements ByteMapping {

  @Override
  public int forward(byte b) {
    return b;
  }

  @Override
  public byte inverse(long i) {
    return (byte) ((i > 127) ? 0x7F : ((i < -128) ? 0x80 : i));
  }

  @Override
  public long normalize(double value, double min, double max) {
    double absMin = Math.abs(min);
    double signMin = Math.signum(min);
    if (max > 127 && value > 0) {
      return Math.round((value / max) * 127);
    }
    if (min < -128 && value < 0) {
      return -Math.round((value / min) * 128);
    }
    return Math.round(value);
  }

}
