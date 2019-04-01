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
    // How many individuals in the population
    private int populationSize;
    // How big an Individual will be (e.g. how many modules there are to schedule)
    private int chromosomeSize;

    // How many "extra" generations to run after a valid (no violated hard constraints) solution has emerged
    private float proportionRunDownGenerations;

    // Crossover with p = 0.6
    private float crossoverProbability;

    // Will each individual mutate? Each one has will spawn a mutated clone with p = 0.9
    private float mutateProbability;
    // How many genes can be mutated within a chromosome if it is mutated: between 1..mutatedGenesMax
    private int mutatedGenesMax;

    // How many of the very best in a population are guaranteed to survive
    private int numEliteSurvivors;

    // How often to send reports back to the database, in percentage of job done
    // This is important for the frontend, since it is how often the status progress bar will update
    private float queryRate;

    // The various things to be scheduled. Each one may have data that the Fitness Function will utilise
    private List<Module> modules;
    private List<Venue> venues;
    private List<Timeslot> timeslots;

    // The results: A set of modules, each placed in a timeslot.
    // If this is not an "modify existing job", then this collection will start as NULL
    // Either way, at the END of the job, it will be filled up with the results
    private List<ScheduledModule> scheduledModules;

    // Does this job's data represent a schedule with no hard constraints violated?
    // Will be set and read DURING the job
    private boolean hasValidSolution;

    private static final Random random = new Random();

    public Module getRandomModule() {
        return modules.get(random.nextInt(modules.size()));
    }

    public Module getIndexedModule(int index) {
        return modules.get(index);
    }

    public Venue getRandomVenue() {
        return venues.get(random.nextInt(venues.size()));
    }

    public Venue getIndexedVenue(int index) {
        return venues.get(index);
    }

    public Timeslot getRandomTimeslot() {
        return timeslots.get(random.nextInt(timeslots.size()));
    }

    public Timeslot getIndexedTimeslot(int index) {
        return timeslots.get(index);
    }

    public int getChromosomeSize() {
        return chromosomeSize;
    }

    public boolean isHasValidSolution() {
        return hasValidSolution;
    }

    public void setHasValidSolution(boolean hasValidSolution) {
        this.hasValidSolution = hasValidSolution;
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

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public float getProportionRunDownGenerations() {
        return proportionRunDownGenerations;
    }

    public void setProportionRunDownGenerations(float proportionRunDownGenerations) {
        this.proportionRunDownGenerations = proportionRunDownGenerations;
    }

    public float getCrossoverProbability() {
        return crossoverProbability;
    }

    public void setCrossoverProbability(float crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    public float getMutateProbability() {
        return mutateProbability;
    }

    public void setMutateProbability(float mutateProbability) {
        this.mutateProbability = mutateProbability;
    }

    public int getMutatedGenesMax() {
        return mutatedGenesMax;
    }

    public void setMutatedGenesMax(int mutatedGenesMax) {
        this.mutatedGenesMax = mutatedGenesMax;
    }

    public int getNumEliteSurvivors() {
        return numEliteSurvivors;
    }

    public void setNumEliteSurvivors(int numEliteSurvivors) {
        this.numEliteSurvivors = numEliteSurvivors;
    }

    public float getQueryRate() {
        return queryRate;
    }

    public void setQueryRate(float queryRate) {
        this.queryRate = queryRate;
    }

    // Potential source of error: If this.modules is retrieved, then items are added/removed,
    // then this.chromosomeSize will be incorrect.
    // There is minimal risk of this, since this.modules will be set when being first read from the
    // database, then it should never again be used
    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
        this.chromosomeSize = this.modules.size();
    }

    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

    public List<Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<Timeslot> timeslots) {
        this.timeslots = timeslots;
    }

    public List<ScheduledModule> getScheduledModules() {
        return scheduledModules;
    }

    public void setScheduledModules(List<ScheduledModule> scheduledModules) {
        this.scheduledModules = scheduledModules;
    }
}
