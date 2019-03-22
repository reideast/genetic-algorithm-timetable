package net.andreweast.services.data.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "jobs", schema = "public", catalog = "ga_dev")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_generator")
    @SequenceGenerator(name = "job_generator", sequenceName = "job_id_seq", allocationSize = 1)
    @Column(name = "job_id", updatable = false, nullable = false)
    private Long jobId;

    // FUTURE: Does SQL Timestamp type need any other work? See: https://www.baeldung.com/hibernate-date-time
    @Basic
    @Column(name = "start_date", nullable = true)
    private Timestamp startDate = new Timestamp(new Date().getTime()); // Default to timestamp of Now

    @Basic
    @Column(name = "total_generations", nullable = true)
    private Integer totalGenerations;

    @Basic
    @Column(name = "current_generation", nullable = true)
    private Integer currentGeneration;

    @Basic
    @Column(name = "last_status_update_time", nullable = true)
    private Timestamp lastStatusUpdateTime;

    @OneToOne(mappedBy = "job")
    private Schedule schedule;

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

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return jobId.equals(job.jobId) &&
                Objects.equals(startDate, job.startDate) &&
                Objects.equals(totalGenerations, job.totalGenerations) &&
                Objects.equals(currentGeneration, job.currentGeneration) &&
                Objects.equals(lastStatusUpdateTime, job.lastStatusUpdateTime) &&
                Objects.equals(schedule, job.schedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, startDate, totalGenerations, currentGeneration, lastStatusUpdateTime, schedule);
    }
}
