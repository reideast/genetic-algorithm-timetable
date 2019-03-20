package net.andreweast.geneticalgorithm.api;

import net.andreweast.api.JobRepository;
import net.andreweast.api.ScheduleRepository;
import net.andreweast.exception.DataNotFoundException;
import net.andreweast.geneticalgorithm.Population;
import net.andreweast.geneticalgorithm.Scheduler;
import net.andreweast.model.Job;
import net.andreweast.model.JobDto;
import net.andreweast.model.Schedule;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/genetic-algorithm-api")
// This Controller isn't grouped with the rest of the REST Repositories, which use application.properties config: spring.data.rest.base-path=/api
public class SchedulerRestController {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    ModelMapper modelMapper;

    /**
     * Start a genetic algorithm job running, using an existing Schedule
     *
     * @param scheduleId Primary key of an existing record in the Schedules Table
     */
    @PutMapping("/start-job/{scheduleId}")
    @ResponseStatus(HttpStatus.ACCEPTED) // Why ACCEPTED? Processing isn't complete, but this HTTP transaction is closed. Perfect! See: https://httpstatuses.com/202
    public JobDto createScheduleBatchJob(@PathVariable Long scheduleId) {
        System.out.println("Hey, let's start a scheduling job! Created from schedule with ID: " + scheduleId); // DEBUG

        // Find Schedule in database
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(DataNotFoundException::new);

        // Make sure this schedule doens't have a currently dispatched job
        if (schedule.getGeneticAlgorithmRunning()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Job has already been started"); // See: https://www.baeldung.com/exception-handling-for-rest-with-spring
        }

        // Notify database that schedule has been started
        schedule.setGeneticAlgorithmRunning(true);
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
                // There's not way to deal this anymore...the REST call has already returned!
                e.printStackTrace();
            }
        }).start();

        return modelMapper.map(job, JobDto.class);
    }

    @GetMapping("/dummy")
    @ResponseStatus(HttpStatus.OK) // Processing isn't complete, but this HTTP transaction is closed. Perfect! See: https://httpstatuses.com/202
    public Population createScheduleDEBUG() {
        System.out.println("Hey, let's run a simple scheduling job!");

        return new Scheduler().schedule();
    }
}
