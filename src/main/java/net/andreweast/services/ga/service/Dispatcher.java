package net.andreweast.services.ga.service;

import net.andreweast.exception.DataNotFoundException;
import net.andreweast.services.data.api.JobRepository;
import net.andreweast.services.data.model.Job;
import net.andreweast.services.ga.geneticalgorithm.GeneticAlgorithmJobData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

/**
 * Dispatch genetic algorithm jobs
 * A service-view connection between the REST API controller and the GA itself
 */
@Service
public class Dispatcher {
    @Autowired
    private DbToGaDeserializer dbToGaDeserializer;

    @Autowired
    private JobRepository jobRepository;

    // An in-memory datastore of current jobs, saved such that they can be queried or stopped later
    // Why is in-memory is acceptable, don't need to go to database? If the server crashes, the job is lost anyway!
    private static final HashMap<Long, GeneticAlgorithmRunner> runningJobHandles = new HashMap<>();

    /**
     * Creates a dynamic GA job based on the static Schedule database record
     *
     * @param scheduleId Database record to fetch
     * @return The created Job's data
     */
    public Job dispatchNewJobForSchedule(Long scheduleId, int numGenerations) throws DataNotFoundException, ResponseStatusException {
        // Save the to the database that we are starting a new job. Throws HTTP errors if such a job is already running
        Job job = dbToGaDeserializer.createJobForSchedule(scheduleId, numGenerations);

        // Get all the data the job will need from the database
        GeneticAlgorithmJobData geneticAlgorithmJobData = dbToGaDeserializer.generateGADataFromDatabase(scheduleId);
        geneticAlgorithmJobData.setScheduleId(scheduleId);
        geneticAlgorithmJobData.setJobId(job.getJobId());
        geneticAlgorithmJobData.setNumGenerations(numGenerations);

        // Start the job!
        GeneticAlgorithmRunner jobRunner = new GeneticAlgorithmRunner(geneticAlgorithmJobData);
        jobRunner.start();

        // Save a handle to the job in the in-memory database
        runningJobHandles.put(geneticAlgorithmJobData.getJobId(), jobRunner);

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

    /**
     * Ask the running Job thread for the current generation number it is on
     * It will also write this information into the database for this job
     *
     * @param jobId
     * @return A {@link Job}, which will be serialized to JSON
     */
    public Job getStatusForJob(Long jobId) {
        GeneticAlgorithmRunner jobHandle = runningJobHandles.get(jobId);
        System.out.println("Found the running job? handle=" + jobHandle); // DEBUG
        if (jobHandle != null) {
            Job job = jobRepository.findById(jobId).orElseThrow(DataNotFoundException::new);

            // Query task runner, which uses an Atomic variable to query the running thread
            job.setCurrentGeneration(jobHandle.getCurrentGeneration());
            job.setLastStatusUpdateTime(new Timestamp(new Date().getTime())); // Timestamp of now
            jobRepository.save(job);

            return job;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No handle to job with that ID exists to check its status", new DataNotFoundException());
        }
    }

    /**
     * This method should stop the Job thread, and then write its results to the DB
     *
     * @param jobId Database job_id to stop
     */
    public void stopJob(Long jobId) {
        GeneticAlgorithmRunner jobHandle = runningJobHandles.get(jobId);
        System.out.println("Stopping job id=" + jobId + ", handle=" + jobHandle); // DEBUG
        if (jobHandle != null) {
            jobHandle.stop();
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No handle to job with that ID exists to be deleted", new DataNotFoundException());
        }
    }
}
