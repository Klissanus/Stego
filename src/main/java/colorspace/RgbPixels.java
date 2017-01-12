package colorspace;

import java.awt.*;

/**
 * Created by Klissan on 02.12.2016.
 */
public class RgbPixels {

  private Rgb[] pixels;

  public Rgb[] getPixels() {
    return pixels;
  }

  public RgbPixels(int[] rgbPixels) {
    pixels = new Rgb[rgbPixels.length / 3];
    for (int i = 0; i < rgbPixels.length / 3; ++i) {
      pixels[i] = new Rgb(
          (byte) (0xFF & rgbPixels[3 * i + 0]),
          (byte) (0xFF & rgbPixels[3 * i + 1]),
          (byte) (0xFF & rgbPixels[3 * i + 2]));
    }
  }

  public RgbPixels(Rgb[] rgbPixels) {
    pixels = new Rgb[rgbPixels.length];
    for (int i = 0; i < rgbPixels.length; ++i) {
      pixels[i] = new Rgb(
          (byte) rgbPixels[i].getComponent(Component.RED),
          (byte) rgbPixels[i].getComponent(Component.GREEN),
          (byte) rgbPixels[i].getComponent(Component.BLUE));
    }
  }
/*
    byte[] getRedComponent(){
        byte[] result = new byte[pixels.length];
        for(int i=0; i < pixels.length; ++i){
            result[i]=pixels[i].r;
        }
        return result;
    }

    byte[] getGreenComponent(){
        byte[] result = new byte[pixels.length];
        for(int i=0; i < pixels.length; ++i){
            result[i]=pixels[i].g;
        }
        return result;
    }

    byte[] getBlueComponent(){
        byte[] result = new byte[pixels.length];
        for(int i=0; i < pixels.length; ++i){
            result[i]=pixels[i].b;
        }
        return result;
    }*/
}