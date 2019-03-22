package net.andreweast.services.ga.service;

import net.andreweast.exception.DataNotFoundException;
import net.andreweast.services.data.api.JobRepository;
import net.andreweast.services.data.api.ModuleRepository;
import net.andreweast.services.data.api.ScheduleRepository;
import net.andreweast.services.data.api.ScheduledModuleRepository;
import net.andreweast.services.data.api.TimeslotRepository;
import net.andreweast.services.data.api.VenueRepository;
import net.andreweast.services.data.model.CourseModule;
import net.andreweast.services.data.model.Job;
import net.andreweast.services.data.model.Schedule;
import net.andreweast.services.ga.geneticalgorithm.GeneticAlgorithmJobData;
import net.andreweast.services.ga.geneticalgorithm.Module;
import net.andreweast.services.ga.geneticalgorithm.ScheduledModule;
import net.andreweast.services.ga.geneticalgorithm.Timeslot;
import net.andreweast.services.ga.geneticalgorithm.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Gets all information needed for a Genetic Algorithm run from the database
 * If this is a new scheduling job, also create the ScheduledModules objects for the job
 * Returns a single object that encapsulates all data for the GA
 */
@Service
public class DbToGaDeserializer {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private TimeslotRepository timeslotRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ScheduledModuleRepository scheduledModuleRepository;

    /**
     * Build up all required data structures for the genetic algorithm by getting them from the database
     *
     * @param scheduleId The schedule_id to record to get from the database
     */
    public GeneticAlgorithmJobData generateGAJobDataFromDatabase(Long scheduleId, Long jobId) throws DataNotFoundException {
        // Find Schedule in database
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(DataNotFoundException::new);

        GeneticAlgorithmJobData data = new GeneticAlgorithmJobData();

        data.setScheduleId(scheduleId);
        data.setJobId(jobId);

        // TODO: How many generations to run?
        data.setNumGenerations(20000); // DEBUG!!

        // Get all timeslots, venues, and modules from database
        // They are not specific to this job (i.e. do not depend on database table "schedules")
        data.setTimeslots(generateTimeslotsFromDatabase());
        data.setModules(generateModulesFromDatabase());
        data.setVenues(generateVenuesFromDatabase());

        // Find or make the ScheduledModules objects, which are the genes that make up each chromosomes in the GA
        if (schedule.getWip()) {
            // This job is a "work in progress", therefore scheduled_modules already exist
            data.setScheduledModules(generateScheduledModulesFromDatabase(schedule.getScheduleId(), data));
            data.setModifyExistingJob(true);

            if (data.getScheduledModules().size() != data.getModules().size()) {
                System.out.println("For preexisting schedule (id=" + scheduleId + "), the no. of scheduled modules in the databse was " + data.getScheduledModules().size() +
                        ", but it was expected to equal the number of modules in the database (" + data.getModules().size() + ")"); // TODO: Logger error
            }
        } else {
            // This job is a new job, therefore there are no scheduled_modules to fetch. They will be created when the job is done
            data.setModifyExistingJob(false);
        }

        return data;
    }

    private List<Timeslot> generateTimeslotsFromDatabase() {
        Iterable<net.andreweast.services.data.model.Timeslot> entities = timeslotRepository.findAll();

        List<Timeslot> timeslots = new ArrayList<>();
        for (net.andreweast.services.data.model.Timeslot entity : entities) {
            timeslots.add(new Timeslot(entity.getTimeslotId(), entity.getDay(), entity.getTime()));
        }

        // DEBUG:
        System.out.print("Timeslots: ");
        for (Timeslot item : timeslots) {
            System.out.print(item);
        }
        System.out.println();

        return timeslots;
    }

