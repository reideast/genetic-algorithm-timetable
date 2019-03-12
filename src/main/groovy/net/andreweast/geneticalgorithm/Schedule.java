package net.andreweast.geneticalgorithm;

public class Schedule {
    private static final int GENERATION_LIMIT = 300;

    static final boolean DEBUG = true;

    private Population population;

    public Schedule() {
        population = new Population();
    }

    public String schedule() {
        StringBuilder s = new StringBuilder();
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
                s.append(population.toFitnessList()).append("<br>");
            }

            if (i == 0) {
                runningAverage = System.nanoTime() - startTime;
            } else {
                runningAverage += ((System.nanoTime() - startTime) - runningAverage) / ((double) (i + 1));
            }

        }

        s.append("Complexity of dataset:<br>");
        s.append("Courses: " + Course.getAllCourses().length + "<br>");
        s.append("Venues x TimeSlots: " + (Venue.Room.values().length * TimeSlot.DayOfWeek.values().length * TimeSlot.StartTime.values().length) + "<br>");

        s.append("\nRunning time stats:<br>");
        s.append("Time init: " + (initTime * 1.0E-6) + " ms<br>");
        s.append("Average generation time: " + (runningAverage * 1E-6) + " ms<br>");

        return s.toString();
    }
}
