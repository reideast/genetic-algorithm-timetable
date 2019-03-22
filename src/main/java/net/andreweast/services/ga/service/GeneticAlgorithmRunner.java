package net.andreweast.services.ga.service;

import net.andreweast.services.ga.geneticalgorithm.GeneticAlgorithmJob;
import net.andreweast.services.ga.geneticalgorithm.GeneticAlgorithmJobData;

/**
 * Control (start, interrupt, and get data from) a sub thread
 *
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
        jobThread.start();
    }

    public void stop() {
        job.setIsRunning(false);
    }

    public int getCurrentGeneration() {
        System.out.println("Attempting to get data from sub-thread");

        System.out.println("Found the running job? handle=" + jobThread);
        if (jobThread != null) {
            System.out.println("state=" + jobThread.getState());
            System.out.println("name=" + jobThread.getName());
            System.out.println("id=" + jobThread.getId());
        }

        return job.getCurrentGeneration();
    }

    public int getTotalGenerations() {
        System.out.println("Attempting to get max gen from sub-thread");
        return job.getNumGenerations();
    }
}
