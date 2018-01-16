package genetic;

/**
 * Created by Klissan on 25.06.2017.
 */
public class Gen {
    private boolean[] gen = new boolean[6];
    private int number;

    public Gen(int num){
        number = num;
        String str = Integer.toBinaryString(num);
        int len = str.length();
        for (int i = 5; i >= len ; i--) {
            gen[i] = false;
        }
        for (int i = 0; i < len ; i++) {
            gen[i] = str.charAt(i) == '1';
        }
    }

    private void toNumber(){
        int num = 0;
        int m = 1;
        for (int i = 0; i < 6 ; i++) {
            if (gen[i]){
                num += m;
            }
            m*=2;
        }
        number = num;
    }

    public int getNumber(){
        return number;
    }

    public boolean[] getGen(){
        return gen;
    }
}
