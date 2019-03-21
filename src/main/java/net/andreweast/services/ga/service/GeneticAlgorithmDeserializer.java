package net.andreweast.services.ga.service;

import net.andreweast.exception.DataNotFoundException;
import net.andreweast.services.data.api.JobRepository;
import net.andreweast.services.data.api.ScheduleRepository;
import net.andreweast.services.data.model.Job;
import net.andreweast.services.data.model.Schedule;
import net.andreweast.services.ga.dataaccess.JobDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Gets all information needed for a Genetic Algorithm run from the database
 * If this is a new scheduling job, also create the ScheduledModules objects for the job
 * Returns a single object that encapsulates all data for the GA
 */
@Service
public class GeneticAlgorithmDeserializer {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private JobRepository jobRepository;

    public JobDao getScheduleData(Long scheduleId) throws DataNotFoundException {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(DataNotFoundException::new);

        return JobDao.generateJobDaoFromDatabase(schedule);
    }

    public Job createJobForSchedule(Long scheduleId) throws DataNotFoundException, ResponseStatusException {
        System.out.println("Deserializer#createJobForSchedule, scheduleId=" + scheduleId + ", scheduleRepository=" + scheduleRepository);

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

        return job;
    }
}
