package genetic;

/**
 * Created by Klissan on 25.06.2017.
 */
public class Population {
    private Gen[] population;

    public Population(int size){
        population = new Gen[size];
        for(Gen gen : population){
            gen = new Gen((int) (Math.random()*62 + 1));

        }
    }

    public int getSize(){
        return population.length;
    }

    public Gen getGen(int i){
        return population[i];
    }

    public boolean checkUnique(){
        for (int i = 0; i < population.length; i++) {
            for (int j = i; j < population.length; j++) {
                if(population[i].getNumber() == population[j].getNumber()){
                    return false;
                }
            }
        }
        return true;
    }
}
