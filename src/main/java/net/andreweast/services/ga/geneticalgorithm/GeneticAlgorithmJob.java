package net.andreweast.services.ga.geneticalgorithm;

import net.andreweast.BeanUtil;
import net.andreweast.services.ga.service.Dispatcher;
import net.andreweast.services.ga.service.GaToDbSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class GeneticAlgorithmJob implements Runnable {
    // Parameters of the GA
    // These are to tweak the algorithm. Other major params (population size, max num generations) are passed as input from the REST interface
    // How many "extra" generations to run after a valid (no violated hard constraints) solution has emerged
    private static final int RUN_DOWN_NUM_GENERATIONS = 20;
    // Crossover with p = 0.6
    private static final int CROSSOVER_PERCENTAGE = 60;
    // Mutate a random individual with p = 0.9
    private static final int MUTATE_PERCENTAGE = 90;
    // How many of the very best in a population are guaranteed to survive
    private static final int ELITE_SURVIVORS = 1;

    // To control the thread, change this atomic (i.e. thread-safe) boolean. The thread will stop itself after the next generation
    private AtomicBoolean isRunning;

    // Spring @Service bean classes
    // Can't use @Autowired here because that would require letting Spring manage the lifecycle of this class,
    // but the lifecycle of this class is managed as a Thread
    private final GaToDbSerializer gaToDbSerializer;
    private final Dispatcher dispatcher;

    // Data on this job:
    // Master data is all the metadata the GA needs to run
    private GeneticAlgorithmJobData masterData;
    // Number of generations to stop after
    private final int numGenerationsMaximum;
    // Generation counter; Atomic so that it can be read by outside services querying this GA job's progress
    private AtomicInteger currentGeneration;

    // Data structures being used by the job in action
    private Population population;
    static final boolean DEBUG = true; // DEBUG

    public GeneticAlgorithmJob(GeneticAlgorithmJobData geneticAlgorithmJobData) {
        this.masterData = geneticAlgorithmJobData;

        // Directly get Spring @Services via a context-aware utility class
        gaToDbSerializer = BeanUtil.getBean(GaToDbSerializer.class);
        dispatcher = BeanUtil.getBean(Dispatcher.class);

        numGenerationsMaximum = masterData.getNumGenerations();
        currentGeneration = new AtomicInteger(0);

        isRunning = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        createInitialPopulation();
        runAllGenerations();
        saveBestIndividualToMasterData();
        writeBackToDatabase();
        finaliseJob();
    }

    private void createInitialPopulation() {
        // TODO: get data from the new DB-based objects
        population = new Population(masterData);
    }

    private void runAllGenerations() {
        currentGeneration.set(0);

        // Let the algorithm be quit earlier than the specified MAX generations
        // Such as, if all hard constraints are met, then let the algorithm run for several more generations, then quit
        int tentativeGenLimit = numGenerationsMaximum;
        // Is the algorithm currently running those "several more generations?"
        boolean isDoingFinalRunDown = false;

        long startTime = System.nanoTime(); // DEBUG
        System.out.println("************* GEN init *************"); // DEBUG
        System.out.println(population); // DEBUG
        final long initTime = System.nanoTime() - startTime; // DEBUG
        double runningAverage = -1; // DEBUG

        isRunning.set(true);
        while (isRunning.get()) { // Use of AtomicBoolean to control a Thread see: https://www.baeldung.com/java-thread-stop
            long generationTime = System.nanoTime(); // DEBUG

            population.crossover(CROSSOVER_PERCENTAGE);

            population.mutate(MUTATE_PERCENTAGE);

            // DEBUG: According to (Padhy 2005), selection should/may be done AFTER crossover & mutation
            population.select(ELITE_SURVIVORS); // Selection must be done after genetic crossover/mutate in order to find cached hasValidSolution

//            // DEBUG
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            if (DEBUG) { // DEBUG
                System.out.print("Gen " + currentGeneration.get() + ": ");
                System.out.println(population.toFitnessList());
            }

            // DEBUG
            if (currentGeneration.get() == 0) {
                runningAverage = System.nanoTime() - generationTime;
            } else {
                runningAverage += ((System.nanoTime() - generationTime) - runningAverage) / ((double) (currentGeneration.get() + 1));
            }

            // Increment generation counter, and then check for exit conditions
            if (currentGeneration.incrementAndGet() > numGenerationsMaximum) {
                isRunning.set(false);
            } else if (currentGeneration.get() > tentativeGenLimit) {
                if (population.hasValidSolution()) {
                    isRunning.set(false); // Can quit early! We had found a solution, ran some more generations as a "run down", and now we still have a solution
                    System.out.println("Found a valid solution and ran for several more generations. Quitting early!"); // FUTURE: Logger
                } else {
                    tentativeGenLimit = numGenerationsMaximum; // Ran several more generations, but have now LOST the valid solution. Go some more
                    isDoingFinalRunDown = false;
                    System.out.println("During the run down, the valid solution was lost. Population needs more more!"); // FUTURE: Logger
                }
            } else if (!isDoingFinalRunDown) {
                if (population.hasValidSolution()) {
                    tentativeGenLimit = currentGeneration.get() + RUN_DOWN_NUM_GENERATIONS;
                    isDoingFinalRunDown = true;
                    System.out.println("Found a valid solution! Doing a final run down now for " + RUN_DOWN_NUM_GENERATIONS + " generations"); // FUTURE: Logger
                }
                // else: There's no valid solution. Continue running the algorithm as normal
            } // else: Already doing a final run down, don't check if the valid solution still exists until we're done
        }
        // DEBUG
        System.out.println("Complexity of dataset:");
        System.out.println("Num modules: " + masterData.getModules().size());
        System.out.println("Venues x Timeslots: " + (masterData.getVenues().size() * masterData.getTimeslots().size()));
        System.out.println("Running time stats:");
        System.out.println("Time init: " + (initTime * 1.0E-6) + " ms");
        System.out.println("Average generation time: " + (runningAverage * 1E-6) + " ms");
        System.out.println("Total time: " + ((System.nanoTime() - startTime) * 1E-6) + " ms");

        System.out.println("GA generations have completed, job=" + masterData.getJobId() + ", schedule=" + masterData.getScheduleId()); // FUTURE: Logger info
    }

    /**
     * Inspects Population, and choose a single Chromosome to write back into {@link this.masterData}
     */
    private void saveBestIndividualToMasterData() {
        // Get info from Population, and choose a Chromosome to write back to {@link masterData}
        masterData.setScheduledModules(population.getBestChromosomeScheduledModule());

        // DEBUG:
        System.out.println("Scheduled Modules:");
        for (ScheduledModule item : masterData.getScheduledModules()) {
            System.out.println(item);
        }
    }

    private void writeBackToDatabase() {
        gaToDbSerializer.writeScheduleData(masterData, masterData.getScheduleId());
    }

    private void finaliseJob() {
        dispatcher.jobCompleted(masterData.getJobId());
        gaToDbSerializer.deleteJobForSchedule(masterData.getScheduleId());
    }

    public boolean getIsRunning() {
        return isRunning.get();
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning.set(isRunning);
    }

    public int getNumGenerations() {
        return numGenerationsMaximum;
    }

    public int getCurrentGeneration() {
        return currentGeneration.get();
    }
}
