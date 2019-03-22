package net.andreweast.services.ga.service;

import net.andreweast.exception.DataNotFoundException;
import net.andreweast.services.data.model.Job;
import net.andreweast.services.ga.geneticalgorithm.GeneticAlgorithmJobData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Dispatch genetic algorithm jobs
 * A service-view connection between the REST API controller and the GA itself
 */
@Service
public class Dispatcher {
    @Autowired
    private DbToGaDeserializer dbToGaDeserializer;

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
        GeneticAlgorithmJobData allGeneticAlgorithmJobData = dbToGaDeserializer.generateGAJobDataFromDatabase(scheduleId, job.getJobId());

        GeneticAlgorithmJob geneticAlgorithmJob = new GeneticAlgorithmJob(allGeneticAlgorithmJobData);
        // TODO: save a handle to this thread to database (Job entity) s.t. the thread can be cancelled later. Don't know how to do this, since Thread can't be serialised
        Thread jobThread = new Thread(geneticAlgorithmJob);
        jobThread.start();

        return job;
    }

    public static net.andreweast.services.data.model.Job getStatusForJob(Long jobId) {
        // TODO: This method should ask the running Job thread for the current generation number it is on
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Checking for job status not implemented", new UnsupportedOperationException());
    }

    public static void stopJobForSchedule(Long jobId) {
        // TODO: This method should stop the Job thread, and then write its results to the DB
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Deleting in-progress job not implemented", new UnsupportedOperationException());
    }
}
