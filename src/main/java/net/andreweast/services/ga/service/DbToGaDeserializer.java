package net.andreweast.services.ga.service;

import net.andreweast.exception.DataNotFoundException;
import net.andreweast.services.data.api.JobRepository;
import net.andreweast.services.data.api.LecturerTimeslotPreferenceRepository;
import net.andreweast.services.data.api.ModuleRepository;
import net.andreweast.services.data.api.ScheduleRepository;
import net.andreweast.services.data.api.ScheduledModuleRepository;
import net.andreweast.services.data.api.TimeslotRepository;
import net.andreweast.services.data.api.VenueRepository;
import net.andreweast.services.data.model.CourseModule;
import net.andreweast.services.data.model.DepartmentBuilding;
import net.andreweast.services.data.model.Job;
import net.andreweast.services.data.model.LecturerTimeslotPreference;
import net.andreweast.services.data.model.Schedule;
import net.andreweast.services.data.model.ScheduledModule;
import net.andreweast.services.ga.geneticalgorithm.Gene;
import net.andreweast.services.ga.geneticalgorithm.GeneticAlgorithmJobData;
import net.andreweast.services.ga.geneticalgorithm.Module;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private LecturerTimeslotPreferenceRepository lecturerTimeslotPreferenceRepository;

    /**
     * Build up all required data structures for the genetic algorithm by getting them from the database
     *
     * @param scheduleId The schedule_id to record to get from the database
     */
    public GeneticAlgorithmJobData generateGADataFromDatabase(Long scheduleId) throws DataNotFoundException {
        // Find Schedule in database
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(DataNotFoundException::new);

        GeneticAlgorithmJobData data = new GeneticAlgorithmJobData();

        // Get all timeslots, venues, and modules from database
        // They are not specific to this job (i.e. do not depend on database table "schedules")
        data.setTimeslots(generateTimeslotsFromDatabase());
        data.setModules(generateModulesFromDatabase());
        data.setVenues(generateVenuesFromDatabase());

        // Find existing ScheduledModules objects, which are the genes that make up each chromosomes in the GA
        if (schedule.getWip()) {
            // This job is a "work in progress", therefore scheduled_modules already exist
            data.setScheduledModules(generateScheduledModulesFromDatabase(schedule.getScheduleId(), data));
            data.setModifyExistingJob(true);

            if (data.getScheduledModules().size() == 0) {
                System.out.println("ERROR For preexisting schedule (id=" + scheduleId + "), there were no scheduled modules in the database"); // FUTURE: Logger error
                // FUTURE: To prevent jobs from not going forward until the GUI has more features, simply ignore this error
                // FUTURE: Also, see {@link Population}
                data.setModifyExistingJob(false); // FUTURE: Hack until future work can be done
            }
            if (data.getScheduledModules().size() != data.getModules().size()) {
                System.out.println("For preexisting schedule (id=" + scheduleId + "), the no. of scheduled modules in the database was " + data.getScheduledModules().size() +
                        ", but it was expected to equal the number of modules in the database (" + data.getModules().size() + ")"); // FUTURE: Logger warning
            }
        } else {
            // This job is a new job, therefore there are no scheduled_modules to fetch. They will be created when the job is done
            data.setModifyExistingJob(false);
        }

        return data;
    }

    private List<Timeslot> generateTimeslotsFromDatabase() {
        Iterable<net.andreweast.services.data.model.Timeslot> entities = timeslotRepository.findAll();

        // Translate all those timeslots into GA Timeslots objects
        List<Timeslot> timeslots = new ArrayList<>();
        for (net.andreweast.services.data.model.Timeslot entity : entities) {
            // Get preferences of all lecturers for this timeslot
            List<LecturerTimeslotPreference> preferences = lecturerTimeslotPreferenceRepository.findByTimeslot_TimeslotId(entity.getTimeslotId());
            HashMap<Long, Integer> mapPreferencesByLecturerId = new HashMap<>();
            for (LecturerTimeslotPreference pref : preferences) {
                mapPreferencesByLecturerId.put(pref.getLecturer().getLecturerId(), pref.getRank());
            }

            timeslots.add(new Timeslot(entity.getTimeslotId(), entity.getDay(), entity.getTime(), mapPreferencesByLecturerId));
        }

        return timeslots;
    }

    private List<Venue> generateVenuesFromDatabase() {
        Iterable<net.andreweast.services.data.model.Venue> entities = venueRepository.findAll();

        // Translate all those venues into GA Venue objects
        List<Venue> venues = new ArrayList<>();
        for (net.andreweast.services.data.model.Venue entity : entities) {
            // Any departments which have provided a score to the building this venue is in, find all those scores
            HashMap<Long, Integer> departmentsScores = new HashMap<>();
            for (DepartmentBuilding deptScoreForBuilding : entity.getBuilding().getDepartmentBuildings()) {
                departmentsScores.put(deptScoreForBuilding.getDepartment().getDepartmentId(),
                        deptScoreForBuilding.getScore());
            }

            venues.add(new Venue(entity.getVenueId(), entity.getName(), entity.getLab(), entity.getCapacity(),
                    entity.getBuilding().getLocation().x, entity.getBuilding().getLocation().y, departmentsScores));
        }

        return venues;
    }

    private List<Module> generateModulesFromDatabase() {
        Iterable<net.andreweast.services.data.model.Module> entities = moduleRepository.findAll();

        // Translate all those modules into GA Module objects
        int totalEnrolled;
        List<Module> modules = new ArrayList<>();
//        // DEBUG: LIMIT TO 3 MODULES ONLY
//        // DEBUG: disabled the following line:
        for (net.andreweast.services.data.model.Module entity : entities) {
//        // DEBUG: Makes the job VERY QUICK
//        Iterator<net.andreweast.services.data.model.Module> itr = entities.iterator(); // DEBUG
//        for (int i = 0; i < 5; ++i) { // DEBUG
//            net.andreweast.services.data.model.Module entity; // DEBUG
//            if (itr.hasNext()) { // DEBUG
//                entity = itr.next(); // DEBUG
//            } else {   // DEBUG
//                break; // DEBUG
//            }          // DEBUG
            HashSet<Long> coursesOfferingModule = new HashSet<>();
            Set<Long> departmentIdsOfferingModule = new HashSet<>();

            // Sum size of all courses which are offering this module, get courses
            totalEnrolled = 0;
            for (CourseModule course : entity.getCourseModules()) {
                totalEnrolled += course.getCourse().getNumEnrolled(); // FUTURE: This could be a COMPOSITE query in SQL
                coursesOfferingModule.add(course.getId().getCourseId());
                departmentIdsOfferingModule.add(course.getCourse().getDepartment().getDepartmentId());
            }

            modules.add(new Module(entity.getModuleId(), entity.getName(), totalEnrolled, entity.getLab(),
                    entity.getLecturer().getLecturerId(), coursesOfferingModule, departmentIdsOfferingModule));
        }

        return modules;
    }

    private List<Gene> generateScheduledModulesFromDatabase(Long scheduleId, GeneticAlgorithmJobData data) {
        List<ScheduledModule> entities = scheduledModuleRepository.findBySchedule_ScheduleId_OrderByTimeslot_TimeslotIdAsc(scheduleId);

        // Build a set of indexes of already-found modules, venues, and timeslots s.t. creating each new Gene won't be a polynomial operation (n^3 at least)
        // Map creation from Stream is based on method from: https://stackoverflow.com/a/20363874/5271224
        Map<Long, Module> moduleIndex = data.getModules().stream().collect(Collectors.toMap(Module::getId, module -> module));
        Map<Long, Venue> venueIndex = data.getVenues().stream().collect(Collectors.toMap(Venue::getId, venue -> venue));
        Map<Long, Timeslot> timeslotIndex = data.getTimeslots().stream().collect(Collectors.toMap(Timeslot::getId, timeslot -> timeslot));

        List<Gene> scheduledModules = new ArrayList<>();
        for (ScheduledModule entity : entities) {
            scheduledModules.add(new Gene(
                    moduleIndex.get(entity.getModule().getModuleId()),
                    venueIndex.get(entity.getVenue().getVenueId()),
                    timeslotIndex.get(entity.getTimeslot().getTimeslotId()),
                    data));
        }

        return scheduledModules;
    }

    public Job createJobForSchedule(Long scheduleId, int numGenerations) throws DataNotFoundException, ResponseStatusException {
        // Find Schedule in database
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(DataNotFoundException::new);

        // Make sure this schedule doesn't have a currently dispatched job
        if (schedule.getJob() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A Job for that Schedule has already been started"); // See: https://www.baeldung.com/exception-handling-for-rest-with-spring
        }

        // Create Job entity
        Job job = new Job();
        job.setStartDate(new Timestamp(new Date().getTime())); // Timestamp to now
        job.setTotalGenerations(numGenerations);
        // current_generation is being left as NULL (until the job has already been running)
        job.setSchedule(schedule);
        jobRepository.save(job);

        // Notify database that this Schedule has a Job running
        schedule.setJob(job);
        scheduleRepository.save(schedule);

        return job;
    }
}
