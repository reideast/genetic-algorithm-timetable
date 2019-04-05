package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    private List<Chromosome> individuals;

    public Population(GeneticAlgorithmJobData masterData, ExecutorService threadPool) {
        data = masterData;
        this.threadPool = threadPool;
        populationSize = data.getPopulationSize();

        individuals = Collections.synchronizedList(new ArrayList<>(populationSize + 600));
        if (data.isModifyExistingJob()) {
            makePopulationFromExisting(data);
        } else {
            makeNewPopulation(data);
        }
    }

    /**
     * To modify an existing Schedule, make a population out of the existing data's ScheduledModules
     *
     * Fill the population up with 1/2 of each:
     *   1. Clones of the existing data
     *   2. Mutations of that data
     */
    private void makePopulationFromExisting(GeneticAlgorithmJobData data) {
        if (data.getScheduledModules() == null || data.getScheduledModules().size() == 0) {
            System.out.println("ERROR For preexisting schedule (id=" + data.getScheduleId() + "), there were no scheduled modules in the database"); // FUTURE: Logger error
            // FUTURE: To prevent jobs from not going forward until the GUI has more features, simply ignore this error
            // FUTURE: Also, see {@link DbToGaDeserializer}
            data.setModifyExistingJob(false); // FUTURE: Hack until future work can be done
            makeNewPopulation(data); // FUTURE: For now, just overwrite everything by running this job as if it were new
        }

        // This constructor will make a new individual with the data provided
        Chromosome chromosomeFromDatabase = new Chromosome(data, data.getScheduledModules());
        individuals.add(chromosomeFromDatabase);

//        int oneHalf = populationSize / 2;
        int oneHalf = populationSize; // DEBUG: Making WHOLE population clones, rather than making a bunch of mutations. This seems like it will let the algorithm work more as expected?

// DEBUG: Making WHOLE population clones, rather than making a bunch of mutations. This seems like it will let the algorithm work more as expected?
//        // Use a thread pool for mutation since it will be recalculating fitness
//        List<Future<Chromosome>> chromosomeCreators = new ArrayList<>(oneHalf);
//        for (int i = 0; i < oneHalf; ++i) {
//            chromosomeCreators.add(threadPool.submit(() -> chromosomeFromDatabase.mutate(data.getMutatedGenesMax())));
//        }

        // Clone the new individual. No thread used since no fitness is calculated upon clone
        // Might as well do it while the mutate threads are working, though
        for (int i = 0; i < oneHalf - 1; ++i) {
            individuals.add(new Chromosome(chromosomeFromDatabase));
        }

 // DEBUG: Making WHOLE population clones, rather than making a bunch of mutations. This seems like it will let the algorithm work more as expected?
//        // Block until all mutating is done
//        try {
//            for (int i = 0; i < oneHalf; ++i) {
//                individuals.add((chromosomeCreators.get(i)).get()); // Block for this thread to return its Future value
//            }
//        } catch (InterruptedException | ExecutionException e) {
//            // TODO: There's no real exception handling here. This should kill the Genetic Algorithm Job and put it in a failed state!
//            System.out.println("ERROR: While creating a new population and calculating its fitness, a threading error was thrown"); // FUTURE: Logger error
//            e.printStackTrace();
//        }
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
                individuals.add((chromosomeCreators.get(i)).get()); // Block for this thread to return its Future value
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
        final List<Chromosome> nextPopulation = Collections.synchronizedList(new ArrayList<>(populationSize));
//        final List<Chromosome> nextPopulation = new ArrayList<>(populationSize + 600);

        // Elite survivors: Keep population members ranked 1st, and maybe also 2nd, and 3rd
        Collections.sort(individuals);
        for (int i = 0; i < numEliteSurvivors; ++i) {
            // Get the best: the 0th, 1st, ... individuals
            nextPopulation.add(new Chromosome(individuals.get(i)));// deep clone individual
        }

        // Determine sum total of all individuals' fitness s.t. roulette wheel can select from them
        long totalFitness = 0;
        for (Chromosome individual : individuals) {
            totalFitness += individual.getCachedFitness();
        }

        // Select a whole new population, limited to the expected population size
        for (int i = 0; i < populationSize - numEliteSurvivors; ++i) {
            // "Spin the roulette wheel". Based on https://en.wikipedia.org/wiki/Fitness_proportionate_selection
            long randomSelected = (long) (random.nextDouble() * totalFitness);

            // For each individual, starting with first, if random number was less than its proportion, then roulette wheel "landed" on this item
            for (Chromosome individual : individuals) {
                // Subtract this individual's fitness, so the next individual's fitness will be the closest to zero
                randomSelected -= individual.getCachedFitness();
                if (randomSelected < 0) {
                    nextPopulation.add(new Chromosome(individual));// deep clone individual
                    break;
                }
            }
        }

        // Increment population to the next generation's
        individuals = nextPopulation;
    }

    /**
     * Possibly do genetic crossover (e.g. sexual reproduction) within the population
     *
     * @param crossoverRate [0.0f, 1.0f] Do crossover with p = crossoverRate . Higher is more often
     */
    public void crossover(float crossoverRate) {
        try {
            // todo: initial size??
            List<Future<Chromosome>> crossedOverChromosomesFutures = new ArrayList<>(populationSize * populationSize);

            for (int i = 0; i < individuals.size(); ++i) {
                for (int j = 0; j < individuals.size(); ++j) {
                    if (i != j) {
                        if (random.nextFloat() < crossoverRate) {
                            final int firstIndex = i, secondIndex = j;
                            crossedOverChromosomesFutures.add(threadPool.submit(() -> {
                                Chromosome first = individuals.get(firstIndex);
                                Chromosome second = individuals.get(secondIndex);
                                Chromosome offspring = new Chromosome(first);
                                final float whichCrossoverMethod = random.nextFloat();
                                if (whichCrossoverMethod < 0.3333f) {
                                    offspring.crossoverBinary(second);
                                } else if (whichCrossoverMethod < 0.6666f) {
                                    offspring.crossoverPiece(second);
                                } else {
                                    offspring.crossoverTwoPieces(second);
                                }
                                return offspring; // save the offspring into the population, where it may be selected to be in the next generation soon
                            }));
                        }
                    }
                }
            }
            for (Future<Chromosome> waiter : crossedOverChromosomesFutures) {
                individuals.add(waiter.get());
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Try to mutate ALL individuals with each one being mutated with p = mutateRate
     *
     * @param mutateRate [0.0f, 1.0f] Probability of mutation for EVERY chromosome. Higher is more often
     */
    public void mutate(float mutateRate, int mutatedGenesMax) {
        final int currentPopulationSize = individuals.size();
        for (int i = 0; i < currentPopulationSize; ++i) {
            if (random.nextFloat() < mutateRate) {
                individuals.add(individuals.get(i).mutate(mutatedGenesMax));
            }
        }
    }

    public Boolean hasValidSolution() {
        // Determine if any of the chromosomes represents a valid solution
        for (Chromosome individual : individuals) {
            if (individual.isValidSolution()) {
                return true;
            }
        }
        return false;
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
        List<Integer> fitnessValues = new ArrayList<>(individuals.size());
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
        Collections.sort(individuals);
        System.out.println("Getting best gene out of chromosome: " + this.toFitnessList()); // FUTURE: Logger

        // Go through list and return the FIRST valid one, which will be best since list is sorted DESC
        for (Chromosome individual : individuals) {
            if (individual.isValidSolution()) {
                return individual;
            }
        }
        // None were valid, so just return the best of the bunch
        return individuals.get(0);
    }

    /**
     * Grab a fitness value from the population, just so the GUI can display an estimated value
     * @return Fitness from the first chromosome in the list
     */
    public long getEstimatedFitness() {
        return individuals.get(0).getCachedFitness();
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
