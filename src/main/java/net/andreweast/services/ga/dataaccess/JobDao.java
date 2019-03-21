package net.andreweast.services.ga.dataaccess;

import net.andreweast.services.data.api.ScheduleRepository;
import net.andreweast.services.data.model.Schedule;
import net.andreweast.services.ga.geneticalgorithm.Module;
import net.andreweast.services.ga.geneticalgorithm.ScheduledModule;
import net.andreweast.services.ga.geneticalgorithm.TimeSlot;
import net.andreweast.services.ga.geneticalgorithm.Venue;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * This is the MASTER object: The {@link net.andreweast.services.ga.service.Dispatcher} will create one of these, (via a {@link net.andreweast.services.ga.service.GeneticAlgorithmDeserializer}
 * and then pass it to the actual GA runner to define ALL properties of the job
 *
 * Contains every piece of information (as component collections) that a GA job needs to create a schedule
 * Essentially, a very complex POJO
 */
public class JobDao implements Serializable {
    private List<TimeSlot> timeslots;
    private List<Module> modules;
    private List<Venue> venues;
    private List<ScheduledModule> scheduledModules;

    // Factory pattern
    // TODO: make sure I'm matching the Factory pattern correctly
    private JobDao() {}

    /**
     * Build up all GA-runner data structures by getting them from the database
     * @param schedule The schedule entity from the database
     */
    public static JobDao generateJobDaoFromDatabase(Schedule schedule) {
        JobDao data = new JobDao();

        // Get all timeslots, venues, and modules from database
        // They are not specific to this job (i.e. do not depend on database table "schedules")
        data.timeslots = TimeslotDao.generateTimeslotsFromDatabase();
        data.modules = ModulesDto.generateModulesFromDatabase();
        data.venues = VenuesDto.generateVenuesFromDatabase();

        // Find or make the ScheduledModules objects, which are the genes that make up each chromosomes in the GA
        if (schedule.getWip()) {
            // This job is a "work in progress", therefore scheduled_modules already exist
            data.scheduledModules = ScheduledModuleDao.generateScheduledModulesFromDatabase(schedule.getScheduleId());
        } else {
            // This job is a new job, therefore need to create all the scheduled_modules
            data.scheduledModules = ScheduledModuleDao.createSetOfScheduledModulesInDatabase(schedule.getScheduleId());
        }

        return data;
    }

    public static void saveJobDaoToDatabase(JobDao data, Schedule schedule) {
        throw new UnsupportedOperationException();
    }

    public List<TimeSlot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<TimeSlot> timeslots) {
        this.timeslots = timeslots;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

    public List<ScheduledModule> getScheduledModules() {
        return scheduledModules;
    }

    public void setScheduledModules(List<ScheduledModule> scheduledModules) {
        this.scheduledModules = scheduledModules;
    }
}
