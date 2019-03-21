package net.andreweast.services.ga.service;

import net.andreweast.exception.DataNotFoundException;
import net.andreweast.services.data.api.JobRepository;
import net.andreweast.services.data.api.ScheduleRepository;
import net.andreweast.services.data.model.Job;
import net.andreweast.services.data.model.Schedule;
import net.andreweast.services.ga.dataaccess.JobDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.Date;

public class Dispatcher {
    @Autowired
    private static ScheduleRepository scheduleRepository;

    @Autowired
    private static JobRepository jobRepository;

    // TODO: Perhaps Dispatcher should be doing this work! (That way, GADeserializer can correctly have responsibility for creating a new job)
    // TODO: ...that would require Dispatcher to run in THIS thread, therefore it could throw HTTP errors to disrupt this REST call
    // TODO: And that's OK, that's a more logical separation of concerns
    public static Job dispatchNewJobForSchedule(Long scheduleId) {
        // Find Schedule in database
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(DataNotFoundException::new);

        // Make sure this schedule doesn't have a currently dispatched job
        if (schedule.getJob() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Job has already been started"); // See: https://www.baeldung.com/exception-handling-for-rest-with-spring
        }

        // Create job
        Job job = new Job();
        job.setStartDate(new Timestamp(new Date().getTime())); // Timestamp to now
        job.setSchedule(schedule);
        jobRepository.save(job);

        // Notify database that schedule has been started
        schedule.setJob(job);
        scheduleRepository.save(schedule);

        // Get all the data the job will need from the database
        JobDao allGAJobData = GeneticAlgorithmDeserializer.getScheduleData(schedule);

        // TODO: save a handle to this thread to database (Job entity) s.t. the thread can be cancelled later

        try {
            // TODO: Actually run the job!
            Thread.sleep(10 * 1000);
            System.out.println("Job's done, m'lord!"); // DEBUG

            // TODO: This should be done by the class which runs the job, in its thread
            // TODO: Should be in its own method in that class, such that it can be called when job is killed OR when job finishes normally
            GeneticAlgorithmSerializer.writeScheduleData(allGAJobData, scheduleId);
        } catch (InterruptedException e) {
            // There's no way to deal this anymore...the REST call has already returned!
            e.printStackTrace();
        }

        return job;
    }

    public static Job getStatusForJob(Long jobId) {
        // TODO: This method should ask the running Job thread for the current generation number it is on
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Checking for job status not implemented", new UnsupportedOperationException());
    }

    public static void stopJobForSchedule(Long jobId) {
        // TODO: This method should stop the Job thread, and then write its results to the DB
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Deleting in-progress job not implemented", new UnsupportedOperationException());
    }
}
