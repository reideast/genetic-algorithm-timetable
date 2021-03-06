package net.andreweast.services.ga.service;

import net.andreweast.exception.DataNotFoundException;
import net.andreweast.services.data.api.JobRepository;
import net.andreweast.services.data.api.ScheduleRepository;
import net.andreweast.services.data.api.ScheduledModuleRepository;
import net.andreweast.services.data.model.Schedule;
import net.andreweast.services.ga.geneticalgorithm.Gene;
import net.andreweast.services.ga.geneticalgorithm.GeneticAlgorithmJobData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.andreweast.WebSocketConfiguration.MESSAGE_PREFIX;

/**
 * Puts all information from a completed Genetic Algorithm job back into the database
 */
@Service
public class GaToDbSerializer {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ScheduledModuleRepository scheduledModuleRepository;

    @Autowired
    private SimpMessagingTemplate websocket;

    public void writeScheduleData(GeneticAlgorithmJobData gaData, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(DataNotFoundException::new);
        // Anytime a schedule is modified by the GA, it is not longer new, therefore should be considered a "work-in-progress"
        schedule.setWip(true);
        schedule.setFitness(gaData.getFitness());
        scheduleRepository.save(schedule);

        // Send a WebSocket to the frontend: this schedule has been updated!
        // FUTURE: Send to a different topic: One that uses the PAYLOAD field to know which schedule was updated. Then, go thorugh the list, and if that schedule is in the GUI, then it can ONLY GET THAT ONE
        this.websocket.convertAndSend(MESSAGE_PREFIX + "/updateSchedule", "Job's done, m'lord");

        // Update or create a database record for each scheduled module that the GA generated
        List<Gene> scheduledModules = gaData.getScheduledModules();
        int numUpdated;
        for (Gene item : scheduledModules) {
            numUpdated = scheduledModuleRepository.upsert(scheduleId, item.getModule().getId(), item.getTimeslot().getId(), item.getVenue().getId());
            if (numUpdated != 1) {
                System.out.println("Saved ScheduledModule to database, but incorrect no. of rows modified: " + numUpdated); // FUTURE: Logger critical
            }
        }
    }

    public void deleteJobForSchedule(Long scheduleId) {
        // Finally, mark this Job as being done by deleting the Job record, setting the Schedule's job foreign key to null
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(DataNotFoundException::new);

        // Only attempt to delete job if this Schedule record has a job
        // Since this is wired to an idempotent REST service, if it doesn't exist (already deleted or never did), then no need for an error!
        if (schedule.getJob() == null) {
            System.out.println("Schedule record has no job to delete, schedule_id=" + scheduleId); // FUTURE: Logger info
        } else {
            net.andreweast.services.data.model.Job job = jobRepository.findById(schedule.getJob().getJobId()).orElseThrow(DataNotFoundException::new);

            schedule.setJob(null);
            scheduleRepository.save(schedule);

            jobRepository.delete(job);
        }
    }
}
