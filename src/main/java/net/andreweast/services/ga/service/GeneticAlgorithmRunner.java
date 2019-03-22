package net.andreweast.services.ga.service;

import net.andreweast.services.ga.geneticalgorithm.GeneticAlgorithmJob;
import net.andreweast.services.ga.geneticalgorithm.GeneticAlgorithmJobData;

/**
 * Control (start, interrupt, and get data from) a sub thread
 * Interacts over Atomic variables
 * Method is from: https://www.baeldung.com/java-thread-stop
 */
public class GeneticAlgorithmRunner {
    private GeneticAlgorithmJob job;
    private Thread jobThread;

    public GeneticAlgorithmRunner(GeneticAlgorithmJobData geneticAlgorithmJobData) {
        this.job = new GeneticAlgorithmJob(geneticAlgorithmJobData);
    }

    public void start() {
        jobThread = new Thread(job);
        job.setIsRunning(true);
        jobThread.start();
    }

    public void stop() {
        job.setIsRunning(false);
    }

    public int getCurrentGeneration() {
        return job.getCurrentGeneration();
    }
}
