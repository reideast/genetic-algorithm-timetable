package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;
import java.util.Random;

public class Chromosome implements Comparable<Chromosome>, Serializable {
    private static Random random = new Random();

    private ScheduledModule[] courses;
    private int storedFitness;
    private boolean isValidSolution;

    public ScheduledModule[] getCourses() {
        return courses;
    }

    public Chromosome() {
        courses = new ScheduledModule[Module.getAllModulesSize()];
        for (int i = 0; i < Module.getAllModulesSize(); ++i) {
            courses[i] = new ScheduledModule(Module.getFromAllModulesByIndex(i));
        }

        storedFitness = calculateFitness(); // Also sets isValidSolution
    }

    public Chromosome(Chromosome toClone) {
        courses = new ScheduledModule[Module.getAllModulesSize()];
        for (int i = 0; i < Module.getAllModulesSize(); ++i) {
            courses[i] = toClone.courses[i].clone();
//            courses[i] = new ScheduledModule(
//                    toClone.courses[i].getModule(),
//                    toClone.courses[i].getVenue(),
//                    toClone.courses[i].getTimeSlot()
//            );
        }

        storedFitness = toClone.getStoredFitness();
        isValidSolution = toClone.isValidSolution();
    }

    public void crossover(Chromosome toCrossWith) {
        final int crossoverPoint = random.nextInt(courses.length);
//        if (Scheduler.DEBUG) {
//            System.out.println("Before cross: " + this.toString());
//            System.out.println("Crossing w/:  " + toCrossWith.toString());
//            System.out.println("CrossoverPoint=" + crossoverPoint);
//        }

        for (int i = 0; i <= crossoverPoint; ++i) {
            courses[i] = toCrossWith.courses[i].clone();
        }
        storedFitness = calculateFitness();
//        if (Scheduler.DEBUG) {
//            System.out.println("After cross:  " + this.toString());
//        }
    }

    public void mutate() {
        // randomise one of the scheduled courses
//        if (Scheduler.DEBUG) {
//            System.out.println("Before mutate: " + this.toString());
//        }

        final int mutateGene = random.nextInt(courses.length);
        courses[mutateGene] = new ScheduledModule(courses[mutateGene].getModule());
        storedFitness = calculateFitness();
        if (Scheduler.DEBUG) {
            System.out.println("@" + mutateGene + " After mutate:  " + this.toString());
        }
    }

    private int calculateFitness() {
        // Weight of any "required" fitness is 100, such as two courses overlapping
        // Weight of any "preferable" fitness is 1, such as keeping a 2-hour lecture as one block vs. two blocks or lecture not being at 8am
        // TODO: Tweak these fitness weights

        isValidSolution = true;

        int fitnessFromOverlappingClasses = Module.getAllModulesSize() * 100;
        // TODO: O(n^2) iterative search. Needs improvement. Ideas: Hash array, make sure no collisions. "Sort" array and then compare linearly.
        for (int i = 0; i < courses.length; ++i) {
            for (int j = i + 1; j < courses.length; ++j) {
                if (i != j) {
                    if (courses[i].equals(courses[j])) {
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
        for (ScheduledModule course : courses) {
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
