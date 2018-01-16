package genetic;

import colorspace.Rgb;
import metrics.Metrics;

/**
 * Created by Klissan on 27.06.2017.
 */
public class Genetic {

    static final int popSize = 20;
    int[][] population = new int[popSize][2];
    double[] fits = new double[popSize];
    int weight = 100;
    double limit = 0.05;

    public int getSize(){
        return popSize;
    }

    public int[] get(int i){
        return population[i];
    }

    public int[] getBestCoeffs(){
        double bestfit = 0;
        int bestind = 0;
        for (int i=0; i<popSize; i++){
            if(fits[i] > bestfit){
                bestfit = fits[i];
                bestind = i;
            }
        }
        return population[bestind];
    }

    public Genetic(boolean random){
        if (random){
            for (int i = 0; i < popSize; i++) {
                for (int j = 0; j < 2; j++) {
                    population[i][j] = (int) (Math.random()*63);
                }
            }
        }else{
            int k = 1;
            for (int i = 0; i < popSize; i++) {
                for (int j = 0; j < 2; j++) {
                    population[i][j] = k++;
                }
            }
        }
    }

    public void fitnessFunction(Rgb[] source, Rgb[] res, byte[] sourceData, byte[] readData, int chromInd){
        double psnr = Metrics.psnr(source, res);
        double nc = Metrics.nc(sourceData, readData);
        System.out.println("PSNR = " + psnr);
        System.out.println("NC = " + nc);
        fits[chromInd] =  psnr + weight * nc;
        System.out.println("Fit value = " + fits[chromInd]);
    }



    public void selection(){
        double[] parray = new double[popSize + 2];
        parray[0] = 0;
        parray[popSize + 1] = 100;
        double p = 0; // init probabilities
        for (int i = 0; i < popSize ; i++) {
            p += fits[i];
            parray[i + 1] = p;
        }
        //selection
        int[][] parents  = new int[popSize][];
        for (int i = 0; i < popSize ; i++) {
            double r = Math.random() * p;
            int k = 0;
            while (r > parray[k]) k++;
            parents[i] = population[k - 1];
        }
        population = parents;
    }

    static int[] gens = {1, 2, 4, 8, 16, 32};
    public void mutation(){
        for (int i = 0; i < popSize; i++) {
            if(Math.random() < limit){
                int ind = (int) (Math.random() * 2);
                population[i][ind] ^= gens[(int) (Math.random() * gens.length)];
            }
        }
    }

    public void forceMutation(){
        for (int i = 0; i < popSize ; i++) {
            if(population[i][0] == population[i][1]){
                if(population[i][1] > 1){
                    population[i][1] -= 1;
                }else{
                    population[i][1] += 1;
                }
            }
        }
    }

    public void crossover(){
        for (int i = 0; i < popSize / 2; i++) {
            int tmp = population[2*i][0];
            population[2*i][0] = population[2*i + 1][0];
            population[2*i + 1][0] = tmp;
        }
    }
}
