package metrics;

import colorspace.Component;
import colorspace.Rgb;

/**
 * Created by Klissan on 27.06.2017.
 */
public class Metrics {
    final static int maxpeak = 255*255;

    public static double mse(Rgb[] source, Rgb[] mode){
        double sum = 0;
        Component[] comp = {Component.RED, Component.GREEN, Component.BLUE};
        for (int i = 0; i < source.length; i++) {
            for(Component c : comp) {
                double r = (source[i].getComponent(c) - mode[i].getComponent(c));
                sum += r * r;
            }
        }
        return sum / ( 3 * source.length);
    }

    public static double psnr(Rgb[] source, Rgb[] mode){
        return 10* Math.log10(maxpeak / Metrics.mse(source, mode));
    }

    public static double nc(String sourceMessage, String resMessage){
        long chisl = 0;
        long znam = 0;
        for (int i = 0; i < sourceMessage.length(); i++) {
            int r = 0;
            if(resMessage.charAt(i) > 255){
                r = 65536 - resMessage.charAt(i);
            }else{
                r = resMessage.charAt(i);
            }
            chisl += sourceMessage.charAt(i) * r;
            znam += sourceMessage.charAt(i) * sourceMessage.charAt(i);
        }
        return (double) chisl / znam;
    }

    public static double nc(byte[] sourceMessage, byte[] resMessage){
        long chisl = 0;
        long znam = 0;
        for (int i = 0; i < resMessage.length; i++) {
            int si = i % sourceMessage.length;
            chisl += sourceMessage[si] * resMessage[i];
            znam += sourceMessage[si] * sourceMessage[si];
        }
        return (double) chisl / znam;
    }

}
