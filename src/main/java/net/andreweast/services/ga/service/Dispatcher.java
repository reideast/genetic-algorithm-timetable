package net.andreweast.services.ga.service;

import net.andreweast.exception.DataNotFoundException;
import net.andreweast.services.data.model.Job;
import net.andreweast.services.ga.geneticalgorithm.GAJobData;
import net.andreweast.services.ga.geneticalgorithm.Module;
import net.andreweast.services.ga.geneticalgorithm.ScheduledModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

/**
 * Dispatch genetic algorithm jobs
 * A service-view connection between the REST API controller and the GA itself
 */
@Service
public class Dispatcher {
    @Autowired
    private GeneticAlgorithmDeserializer geneticAlgorithmDeserializer;

    @Autowired
    private GeneticAlgorithmSerializer geneticAlgorithmSerializer;

    // TODO: Perhaps Dispatcher should be doing this work! (That way, GADeserializer can correctly have responsibility for creating a new job)
    // TODO: ...that would require Dispatcher to run in THIS thread, therefore it could throw HTTP errors to disrupt this REST call
    // TODO: And that's OK, that's a more logical separation of concerns

    /**
     * Creates a dynamic GA job based on the static Schedule database record
     * @param scheduleId Database record to fetch
     * @return The created Job's data
     */
    public Job dispatchNewJobForSchedule(Long scheduleId) throws DataNotFoundException, ResponseStatusException {
        // Save the to the database that we are starting a new job. Throws HTTP errors if such a job is already running
        Job job = geneticAlgorithmDeserializer.createJobForSchedule(scheduleId);

        // Get all the data the job will need from the database
        GAJobData allGAJobData = geneticAlgorithmDeserializer.generateGAJobDataFromDatabase(scheduleId);

        // TODO: save a handle to this thread to database (Job entity) s.t. the thread can be cancelled later. Don't know how to do this, since Thread can't be serialised

        new Thread(() -> {
            try {
                // TODO: Actually run the job!
                Thread.sleep(5 * 1000);

                // DEBUG:
                // But, to simulate a job, we'll make a set of random ScheduledModules
                List<ScheduledModule> randomScheduledModules = new ArrayList<>();
                for (Module module : allGAJobData.getModules()) {
                    randomScheduledModules.add(new ScheduledModule(module, allGAJobData.getRandomVenue(), allGAJobData.getRandomTimeslot()));
                }
                allGAJobData.setScheduledModules(randomScheduledModules);

                System.out.println("Job's done, m'lord!"); // DEBUG

                // DEBUG:
                System.out.println("Scheduled Modules:");
                for (ScheduledModule item : allGAJobData.getScheduledModules()) {
                    System.out.println(item);
                }

                // TODO: This should be done by the class which runs the job, in its thread
                // TODO: Should be in its own method in that class, such that it can be called when job is killed OR when job finishes normally
                geneticAlgorithmSerializer.writeScheduleData(allGAJobData, scheduleId);
                geneticAlgorithmSerializer.deleteJobForSchedule(scheduleId);
            } catch (InterruptedException e) {
                // There's no way to deal this anymore...the REST call has already returned!
                e.printStackTrace();
            }
        }).start();

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
