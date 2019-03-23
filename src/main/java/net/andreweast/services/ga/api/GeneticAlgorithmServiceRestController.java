package net.andreweast.services.ga.api;

import net.andreweast.services.data.model.Job;
import net.andreweast.services.data.model.JobDto;
import net.andreweast.services.ga.service.Dispatcher;
import net.andreweast.services.ga.service.GaToDbSerializer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/genetic-algorithm-api")
// Does not run from /api/genetic-algorithm-api, but rather just from /genetic-algorithm-api
// Because this Controller isn't included with the data REST Repositories, which use application.properties config: spring.data.rest.base-path=/api
public class GeneticAlgorithmServiceRestController {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    Dispatcher dispatcher;

    // TODO: Where should this be determined? Some sort of public static constant in the GA controller?
    // TODO: Perhaps in a configuration text file
    // TODO: How many generations to run?
    private static final String NUM_GENERATIONS = "20"; // DEBUG!!

    /**
     * Start a genetic algorithm batch job running, using an existing Schedule (which may or may not be a work-in-progress)
     *
     * @param scheduleId Primary key of an existing record in the Schedules Table
     * @return Data on the new job, and an HTTP 202 Accepted (which comes from the @ResponseStatus annotation)
     */
    @PutMapping("/job/{scheduleId}")
    @ResponseStatus(HttpStatus.ACCEPTED) // Why ACCEPTED? Processing isn't complete, but this HTTP transaction is closed. Perfect! See: https://httpstatuses.com/202
    public JobDto createJob(@PathVariable Long scheduleId,
                            @RequestParam(required = false, defaultValue = NUM_GENERATIONS) Integer numGenerations) {
        System.out.println("Creating a GA job from schedule, id=" + scheduleId); // FUTURE: Logger info

        // Dispatch the job. After getting data from database, and creating a new record in the Job table,
        // the dispatcher will spawn its own thread (so that this method (and API call) can return)
        Job job = dispatcher.dispatchNewJobForSchedule(scheduleId, numGenerations);

        return modelMapper.map(job, JobDto.class);
    }

    // DEBUG: Used for my temporary cleanup method
    @Autowired
    GaToDbSerializer gaToDbSerializer;
    // DEBUG: A temporary method to clean up the database faster
    @DeleteMapping("/failed-job/{scheduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cleanUpDatabaseAfterJobFailed(@PathVariable Long scheduleId) {
        gaToDbSerializer.deleteJobForSchedule(scheduleId);
    }

    @GetMapping("/job/{jobId}")
    @ResponseStatus(HttpStatus.OK)
    public JobDto checkStatusOfJob(@PathVariable Long jobId) {
        Job job = dispatcher.getStatusForJob(jobId);
        return modelMapper.map(job, JobDto.class);
    }

    @DeleteMapping("/job/{jobId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void stopJob(@PathVariable Long jobId) {
        dispatcher.stopJob(jobId);
    }
}
