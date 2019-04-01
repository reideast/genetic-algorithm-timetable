package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Population implements Serializable {
    private static final Random random = new Random();

    private final ExecutorService threadPool;

    private GeneticAlgorithmJobData data;

    private final int populationSize;

    private Chromosome[] individuals;

    public Population(GeneticAlgorithmJobData masterData, ExecutorService threadPool) {
        data = masterData;
        this.threadPool = threadPool;
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
        // Using the thread pool, start a series of jobs ot make a new chromosome.
        // Threads are justified since the new chromosomes will be calculating fitness
        List<Future<Chromosome>> chromosomeCreators = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; ++i) {
            chromosomeCreators.add(threadPool.submit(() -> new Chromosome(data)));
        }

        // Block until all threads are done
        try {
            for (int i = 0; i < populationSize; ++i) {
                individuals[i] = (chromosomeCreators.get(i)).get(); // Block for this thread
            }
        } catch (InterruptedException | ExecutionException e) {
            // TODO: There's no real exception handling here. This should kill the Genetic Algorithm Job and put it in a failed state!
            System.out.println("ERROR: While creating a new population and calculating its fitness, a threading error was thrown"); // FUTURE: Logger error
            e.printStackTrace();
        }
    }

    /**
     * Select a new population with the "roulette wheel" method
     * The fittest individuals will tend to be selected more often
     *
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
        for (int i = 0; i < populationSize; ++i) {
            totalFitness += individuals[i].getCachedFitness();
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

        // Increment population to the next generation's
        individuals = nextPopulation;
    }

    // TODO: Fix crossover percent: right now it's going float -> int -> float
    /**
     * Possibly do genetic crossover (e.g. sexual reproduction) within the population
     *
     * @param crossoverRate int range [0,100], how often to do crossover. Higher is more often
     */
    public void crossover(float crossoverRate) {
        if (random.nextInt(100) < (crossoverRate * 100)) { // TODO: hardcoded 60% crossover. See (Cekała et all 2015) and/or my lit review for suggested %
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
            // DEBUG: REPLACING...THIS IS NOT SUPPORTED BY LITERATURE
            individuals[individuals.length - 1] = offspring;
        }
    }

    // TODO: Fix mutate percent: right now it's going float -> int -> float
    /**
     * Possibly mutate (e.g. random perturb a gene) within the population
     *
     * @param mutateRate int range [0,100], how often to mutate. Higher is more often
     */
    public void mutate(float mutateRate) {
        if (random.nextInt(100) < (mutateRate * 100)) { // TODO: hardcoded 90% mutate. See (Cekała et all 2015) and/or my lit review for suggested %
            final int numToMutate = random.nextInt(10) + 1;
            for (int i = 0; i < numToMutate; ++i) {
                individuals[random.nextInt(populationSize)].mutate();
            }
        }
    }

    public Boolean hasValidSolution() {
        // Determine if any of the chromosomes represents a valid solution
        boolean hasValidSolution = false;
        for (int i = 0; i < populationSize; ++i) {
            if (individuals[i].isValidSolution()) {
                hasValidSolution = true;
            }
        }
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

    private Chromosome getBestChromosome() {
        Arrays.sort(individuals);
        System.out.println("Getting best gene out of chromosome: " + this.toFitnessList()); // FUTURE: Logger

        // Go through list and return the FIRST valid one, which will be best since list is sorted DESC
        for (Chromosome individual : individuals) {
            if (individual.isValidSolution()) {
                return individual;
            }
        }
        // None were valid, so just return the best of the bunch
        return individuals[0];
    }

    /**
     * Used after the algorithm runs to get the best chromosome for saving to the database
     * If there is at least one valid solution (i.e. has no violated hard constraints), this will return the BEST VALID individual
     * If there isn't a valid solution, this will return the BEST individual
     *
     * @return The best individual in the population, as a List of ScheduledModules
     */
    public List<ScheduledModule> getBestChromosomeScheduledModule() {
        return Arrays.asList(getBestChromosome().getGenes());
    }

    /**
     * A debug method to help track down which modules don't have venues that they could POSSIBLY fit into
     */
    public void logFailuresToSchedule() {
        Chromosome best = this.getBestChromosome();
        if (best.isValidSolution()) {
            System.out.println("No conflicts in the best solution");
        } else {
            System.out.println("******************** There were conflicts in the best solution! ********************");
            best.logFailuresToSchedule();
            System.out.println("************************************************************************************");
        }
    }
}
