package net.andreweast.services.ga.api;

import net.andreweast.exception.DataNotFoundException;
import net.andreweast.services.data.api.JobRepository;
import net.andreweast.services.data.api.ScheduleRepository;
import net.andreweast.services.data.model.Job;
import net.andreweast.services.data.model.JobDto;
import net.andreweast.services.data.model.Schedule;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.Date;

@RestController
@RequestMapping("/genetic-algorithm-api")
// Does not run from /api/genetic-algorithm-api, but rather just from /genetic-algorithm-api
// Because this Controller isn't included with the data REST Repositories, which use application.properties config: spring.data.rest.base-path=/api
public class GeneticAlgorithmServiceRestController {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    ModelMapper modelMapper;

    /**
     * Start a genetic algorithm job running, using an existing Schedule
     *
     * @param scheduleId Primary key of an existing record in the Schedules Table
     * @return Data on the new job, and an HTTP 202 Accepted (which comes from the @ResponseStatus annotation)
     */
    @PutMapping("/job/{scheduleId}")
    @ResponseStatus(HttpStatus.ACCEPTED) // Why ACCEPTED? Processing isn't complete, but this HTTP transaction is closed. Perfect! See: https://httpstatuses.com/202
    public JobDto createScheduleBatchJob(@PathVariable Long scheduleId) {
        System.out.println("Hey, let's start a scheduling job! Created from schedule with ID: " + scheduleId); // DEBUG

        // Find Schedule in database
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(DataNotFoundException::new);

//        System.out.println(schedule);

        // Make sure this schedule doesn't have a currently dispatched job
        if (schedule.getJob() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Job has already been started"); // See: https://www.baeldung.com/exception-handling-for-rest-with-spring
        }

        // Create job
        Job job = new Job();
        job.setStartDate(new Timestamp(new Date().getTime())); // Timestamp to now
        job.setSchedule(schedule);
        jobRepository.save(job);

        // Notify database that schedule has been started
        schedule.setJob(job);
        scheduleRepository.save(schedule);

        // Kick off the job in its own thread (so that this method (and API call) can return)
        new Thread(() -> {
            try {
                // TODO: actually run a scheduler...
                Thread.sleep(10 * 1000);

                schedule.setGeneticAlgorithmRunning(false);
                scheduleRepository.save(schedule);
                System.out.println("Job's done, m'lord!"); // DEBUG
            } catch (InterruptedException e) {
                // There's no way to deal this anymore...the REST call has already returned!
                e.printStackTrace();
            }
        }).start();

        return modelMapper.map(job, JobDto.class);
    }

    @GetMapping("/job/{jobId}")
    @ResponseStatus(HttpStatus.OK)
    public JobDto checkStatusOfJob(@PathVariable Long jobId) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Checking for job status not implemented", new UnsupportedOperationException());
    }

    @DeleteMapping("/job/{jobId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JobDto stopJob(@PathVariable Long jobId) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Deleting in-progress job not implemented", new UnsupportedOperationException());
    }
}
