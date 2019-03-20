package net.andreweast.api;

import net.andreweast.geneticalgorithm.Population;
import net.andreweast.geneticalgorithm.Scheduler;
import net.andreweast.model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/scheduler") // TODO: this RestController doesn't get configured to /api/* with the application.properties config: spring.data.rest.base-path=/api
@RequestMapping("/api/scheduler")
public class SchedulerRestController {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED) // Processing isn't complete, but this HTTP transaction is closed. Perfect! See: https://httpstatuses.com/202
    public JobIdJson createScheduleBatchJob(UserJson creator) {
        System.out.println("Hey, let's start a scheduling job! It's for user: " + creator.getCreatorId());

        StringBuilder out = new StringBuilder();
        Iterable<Schedule> all = scheduleRepository.findAll();
        for (Schedule s : all) {
            out.append(s).append("****");
        }

        Schedule schedule = scheduleRepository.findById(2L).orElseThrow(RuntimeException::new); // TODO: Right HTTP status
        if (schedule.getGeneticAlgorithmRunning()) {
            // TODO: throw the right kind of HTTP status
            throw new RuntimeException("This scheduler job is already running");
            // DEBUG: THIS DOES NOT ACTUALLY BREAK THE REST, i.e. still returns HTTP 202 ACCEPTED
        }
        schedule.setGeneticAlgorithmRunning(true);
        scheduleRepository.save(schedule);

        Long jobId = schedule.getScheduleId();
        return new JobIdJson(jobId, out.toString());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK) // Processing isn't complete, but this HTTP transaction is closed. Perfect! See: https://httpstatuses.com/202
    public Population createScheduleDEBUG() {
        System.out.println("Hey, let's run a simple scheduling job!");

        return new Scheduler().schedule();
    }
}

// Will be marshalled by Jackson into JSON format: https://github.com/FasterXML/jackson
class JobIdJson {
    private final Long scheduleJobId;
    private final String comment;

    JobIdJson(long scheduleJobId, String comment) {
        this.scheduleJobId = scheduleJobId;
        this.comment = comment;
    }

    public Long getScheduleJobId() {
        return scheduleJobId;
    }

    public String getComment() {
        return comment;
    }
}

// Will be marshalled by Jackson into JSON format: https://github.com/FasterXML/jackson
class UserJson {
    private final Long creatorId;

    public UserJson() {
        // DEBUG: This should be an error. Happens when calling the rest endpoint w/o any BODY
        this.creatorId = null;
    }

    UserJson(long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getCreatorId() {
        return creatorId;
    }
}
