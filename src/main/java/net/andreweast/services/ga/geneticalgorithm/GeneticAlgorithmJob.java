package net.andreweast.services.ga.geneticalgorithm;

import net.andreweast.BeanUtil;
import net.andreweast.services.ga.service.Dispatcher;
import net.andreweast.services.ga.service.GaToDbSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GeneticAlgorithmJob implements Runnable {
    private final GaToDbSerializer gaToDbSerializer;
    private AtomicBoolean isRunning;
    private GeneticAlgorithmJobData masterData;
    private AtomicInteger numGenerations;
    private AtomicInteger currentGeneration;

    public GeneticAlgorithmJob(GeneticAlgorithmJobData geneticAlgorithmJobData) {
        this.masterData = geneticAlgorithmJobData;

        gaToDbSerializer = BeanUtil.getBean(GaToDbSerializer.class);
        System.out.println("Attempted to get a gaToDbSerializer=" + gaToDbSerializer); // DEBUG

        numGenerations = new AtomicInteger(masterData.getNumGenerations());
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
    }

    private void runAllGenerations() {
        // TODO: Figure out how to have this be interruptable (requires serializing a handle to this thread into the database, maybe?)
        currentGeneration.set(0);
        isRunning.set(true);
        while (isRunning.get()) { // Use of AtomicBoolean to control a Thread see: https://www.baeldung.com/java-thread-stop
            // TODO: Actually run the job!
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // DEBUG: There's no way to deal this anymore...the REST call has already returned!
                // Should I do the interrupt() way, see: https://www.baeldung.com/java-thread-stop
                e.printStackTrace();
            }
            if (currentGeneration.incrementAndGet() > numGenerations.get()) {
                isRunning.set(false);
            }
        }
        System.out.println("Job's done, m'lord!"); // DEBUG
    }

    private void saveBestIndividualToMasterData() {
        // DEBUG: To simulate a job, we'll make a set of random ScheduledModules
        List<ScheduledModule> randomScheduledModules = new ArrayList<>();
        for (Module module : masterData.getModules()) {
            randomScheduledModules.add(new ScheduledModule(module, masterData.getRandomVenue(), masterData.getRandomTimeslot()));
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
        Dispatcher.jobCompleted(masterData.getJobId());
        gaToDbSerializer.deleteJobForSchedule(masterData.getScheduleId());
    }

    public boolean getIsRunning() {
        return isRunning.get();
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning.set(isRunning);
    }

    public GeneticAlgorithmJobData getMasterData() {
        return masterData;
    }

    public void setMasterData(GeneticAlgorithmJobData masterData) {
        this.masterData = masterData;
    }

    public int getNumGenerations() {
        return numGenerations.get();
    }

    public void setNumGenerations(int numGenerations) {
        this.numGenerations.set(numGenerations);
    }

    public int getCurrentGeneration() {
        return currentGeneration.get();
    }

    public void setCurrentGeneration(int currentGeneration) {
        this.currentGeneration.set(currentGeneration);
    }
}
