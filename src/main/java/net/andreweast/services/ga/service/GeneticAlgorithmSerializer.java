package net.andreweast.services.ga.service;

import net.andreweast.exception.DataNotFoundException;
import net.andreweast.services.data.api.JobRepository;
import net.andreweast.services.data.api.ScheduleRepository;
import net.andreweast.services.data.model.Job;
import net.andreweast.services.data.model.Schedule;
import net.andreweast.services.ga.dataaccess.JobDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Puts all information from a completed Genetic Algorithm run back into the database
 */
@Service
public class GeneticAlgorithmSerializer {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private JobRepository jobRepository;

    public void writeScheduleData(JobDao gaData, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(DataNotFoundException::new);

        // TODO: use each DTO to save its own data to database
        JobDao.saveJobDaoToDatabase(gaData, schedule);
    }

    public void deleteJobForSchedule(Long scheduleId) {
        // Finally, mark this Job as being done by deleting the Job record, setting the Schedule's job foreign key to null
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(DataNotFoundException::new);

        // Only attempt to delete job if this Schedule record has a job
        // Since this is wired to an idempotent REST service, if it doesn't exist (already deleted or never did), then no need for an error!
        if (schedule.getJob() == null) {
            System.out.println("Schedule record has no job to delete, schedule_id=" + scheduleId); // TODO: Logger
        } else {
            Job job = jobRepository.findById(schedule.getJob().getJobId()).orElseThrow(DataNotFoundException::new);

            schedule.setJob(null);
            scheduleRepository.save(schedule);

            jobRepository.delete(job);
        }
    }
}
