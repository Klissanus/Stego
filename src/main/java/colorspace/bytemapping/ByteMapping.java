package colorspace.bytemapping;

/**
 * Created by Klissan on 16.12.2016.
 */
public interface ByteMapping {
  int forward(byte b);

  byte inverse(long i);

  long normalize(double value, double min, double max);
}
