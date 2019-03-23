package net.andreweast.services.data.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.sql.Timestamp;

/**
 * DTO method to expose @Entity to outside service, over REST.
 * See: https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
 * Building a HATEOAS JSON object: https://docs.spring.io/spring-hateoas/docs/current/reference/html/
 */
public class JobDto extends ResourceSupport {
    @JsonProperty("jobId")
    private Long jobId;
    @JsonProperty("startDate")
    private Timestamp startDate;
    @JsonProperty("totalGenerations")
    private Integer totalGenerations;
    @JsonProperty("currentGeneration")
    private Integer currentGeneration;
    @JsonProperty("lastStatusUpdateTime")
    private Timestamp lastStatusUpdateTime;

    @JsonCreator
    public JobDto(Job job) {
        jobId = job.getJobId();
        startDate = job.getStartDate();
        totalGenerations = job.getTotalGenerations();
        currentGeneration = job.getCurrentGeneration();
        lastStatusUpdateTime = job.getLastStatusUpdateTime();
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Integer getTotalGenerations() {
        return totalGenerations;
    }

    public void setTotalGenerations(Integer totalGenerations) {
        this.totalGenerations = totalGenerations;
    }

    public Integer getCurrentGeneration() {
        return currentGeneration;
    }

    public void setCurrentGeneration(Integer currentGeneration) {
        this.currentGeneration = currentGeneration;
    }

    public Timestamp getLastStatusUpdateTime() {
        return lastStatusUpdateTime;
    }

    public void setLastStatusUpdateTime(Timestamp lastStatusUpdateTime) {
        this.lastStatusUpdateTime = lastStatusUpdateTime;
    }
}
