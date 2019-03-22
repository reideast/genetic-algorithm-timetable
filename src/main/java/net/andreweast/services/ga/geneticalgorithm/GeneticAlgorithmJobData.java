package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

/**
 * This is the MASTER object: The {@link net.andreweast.services.ga.service.Dispatcher} will create one of these, (via a {@link net.andreweast.services.ga.service.DbToGaDeserializer}
 * and then pass it to the actual GA runner to define ALL properties of the job
 *
 * Contains every piece of information (as component collections) that a GA job needs to create a schedule
 * Essentially, a very complex POJO
 */
public class GeneticAlgorithmJobData implements Serializable {
    // Database keys for this job
    private long scheduleId;
    private long jobId;

    // Properties of this job
    // Are we making a brand new schedule or are we based on an existing schedule?
    private boolean isModifyExistingJob;
    // How many generations maximum to run
    private int numGenerations;

    // The various things to be scheduled. Each one may have data that the Fitness Function will utilise
    private List<Timeslot> timeslots;
    private List<Module> modules;
    private List<Venue> venues;

    // The results: A set of modules, each placed in a timeslot.
    // If this is not an "modify existing job", then this collection will be NULL
    // Either way, at the end of the job, it will be filled up with the results
    // TODO: fill it up wiht results at end
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

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
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

    public int getNumGenerations() {
        return numGenerations;
    }

    public void setNumGenerations(int numGenerations) {
        this.numGenerations = numGenerations;
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
