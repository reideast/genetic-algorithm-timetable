package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;
import java.util.Random;

public class Chromosome implements Comparable<Chromosome>, Serializable {
    private static Random random = new Random();

    private static boolean havePrintedCrossoverDebug = false; // DEBUG
    private static boolean havePrintedMutateDebug = false; // DEBUG

    private ScheduledModule[] genes;

    private GeneticAlgorithmJobData data;

    private int cachedFitness;

    // TODO: Do something with this. Maybe stop job early?
    private boolean isValidSolution;

    /**
     * Randomising constructor
     */
    public Chromosome(GeneticAlgorithmJobData masterData) {
        data = masterData;

        genes = new ScheduledModule[data.getChromosomeSize()];
        for (int i = 0; i < data.getChromosomeSize(); ++i) {
            genes[i] = new ScheduledModule(data.getIndexedModule(i), data);
        }

        cachedFitness = calculateFitness(); // Also sets isValidSolution
    }

    /**
     * Cloning constructor
     */
    public Chromosome(Chromosome toClone) {
        data = toClone.data;

        genes = new ScheduledModule[data.getChromosomeSize()];
        for (int i = 0; i < data.getChromosomeSize(); ++i) {
            genes[i] = toClone.genes[i].clone();
        }

        cachedFitness = toClone.getCachedFitness();
        isValidSolution = toClone.isValidSolution();
    }

    public void crossover(Chromosome toCrossWith) {
        final int crossoverPoint = random.nextInt(genes.length);
        if (!havePrintedCrossoverDebug && GeneticAlgorithmJob.DEBUG) { // DEBUG
            System.out.println("Before cross: " + this.toString());
            System.out.println("Crossing w/:  " + toCrossWith.toString());
            System.out.println("CrossoverPoint=" + crossoverPoint);
        }

        for (int i = 0; i <= crossoverPoint; ++i) {
            genes[i] = toCrossWith.genes[i].clone();
        }
        cachedFitness = calculateFitness();
        if (!havePrintedCrossoverDebug && GeneticAlgorithmJob.DEBUG) { // DEBUG
            System.out.println("After cross:  " + this.toString());

            havePrintedCrossoverDebug = true;
        }
    }

    public void mutate() {
        // Randomise one of the scheduled modules
        if (!havePrintedMutateDebug && GeneticAlgorithmJob.DEBUG) {
            System.out.println("Before mutate: " + this.toString());
        }

        final int mutateGene = random.nextInt(genes.length);
        genes[mutateGene] = new ScheduledModule(genes[mutateGene].getModule(), data);
        cachedFitness = calculateFitness();

        if (!havePrintedMutateDebug && GeneticAlgorithmJob.DEBUG) {
            System.out.println("@" + mutateGene + " After mutate:  " + this.toString());

            havePrintedMutateDebug = true;
        }
    }

    private int calculateFitness() {
        // Weight of any "required" fitness is 100, such as two modules overlapping
        // Weight of any "preferable" fitness is 1, such as keeping a 2-hour lecture as one block vs. two blocks or lecture not being at 8am
        // TODO: Tweak these fitness weights

        // TODO: act differently based on masterData.isModifyExistingJob

        isValidSolution = true;

        // Calculate minimal fitness of any chromosome which has all HARD constraints met
        final int ONE_HARD_CONSTRAINT = 100;
        // TODO: soft constraints should be able to add up to just below that
        /*
          If there are 5 modules, and each violated hard constraint takes away 100 = ONE_HARD_CONSTRAINT fitness,
          then if no modules overlap, then Fitness would be 500.

          Almost all hard constraints met
          If just one hard constraint isn't met, then hard-fitness is 400.
          Let's say its soft constraints add up to soft-fitness 450. Then Fitness would be 950
          That's too much. A valid solution with really bad soft-fitness could be Fitness 501: much, much less, likely to be culled
          Let's say its soft constraints add up to soft-fitness 90. Then fitness would be 490
          A valid solution with good soft constraints could be 590. One with bad soft constraints could be 510.
          Seems good!

          Moderate amount of hard constraints met
          A solution with 3 violated hard constraints would be 200. Let's say it has really good soft: Fitness 290
          A solution with 2 violated hard constraints would be 300. Really bad soft: 310
          Very close! Either would have about the same probability of being selected for

          Most hard constraints violated vs. moderate
          2 violated hard, really bad soft: 310
          4 violated hard, really good soft: 190. Really bad soft: 110
          Either one has a good bit smaller probability of being selected for

          OK, so conclusion: Total of all POSSIBLE soft constraints should sum to 99 (99 = ONE_HARD_CONSTRAINT minus one)
         */

        // Start with the max possible hard-fitness value; subtract has violations are found
        int fitnessFromOverlappingClasses = data.getChromosomeSize() * ONE_HARD_CONSTRAINT;

        // TODO: O(n^2) iterative search. Needs improvement. Ideas: Hash array, make sure no collisions. "Sort" array and then compare linearly.
        for (int i = 0; i < genes.length; ++i) {
            for (int j = i + 1; j < genes.length; ++j) {
                if (i != j) {
                    if (genes[i].conflict(genes[j])) {
                        fitnessFromOverlappingClasses -= ONE_HARD_CONSTRAINT;
                        isValidSolution = false;
                    }
                }
            }
        }

        return fitnessFromOverlappingClasses;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(cachedFitness).append(": ");
        for (ScheduledModule course : genes) {
            s.append(course.toString()).append(", ");
        }

        return s.toString();
    }

    public int compareTo(Chromosome o) {
        return o.getCachedFitness() - cachedFitness;
    }

    public int getCachedFitness() {
        return cachedFitness;
    }

    public boolean isValidSolution() {
        return isValidSolution;
    }
}
