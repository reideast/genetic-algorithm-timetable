package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;
import java.util.Random;

public class Chromosome implements Comparable<Chromosome>, Serializable {
    private static Random random = new Random();

    private static boolean havePrintedCrossoverDebug = false; // DEBUG
    private static boolean havePrintedMutateDebug = false; // DEBUG

    private ScheduledModule[] genes;

    private GeneticAlgorithmJobData data;

    private int storedFitness;

    // TODO: Do something with this. Maybe stop job early?
    private boolean isValidSolution;

    public ScheduledModule[] getGenes() {
        return genes;
    }

    /**
     * Randomising constructor
     */
    public Chromosome(GeneticAlgorithmJobData masterData) {
        data = masterData;

        genes = new ScheduledModule[data.getChromosomeSize()];
        for (int i = 0; i < data.getChromosomeSize(); ++i) {
            genes[i] = new ScheduledModule(data.getIndexedModule(i), data);
        }

        storedFitness = calculateFitness(); // Also sets isValidSolution
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

        storedFitness = toClone.getStoredFitness();
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
        storedFitness = calculateFitness();
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
        storedFitness = calculateFitness();

        if (!havePrintedMutateDebug && GeneticAlgorithmJob.DEBUG) {
            System.out.println("@" + mutateGene + " After mutate:  " + this.toString());

            havePrintedMutateDebug = true;
        }
    }

    private int calculateFitness() {
        // Weight of any "required" fitness is 100, such as two modules overlapping
        // Weight of any "preferable" fitness is 1, such as keeping a 2-hour lecture as one block vs. two blocks or lecture not being at 8am
        // TODO: Tweak these fitness weights

        isValidSolution = true;

        int fitnessFromOverlappingClasses = data.getChromosomeSize() * 100;
        // TODO: O(n^2) iterative search. Needs improvement. Ideas: Hash array, make sure no collisions. "Sort" array and then compare linearly.
        for (int i = 0; i < genes.length; ++i) {
            for (int j = i + 1; j < genes.length; ++j) {
                if (i != j) {
                    if (genes[i].conflict(genes[j])) {
                        fitnessFromOverlappingClasses -= 100;
                        isValidSolution = false;
                    }

                }

            }

        }
        return fitnessFromOverlappingClasses;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(storedFitness).append(": ");
        for (ScheduledModule course : genes) {
            s.append(course.toString()).append(", ");
        }

        return s.toString();
    }

    public int compareTo(Chromosome o) {
        return o.getStoredFitness() - storedFitness;
    }

    public int getStoredFitness() {
        return storedFitness;
    }

    public boolean isValidSolution() {
        return isValidSolution;
    }
}
