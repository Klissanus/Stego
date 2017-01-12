package colorspace;


/**
 * Created by Klissan on 12.01.2017.
 */
public interface ColorSpace {
  double getComponent(Component component);
  void setComponent(Component component, double value);
}
