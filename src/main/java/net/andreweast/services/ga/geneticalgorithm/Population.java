package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Population implements Serializable {
    private static final Random random = new Random();

    private GeneticAlgorithmJobData data;
    private final int populationSize;

    private Chromosome[] individuals;
    private Boolean hasValidSolution = null;

    public Population(GeneticAlgorithmJobData masterData) {
        data = masterData;
        populationSize = data.getPopulationSize();

        individuals = new Chromosome[populationSize];
        if (data.isModifyExistingJob()) {
            makePopulationFromExisting(data);
        } else {
            makeNewPopulation(data);
        }
    }

    private void makePopulationFromExisting(GeneticAlgorithmJobData data) {
        // FUTURE: To modify an existing Schedule, make a population out of the existing data's ScheduledModules
        makeNewPopulation(data); // FUTURE: For now, just overwrite everything by running this job as if it were new
    }

    private void makeNewPopulation(GeneticAlgorithmJobData data) {
        for (int i = 0; i < populationSize; ++i) {
            individuals[i] = new Chromosome(data);
        }
    }

    /**
     * Select a new population with the "roulette wheel" method
     * The fittest individuals will tend to be selected more often
     * @param numEliteSurvivors How many of the very best individuals should be guaranteed to be represented at least once in the next generation
     *                          As suggested by (Cekała et al 2015), keeping the "Elite" members of the population
     *                          can speed up convergence to a solution as much as twice as fast as just plain roulette-wheel selection.
     *                          Keeping 1 elite member halved num generations to converge. 2-3 elites selected improved a good bit, and any more had diminishing returns.
     */
    public void select(int numEliteSurvivors) {
        final Chromosome[] nextPopulation = new Chromosome[populationSize];

        // TODO: numEliteSurvivors:
        // TODO: Keep population members ranked 1st, and maybe also 2nd, and 3rd
        // TODO: Probably requires: Arrays.sort(individuals);

        // Determine sum total of all individuals' fitness s.t. roulette wheel can select from them
        int totalFitness = 0;
        hasValidSolution = false;
        for (int i = 0; i < populationSize; ++i) {
            totalFitness += individuals[i].getCachedFitness();
            if (individuals[i].isValidSolution()) {
                hasValidSolution = true;
            }
        }

        // Select a whole new population
        for (int i = 0; i < populationSize; ++i) {
            // "Spin the roulette wheel". Based on https://en.wikipedia.org/wiki/Fitness_proportionate_selection
            int randomSelected = random.nextInt(totalFitness);

            // For each individual, starting with first, if random number was less than its proportion, then roulette wheel "landed" on this item
            for (int individual = 0; individual < populationSize; ++individual) {
                // Subtract this individual's fitness, so the next individual's fitness will be the closest to zero
                randomSelected -= individuals[individual].getCachedFitness();
                if (randomSelected < 0) {
                    nextPopulation[i] = new Chromosome(individuals[individual]);// deep clone individual
                    break;
                }

            }
        }

        // DEBUG: an assertion
        for (int i = 0; i < populationSize; ++i) {
            if (nextPopulation[i] == null) {
                throw new RuntimeException("ERROR: individual #" + i + " was null!");
            }
        }

        // Increment population to the next generation's
        individuals = nextPopulation;
    }

    /**
     * Possibly do genetic crossover (e.g. sexual reproduction) within the population
     *
     * @param crossoverRate int range [0,100], how often to do crossover. Higher is more often
     */
    public void crossover(int crossoverRate) {
        if (random.nextInt(100) < crossoverRate) { // TODO: hardcoded 60% crossover. See (Cekała et all 2015) and/or my lit review for suggested %
            // TODO: Rather naive (and therefore probably inefficient) method of choosing best: Just sort the population by fitness

            // TODO: Is there support in the literature to crossing _random_ individuals rather than the best
            Arrays.sort(individuals);
            Chromosome first = individuals[0];
            Chromosome second = individuals[1];

            // create a new one cloned from highest
            Chromosome offspring = new Chromosome(first);

            // swap some genes with second highest
            offspring.crossover(second);

            // replace lowest individual with new offspring
            individuals[individuals.length - 1] = offspring;

            // Since population has changed, invalidate cached hasValidSolution
            hasValidSolution = null;
        }
    }

    /**
     * Possibly mutate (e.g. random perturb a gene) within the population
     *
     * @param mutateRate int range [0,100], how often to mutate. Higher is more often
     */
    public void mutate(int mutateRate) {
        if (random.nextInt(100) < mutateRate) { // TODO: hardcoded 90% mutate. See (Cekała et all 2015) and/or my lit review for suggested %
            individuals[random.nextInt(populationSize)].mutate();

            // Since population has changed, invalidate cached hasValidSolution
            hasValidSolution = null;
        }

    }

    public Boolean hasValidSolution() {
        return hasValidSolution;
    }

    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        s.append("Entire Population\n");
        for (Chromosome individual : individuals) {
            s.append(individual.toString()).append("\n");
        }
        return s.toString();
    }

    public List<Integer> toFitnessList() {
        List<Integer> fitnessValues = new ArrayList<>(individuals.length);
        for (Chromosome individual : individuals) {
            fitnessValues.add(individual.getCachedFitness());
        }
        return fitnessValues;
    }

    /**
     * Used after the algorithm runs to get the best chromosome for saving to the database
     * If there is at least one valid solution (i.e. has no violated hard constraints), this will return the BEST VALID individual
     * If there isn't a valid solution, this will return the BEST individual
     *
     * @return The best individual in the population
     */
    public Chromosome getBestChromosome() {
//        Arrays.sort(individuals);
        throw new UnsupportedOperationException(); // DEBUG TODO
    }
}
