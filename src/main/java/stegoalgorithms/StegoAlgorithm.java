package stegoalgorithms;

import colorspace.Component;
import utils.PixelsMatrix;

import java.util.Set;

/**
 * Created by Klissan on 19.05.2017.
 */
public interface StegoAlgorithm {

    void hide(PixelsMatrix where, Set<Component> usedColors, byte[] msg, byte[] key);

    byte[] read(PixelsMatrix from, Set<Component> usedColors, byte[] key);
}
