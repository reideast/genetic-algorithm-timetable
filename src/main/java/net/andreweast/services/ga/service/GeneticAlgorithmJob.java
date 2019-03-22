package net.andreweast.services.ga.service;

import net.andreweast.BeanUtil;
import net.andreweast.services.ga.geneticalgorithm.GeneticAlgorithmJobData;
import net.andreweast.services.ga.geneticalgorithm.Module;
import net.andreweast.services.ga.geneticalgorithm.ScheduledModule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GeneticAlgorithmJob implements Runnable {
    private final GaToDbSerializer gaToDbSerializer;
    private AtomicBoolean isRunning;
    private GeneticAlgorithmJobData masterData;

    public GeneticAlgorithmJob(GeneticAlgorithmJobData geneticAlgorithmJobData) {
        gaToDbSerializer = BeanUtil.getBean(GaToDbSerializer.class);
        System.out.println("Attempted to get a gaToDbSerializer=" + gaToDbSerializer); // DEBUG
        isRunning = new AtomicBoolean(false);

        masterData = geneticAlgorithmJobData;
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
        isRunning.set(true); // Use of AtomicBoolean see: https://www.baeldung.com/java-thread-stop
        while (isRunning.get()) {
            // TODO: Actually run the job!
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                // There's no way to deal this anymore...the REST call has already returned!
                e.printStackTrace();
            }
            isRunning.set(false);
            System.out.println("Job's done, m'lord!"); // DEBUG
        }
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
        gaToDbSerializer.deleteJobForSchedule(masterData.getScheduleId());
    }
}
