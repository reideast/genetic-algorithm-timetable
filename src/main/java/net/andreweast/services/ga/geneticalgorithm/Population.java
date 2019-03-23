package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Population implements Serializable {
    private static final int NUM_INDIVIDUALS = 20;
    private static final Random random = new Random();

    private GeneticAlgorithmJobData data;

    private Chromosome[] individuals;

    public Chromosome[] getIndividuals() {
        return individuals;
    }

    public Population(GeneticAlgorithmJobData masterData) {
        data = masterData;

        individuals = new Chromosome[NUM_INDIVIDUALS];
        if (data.isModifyExistingJob()) {
            makePopulationFromExisting(data);
        } else {
            makeNewPopulation(data);
        }
    }

    private void makePopulationFromExisting(GeneticAlgorithmJobData data) {
        // FUTURE: To modify an existing Schedule, make a population out of the existing data's ScheduledModules
        // FUTURE: For now, just overwrite everything by running this job as if it were new
        makeNewPopulation(data); // FUTURE
    }

    private void makeNewPopulation(GeneticAlgorithmJobData data) {
        for (int i = 0; i < NUM_INDIVIDUALS; ++i) {
            individuals[i] = new Chromosome(data);
        }
    }

    /**
     * Select a new population with the "roulette wheel" method
     * The fittest individuals will tend to be selected more often
     */
    public void select() {
        final Chromosome[] nextPopulation = new Chromosome[NUM_INDIVIDUALS];

        // Determine sum total of all individuals' fitness s.t. roulette wheel can select from them
        int totalFitness = 0;
        for (int i = 0; i < NUM_INDIVIDUALS; ++i) {
            totalFitness += individuals[i].getStoredFitness();
        }

        // Select a whole new population
        /*
           FUTURE: As suggested by (Cekała et al 2015), keeping the "Elite" members of the population
           can speed up convergence to a solution as much as twice as fast as just plain roulette-wheel selection.
           Keeping 1 elite member halved num generations to converge. 2-3 elites selected improved a good bit, and any more had diminishing returns.
           TODO: Keep population members ranked 1st, and maybe also 2nd, and 3rd
           TODO: Probably require: Arrays.sort(individuals);
         */
        for (int i = 0; i < NUM_INDIVIDUALS; ++i) {
            // "Spin the roulette wheel". Based on https://en.wikipedia.org/wiki/Fitness_proportionate_selection
            int randomSelected = random.nextInt(totalFitness);

            // For each individual, starting with first, if random number was less than its proportion, then roulette wheel "landed" on this item
            for (int individual = 0; individual < NUM_INDIVIDUALS; ++individual) {
                // Subtract this individual's fitness, so the next individual's fitness will be the closest to zero
                randomSelected -= individuals[individual].getStoredFitness();
                if (randomSelected < 0) {
                    nextPopulation[i] = new Chromosome(individuals[individual]);// deep clone individual
                    break;
                }

            }
        }

        // DEBUG: an assertion
        for (int i = 0; i < NUM_INDIVIDUALS; ++i) {
            if (nextPopulation[i] == null) {
                throw new RuntimeException("ERROR: individual #" + i + " was null!");
            }
        }

        // Increment population to the next generation's
        individuals = nextPopulation;
    }

    public void crossover() {
        // Crossover with p = 0.6
        if (random.nextInt(100) < 60) { // TODO: hardcoded 60% crossover. See (Cekała et all 2015) and/or my lit review for suggested %
            // TODO: Rather naive (and therefore probably inefficient) method of choosing best: Just sort the population by fitness
            // TODO: Is there support in the literature to crossing _random_ individuals
            Arrays.sort(individuals);
            Chromosome first = individuals[0];
            Chromosome second = individuals[1];

            // create a new one cloned from highest
            Chromosome offspring = new Chromosome(first);

            // swap some genes with second highest
            offspring.crossover(second);

            // replace lowest individual with new offspring
            individuals[individuals.length - 1] = offspring;
        }

    }

    public void mutate() {
        // Mutate a random individual with p = 0.9
        if (random.nextInt(100) < 90) { // TODO: hardcoded 90% mutate. See (Cekała et all 2015) and/or my lit review for suggested %
            individuals[random.nextInt(NUM_INDIVIDUALS)].mutate();
        }

    }

    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        for (Chromosome individual : individuals) {
            s.append(individual.toString()).append("\n");
        }
        return s.toString();
    }

    public List<Integer> toFitnessList() {
        List<Integer> fitnessValues = new ArrayList<>(individuals.length);
        for (Chromosome individual : individuals) {
            fitnessValues.add(individual.getStoredFitness());
        }
        return fitnessValues;
    }
}