    private List<Venue> generateVenuesFromDatabase() {
        // TODO: this needs a custom query, with proper joins, in order to get all data in one go
        Iterable<net.andreweast.services.data.model.Venue> entities = venueRepository.findAll();

        List<Venue> venues = new ArrayList<>();
        for (net.andreweast.services.data.model.Venue entity : entities) {
            venues.add(new Venue(entity.getVenueId(), entity.getName(), entity.getLab(), entity.getCapacity(), entity.getBuilding().getLocation().x, entity.getBuilding().getLocation().y));
            // TODO: departmentScore is just -1. Need custom query
            // TODO: and even more detailed: need to get department_building.score's for ALL departments for that venue, and save them in a HashMap(dept_id -> score) in that venue
        }

        // DEBUG:
        System.out.println("Venues:");
        for (Venue item : venues) {
            System.out.println(item);
        }

        return venues;
    }

    private List<Module> generateModulesFromDatabase() {
        Iterable<net.andreweast.services.data.model.Module> entities = moduleRepository.findAll();

        List<Module> modules = new ArrayList<>();
        for (net.andreweast.services.data.model.Module entity : entities) {
            int totalEnrolled = 0;
            for (CourseModule courses : entity.getCourseModules()) {
                totalEnrolled += courses.getCourse().getNumEnrolled();
            }
            modules.add(new Module(entity.getModuleId(), entity.getName(), totalEnrolled));
        }

        // DEBUG:
        System.out.println("Modules:");
        for (Module item : modules) {
            System.out.println(item);
        }

        return modules;
    }

    private List<ScheduledModule> generateScheduledModulesFromDatabase(Long scheduleId, GeneticAlgorithmJobData data) {
//        List<net.andreweast.services.data.model.ScheduledModule> entities = scheduledModuleRepository.getAllBySchedule_ScheduleId(scheduleId);
        List<net.andreweast.services.data.model.ScheduledModule> entities = scheduledModuleRepository.findBySchedule_ScheduleId_OrderByTimeslot_TimeslotIdAsc(scheduleId);

        // Build a set of indexes of already-found modules, venues, and timeslots s.t. creating each new ScheduledModule won't be a polynomial operation (n^3 at least)
        HashMap<Long, Module> moduleIndex = new HashMap<>();
        for (Module module : data.getModules()) {
            moduleIndex.put(module.getId(), module);
        }
        HashMap<Long, Venue> venueIndex = new HashMap<>();
        for (Venue venue : data.getVenues()) {
            venueIndex.put(venue.getId(), venue);
        }
        HashMap<Long, Timeslot> timeslotIndex = new HashMap<>();
        for (Timeslot timeslot : data.getTimeslots()) {
            timeslotIndex.put(timeslot.getId(), timeslot);
        }

        List<ScheduledModule> scheduledModules = new ArrayList<>();
        for (net.andreweast.services.data.model.ScheduledModule entity : entities) {
            scheduledModules.add(new ScheduledModule(moduleIndex.get(entity.getModule().getModuleId()),
                    venueIndex.get(entity.getVenue().getVenueId()),
                    timeslotIndex.get(entity.getTimeslot().getTimeslotId())));
        }

        // DEBUG:
        System.out.println("Scheduled Modules:");
        for (ScheduledModule item : scheduledModules) {
            System.out.println(item);
        }

        return scheduledModules;
    }

    public Job createJobForSchedule(Long scheduleId) throws DataNotFoundException, ResponseStatusException {
        // Find Schedule in database
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(DataNotFoundException::new);

        // Make sure this schedule doesn't have a currently dispatched job
        if (schedule.getJob() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Job has already been started"); // See: https://www.baeldung.com/exception-handling-for-rest-with-spring
        }

        // Create job
        net.andreweast.services.data.model.Job job = new net.andreweast.services.data.model.Job();
        job.setStartDate(new Timestamp(new Date().getTime())); // Timestamp to now
        job.setSchedule(schedule);
        jobRepository.save(job);

        // Notify database that schedule has been started
        schedule.setJob(job);
        scheduleRepository.save(schedule);

        return job;
    }
}
