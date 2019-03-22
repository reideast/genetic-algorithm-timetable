package net.andreweast.services.data.model;

import java.sql.Timestamp;

// DTO method to expose @Entity to outside service, over REST. See: https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
public class JobDto {
    private Long jobId;
    private Timestamp startDate;
    private Integer totalGenerations;
    private Integer currentGeneration;

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
}
