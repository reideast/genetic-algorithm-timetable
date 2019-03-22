package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

/**
 * This is the MASTER object: The {@link net.andreweast.services.ga.service.Dispatcher} will create one of these, (via a {@link net.andreweast.services.ga.service.GeneticAlgorithmDeserializer}
 * and then pass it to the actual GA runner to define ALL properties of the job
 *
 * Contains every piece of information (as component collections) that a GA job needs to create a schedule
 * Essentially, a very complex POJO
 */
public class GAJobData implements Serializable {
    private long jobId;

    private boolean isModifyExistingJob;

    private List<Timeslot> timeslots;
    private List<Module> modules;
    private List<Venue> venues;

    private List<ScheduledModule> scheduledModules;

    private static final Random random = new Random();

    public Timeslot getRandomTimeslot() {
        return timeslots.get(random.nextInt(timeslots.size()));
    }

    public Module getRandomModule() {
        return modules.get(random.nextInt(modules.size()));
    }

    public Venue getRandomVenue() {
        return venues.get(random.nextInt(venues.size()));
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public boolean isModifyExistingJob() {
        return isModifyExistingJob;
    }

    public void setModifyExistingJob(boolean modifyExistingJob) {
        isModifyExistingJob = modifyExistingJob;
    }

    public List<Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<Timeslot> timeslots) {
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
