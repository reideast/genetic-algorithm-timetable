package net.andreweast.services.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "schedules", schema = "public", catalog = "ga_dev")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schedule_generator")
    @SequenceGenerator(name = "schedule_generator", sequenceName = "schedule_id_sequence", allocationSize = 1)
    @Column(name = "schedule_id", updatable = false, nullable = false)
    private Long scheduleId;

    @Version
    @JsonIgnore
    private Long version;

    // FUTURE: Does SQL Timestamp type need any other work? See: https://www.baeldung.com/hibernate-date-time
    @Basic
    @Column(name = "creation_date", nullable = true)
    private Timestamp creationDate = new Timestamp(new Date().getTime()); // Default to timestamp of Now

    /**
     * isWip: WIP is for Work-in-Progress
     * If true, this Schedule is the linked user's current schedule they are working on
     */
    @Basic
    @Column(name = "is_wip", nullable = true)
    private Boolean isWip = false;

    /**
     * isAccepted: This schedule has been approved by appropriate department persons, and is ready to be submitted to Facilities as a revision
     */
    @Basic
    @Column(name = "is_accepted", nullable = true)
    private Boolean isAccepted = false;

    /**
     * isMaster: The current master schedule, accepted and published by the Facilities Department, from which all other schedules should be based.
     */
    @Basic
    @Column(name = "is_master", nullable = true)
    private Boolean isMaster = false;

    @Basic
    @Column(name = "fitness", nullable = true)
    private Long fitness;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @OneToMany(mappedBy = "schedule")
    private List<ScheduledModule> scheduledModules;

    @OneToOne
    @JoinColumn(name = "job_id")
    private Job job;

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getWip() {
        return isWip;
    }

    public void setWip(Boolean wip) {
        isWip = wip;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    public Boolean getMaster() {
        return isMaster;
    }

    public void setMaster(Boolean master) {
        isMaster = master;
    }

    public Long getFitness() {
        return fitness;
    }

    public void setFitness(Long fitness) {
        this.fitness = fitness;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<ScheduledModule> getScheduledModules() {
        return scheduledModules;
    }

    public void setScheduledModules(List<ScheduledModule> scheduledModules) {
        this.scheduledModules = scheduledModules;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return scheduleId.equals(schedule.scheduleId) &&
                version.equals(schedule.version) &&
                Objects.equals(creationDate, schedule.creationDate) &&
                Objects.equals(isWip, schedule.isWip) &&
                Objects.equals(isAccepted, schedule.isAccepted) &&
                Objects.equals(isMaster, schedule.isMaster) &&
                Objects.equals(fitness, schedule.fitness) &&
                Objects.equals(creator, schedule.creator) &&
                Objects.equals(scheduledModules, schedule.scheduledModules) &&
                Objects.equals(job, schedule.job);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheduleId, version, creationDate, isWip, isAccepted, isMaster, fitness, creator, scheduledModules, job);
    }
}
