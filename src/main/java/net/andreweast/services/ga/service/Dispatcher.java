package net.andreweast.services.ga.service;

import net.andreweast.exception.DataNotFoundException;
import net.andreweast.services.data.model.Job;
import net.andreweast.services.ga.geneticalgorithm.GeneticAlgorithmJob;
import net.andreweast.services.ga.geneticalgorithm.GeneticAlgorithmJobData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

/**
 * Dispatch genetic algorithm jobs
 * A service-view connection between the REST API controller and the GA itself
 */
@Service
public class Dispatcher {
    @Autowired
    private DbToGaDeserializer dbToGaDeserializer;

    // An in-memory datastore of current jobs, saved such that they can be queried or stopped later
    private static final HashMap<Long, GeneticAlgorithmRunner> runningJobHandles = new HashMap<>();

    /**
     * Creates a dynamic GA job based on the static Schedule database record
     *
     * @param scheduleId Database record to fetch
     * @return The created Job's data
     */
    public Job dispatchNewJobForSchedule(Long scheduleId) throws DataNotFoundException, ResponseStatusException {
        // Save the to the database that we are starting a new job. Throws HTTP errors if such a job is already running
        Job job = dbToGaDeserializer.createJobForSchedule(scheduleId);

        // Get all the data the job will need from the database
        GeneticAlgorithmJobData geneticAlgorithmJobData = dbToGaDeserializer.generateGAJobDataFromDatabase(scheduleId, job.getJobId());

        GeneticAlgorithmRunner jobRunner = new GeneticAlgorithmRunner(geneticAlgorithmJobData);
        jobRunner.start();

        // TODO: save a handle to this thread to database (Job entity) s.t. the thread can be cancelled later. Don't know how to do this, since Thread can't be serialised
        runningJobHandles.put(geneticAlgorithmJobData.getJobId(), jobRunner);

        System.out.println("Job thread handle saved=" + jobRunner); // DEBUG

        return job;
    }

    /**
     * A job has completed. Removed it from the datastore
     *
     * @param jobId The database ID of the job to remove
     */
    public static void jobCompleted(Long jobId) {
        System.out.println("Current job handles:");
        runningJobHandles.forEach((key, value) -> {
            System.out.println(key + ":" + value);
        });
        runningJobHandles.remove(jobId);
        System.out.println("Job removed, handles:");
        runningJobHandles.forEach((key, value) -> {
            System.out.println(key + ":" + value);
        });
    }

    public Job getStatusForJob(Long jobId) {
        // TODO: This method should ask the running Job thread for the current generation number it is on
//        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Checking for job status not implemented", new UnsupportedOperationException());

        GeneticAlgorithmRunner jobHandle = runningJobHandles.get(jobId);
        System.out.println("Found the running job? handle=" + jobHandle);

        System.out.println("current gen=" + jobHandle.getCurrentGeneration());
        System.out.println("max gen=" + jobHandle.getTotalGenerations());

        Job dummy = new Job();
        dummy.setJobId(jobId);
        return dummy;
    }

    public void stopJobForSchedule(Long jobId) {
        // TODO: This method should stop the Job thread, and then write its results to the DB
        runningJobHandles.remove(jobId);
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Deleting in-progress job not implemented", new UnsupportedOperationException());
    }
}
