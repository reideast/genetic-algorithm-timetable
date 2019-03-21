package net.andreweast.services.ga.service;

import net.andreweast.services.data.model.Schedule;
import net.andreweast.services.ga.dataaccess.JobDao;

/**
 * Gets all information needed for a Genetic Algorithm run from the database
 * If this is a new scheduling job, also create the ScheduledModules objects for the job
 * Returns a single object that encapsulates all data for the GA
 */
public class GeneticAlgorithmDeserializer {
    public static JobDao getScheduleData(Schedule schedule) {
        return JobDao.generateJobDaoFromDatabase(schedule);
    }
}
