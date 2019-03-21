package net.andreweast.services.ga.service;

import net.andreweast.exception.DataNotFoundException;
import net.andreweast.services.data.api.JobRepository;
import net.andreweast.services.data.api.ScheduleRepository;
import net.andreweast.services.data.model.Job;
import net.andreweast.services.data.model.Schedule;
import net.andreweast.services.ga.dataaccess.JobDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Puts all information from a completed Genetic Algorithm run back into the database
 */
public class GeneticAlgorithmSerializer {
    @Autowired
    private static ScheduleRepository scheduleRepository;

    @Autowired
    private static JobRepository jobRepository;

    public static void writeScheduleData(JobDao gaData, Long scheduleId) {
        // TODO: use each DTO to save its own data to database

        // Finally, mark this Job as being done by deleting the Job record, setting the Schedule's job foreign key to null
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(DataNotFoundException::new);

        Job job = jobRepository.findById(schedule.getJob().getJobId()).orElseThrow(DataNotFoundException::new);

        schedule.setJob(null);
        scheduleRepository.save(schedule);

        jobRepository.delete(job);
    }
}
