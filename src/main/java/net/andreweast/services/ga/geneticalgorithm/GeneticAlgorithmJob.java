package net.andreweast.services.ga.geneticalgorithm;

import net.andreweast.BeanUtil;
import net.andreweast.services.ga.service.Dispatcher;
import net.andreweast.services.ga.service.GaToDbSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class GeneticAlgorithmJob implements Runnable {
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
    private final int numGenerations;
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

        numGenerations = masterData.getNumGenerations();
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
        isRunning.set(true);

        long startTime = System.nanoTime(); // DEBUG
        System.out.println("************* GEN init *************"); // DEBUG
        System.out.println(population); // DEBUG
        final long initTime = System.nanoTime() - startTime; // DEBUG
        double runningAverage = -1; // DEBUG
        while (isRunning.get()) { // Use of AtomicBoolean to control a Thread see: https://www.baeldung.com/java-thread-stop
            // TODO: Actually run the job!
            // TODO: act differently based on masterData.isModifyExistingJob

            startTime = System.nanoTime(); // DEBUG

            population.select();

            population.crossover();

            population.mutate();

            if (DEBUG) { // DEBUG
                System.out.println(population.toFitnessList());
            }

            // DEBUG
            if (currentGeneration.get() == 0) {
                runningAverage = System.nanoTime() - startTime;
            } else {
                runningAverage += ((System.nanoTime() - startTime) - runningAverage) / ((double) (currentGeneration.get() + 1));
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO: Should I do the interrupt() way, see: https://www.baeldung.com/java-thread-stop

                // DEBUG: How to deal with this error? There's no way to send it back to user directly anymore...the REST call has already returned!
                e.printStackTrace();
            }
            if (currentGeneration.incrementAndGet() > numGenerations) {
                isRunning.set(false);
            }
        }
        // DEBUG
        System.out.println("Complexity of dataset:");
        System.out.println("Num modules: " + masterData.getModules().size());
        System.out.println("Venues x Timeslots: " + (masterData.getVenues().size() * masterData.getTimeslots().size()));
        System.out.println("Running time stats:");
        System.out.println("Time init: " + (initTime * 1.0E-6) + " ms");
        System.out.println("Average generation time: " + (runningAverage * 1E-6) + " ms");

        System.out.println("GA generations have completed, job=" + masterData.getJobId() + ", schedule=" + masterData.getScheduleId()); // FUTURE: Logger info
    }

    /**
     * Inspects Population, and choose a single Chromosome to write back into {@link this.masterData}
     */
    private void saveBestIndividualToMasterData() {
        // TODO: Get info from Population, and choose a Chromosome to write into {@link masterData}

        // DEBUG: To simulate a job, we'll just make a set of random ScheduledModules
        List<ScheduledModule> randomScheduledModules = new ArrayList<>();
        for (Module module : masterData.getModules()) {
            randomScheduledModules.add(new ScheduledModule(module, masterData.getRandomVenue(), masterData.getRandomTimeslot(), masterData));
        }
        masterData.setScheduledModules(randomScheduledModules);

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
        return numGenerations;
    }

    public int getCurrentGeneration() {
        return currentGeneration.get();
    }
}
