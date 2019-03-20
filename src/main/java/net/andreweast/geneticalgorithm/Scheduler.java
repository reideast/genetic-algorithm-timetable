package net.andreweast.geneticalgorithm;

public class Scheduler {
    private static final int GENERATION_LIMIT = 300;

    static final boolean DEBUG = true;

    private Population population;

    public Scheduler() {
        population = new Population();
    }

    public Population schedule() {
//        StringBuilder s = new StringBuilder();
        long startTime = System.nanoTime();

        System.out.println("************* GEN init *************");
        System.out.println(population);
        final long initTime = System.nanoTime() - startTime;

        double runningAverage = -1;
        // TODO: Run with GENERATION_LIMIT as a maxium, but stop if an acceptable solution is found
        for (int i = 0; i < GENERATION_LIMIT ; ++i){
            startTime = System.nanoTime();
//            println "************* GEN $i *************"

            population.select();

            population.crossover();
//            population.selectWithSamePopulation()

            population.mutate();

            if (DEBUG) {
                System.out.println(population.toFitnessList());
            }

            if (i == 0) {
                runningAverage = System.nanoTime() - startTime;
            } else {
                runningAverage += ((System.nanoTime() - startTime) - runningAverage) / ((double) (i + 1));
            }

        }

        System.out.println("Complexity of dataset:");
        System.out.println("Courses: " + Course.getAllCoursesSize());
        System.out.println("Venues x TimeSlots: " + (Venue.Room.values().length * TimeSlot.DayOfWeek.values().length * TimeSlot.StartTime.values().length));

        System.out.println("\nRunning time stats:");
        System.out.println("Time init: " + (initTime * 1.0E-6) + " ms");
        System.out.println("Average generation time: " + (runningAverage * 1E-6) + " ms");

//        return s.toString();
        return population;
    }
}
