package net.andreweast.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/scheduler") // TODO: this RestController doesn't get configured to /api/* with the application.properties config: spring.data.rest.base-path=/api
@RequestMapping("/api/scheduler")
public class SchedulerRestController {
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED) // Processing isn't complete, but this HTTP transaction is closed. Perfect! See: https://httpstatuses.com/202
    public JobIdJson createScheduleBatchJob(UserJson creator) {
        System.out.println("Hey, let's start a scheduling job! It's for user: " + creator.getCreatorId());
        int jobId = 1234;
        return new JobIdJson(jobId);
    }
}

// Will be marshalled by Jackson into JSON format: https://github.com/FasterXML/jackson
class JobIdJson {
    private final long scheduleJobId;

    JobIdJson(long scheduleJobId) {
        this.scheduleJobId = scheduleJobId;
    }

    public long getScheduleJobId() {
        return scheduleJobId;
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
