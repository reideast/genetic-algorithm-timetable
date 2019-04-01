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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    // A thread pool to spawn dispatched worker tasks from
    // Those tasks are also allowed to spawn tasks from the thread pool
    private static final ExecutorService threadPool = Executors.newCachedThreadPool(); // CachedPool can: Makes new threads as need. After terminating, keeps threads available for reuse (for 60 seconds)
    // DEBUG: There might be more justification for FixedThreadPool, maybe of 20 or so available threads
    // DEBUG: the queue thing turned me off, but then I realised that having a queue is still better than calculating fitness synchronously

    /**
     * Creates a dynamic GA job based on the static Schedule database record
     *
     * @param scheduleId Database record to fetch
     * @return The created Job's data
     */
    public Job dispatchNewJobForSchedule(Long scheduleId, int numGenerations, int populationSize, int proportionRunDownGenerations, int crossoverPercentage, int mutatePercentage, int mutateGenesMax, int numEliteSurvivors, int queryRate) throws DataNotFoundException, ResponseStatusException {
        // Save the to the database that we are starting a new job. Throws HTTP errors if such a job is already running
        Job job = dbToGaDeserializer.createJobForSchedule(scheduleId, numGenerations);

        // Get all the data the job will need from the database
        GeneticAlgorithmJobData geneticAlgorithmJobData = dbToGaDeserializer.generateGADataFromDatabase(scheduleId);
        geneticAlgorithmJobData.setScheduleId(scheduleId);
        geneticAlgorithmJobData.setJobId(job.getJobId());
        geneticAlgorithmJobData.setNumGenerations(numGenerations);
        geneticAlgorithmJobData.setPopulationSize(populationSize);
        geneticAlgorithmJobData.setProportionRunDownGenerations(proportionRunDownGenerations / 100.0f);
        geneticAlgorithmJobData.setCrossoverProbability(crossoverPercentage / 100.0f);
        geneticAlgorithmJobData.setMutateProbability(mutatePercentage / 100.0f);
        geneticAlgorithmJobData.setMutatedGenesMax(mutateGenesMax);
        geneticAlgorithmJobData.setNumEliteSurvivors(numEliteSurvivors);
        geneticAlgorithmJobData.setQueryRate(queryRate / 100.0f);

        // Start the job!
        GeneticAlgorithmRunner jobRunner = new GeneticAlgorithmRunner(geneticAlgorithmJobData, threadPool);
        jobRunner.start();

        // Save a handle to the job in the in-memory datastore
        runningJobHandles.put(geneticAlgorithmJobData.getJobId(), jobRunner);

        return job;
    }

    /**
     * A job has completed. Removed it from the datastore
     *
     * @param jobId The database ID of the job to remove
     */
    public void jobCompleted(Long jobId) {
        runningJobHandles.remove(jobId);
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
        if (jobHandle != null) {
            jobHandle.stop();
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No handle to job with that ID exists to be deleted", new DataNotFoundException());
        }
    }
}
