package net.andreweast.services.ga.service;

import net.andreweast.exception.DataNotFoundException;
import net.andreweast.services.data.api.JobRepository;
import net.andreweast.services.data.api.ScheduleRepository;
import net.andreweast.services.data.api.ScheduledModuleRepository;
import net.andreweast.services.data.model.Schedule;
import net.andreweast.services.data.model.ScheduledModulePK;
import net.andreweast.services.ga.geneticalgorithm.GAJobData;
import net.andreweast.services.ga.geneticalgorithm.ScheduledModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Puts all information from a completed Genetic Algorithm run back into the database
 */
@Service
public class GeneticAlgorithmSerializer {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ScheduledModuleRepository scheduledModuleRepository;

    public void writeScheduleData(GAJobData gaData, Long scheduleId) {
        // Anytime a schedule is modified by the GA, it is not longer new, therefore should be considered a "work-in-progress"
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(DataNotFoundException::new);
        schedule.setWip(true);
        scheduleRepository.save(schedule);

        List<ScheduledModule> scheduledModules = gaData.getScheduledModules();
//        List<net.andreweast.services.data.model.ScheduledModule> entities = new ArrayList<>();
//        for (ScheduledModule item : scheduledModules) {
//            net.andreweast.services.data.model.ScheduledModule entity = new net.andreweast.services.data.model.ScheduledModule();
////            ScheduledModulePK pk = new ScheduledModulePK();
////            pk.setScheduleId(scheduleId);
////            pk.setModuleId(item.getModule().getId());
////            entity.setId(pk);
////            entity.setSchedule();
////            entity.setTimeslot();
//        }
//        scheduledModuleRepository.saveAll(entities);

//        List<net.andreweast.services.data.model.ScheduledModule> entities = scheduledModuleRepository.findBySchedule_ScheduleId(scheduleId);
//        scheduledModuleRepository.saveAll(entities);

//        for (ScheduledModule item : scheduledModules) {
//            ScheduledModulePK pk = new ScheduledModulePK();
//            pk.setScheduleId(scheduleId);
//            pk.setModuleId(item.getModule().getId());
//            net.andreweast.services.data.model.ScheduledModule entity = scheduledModuleRepository.findById(pk).orElseThrow(DataNotFoundException::new);
//            entity.setTimeslot();
//        }

        // The results of a genetic algorithm run are encapsulated with the successfully scheduled modules,
        // and that's all that needs to be written back to DB
        int numUpdated;
        for (ScheduledModule item : scheduledModules) {
            numUpdated = scheduledModuleRepository.upsert(scheduleId, item.getModule().getId(), item.getTimeSlot().getId(), item.getVenue().getId());
            if (numUpdated != 1) {
                System.out.println("Saved ScheduledModule to database, but incorrect no. of rows modified: " + numUpdated); // TODO: Logger critical
            }
        }
    }

    public void deleteJobForSchedule(Long scheduleId) {
        // Finally, mark this Job as being done by deleting the Job record, setting the Schedule's job foreign key to null
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(DataNotFoundException::new);

        // Only attempt to delete job if this Schedule record has a job
        // Since this is wired to an idempotent REST service, if it doesn't exist (already deleted or never did), then no need for an error!
        if (schedule.getJob() == null) {
            System.out.println("Schedule record has no job to delete, schedule_id=" + scheduleId); // TODO: Logger
        } else {
            net.andreweast.services.data.model.Job job = jobRepository.findById(schedule.getJob().getJobId()).orElseThrow(DataNotFoundException::new);

            schedule.setJob(null);
            scheduleRepository.save(schedule);

            jobRepository.delete(job);
        }
    }
}
