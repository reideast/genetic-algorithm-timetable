package net.andreweast.model;

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

    // TODO: See: https://www.baeldung.com/hibernate-date-time
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

    @Basic
    @Column(name = "is_genetic_algorithm_running", nullable = true)
    private Boolean isGeneticAlgorithmRunning = false;

    /**
     * isMaster: The current master schedule, accepted and published by the Facilities Department, from which all other schedules should be based.
     */
    @Basic
    @Column(name = "is_master", nullable = true)
    private Boolean isMaster = false;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @OneToMany(mappedBy = "schedule")
    private List<ScheduledModule> scheduledModules;

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
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

    public Boolean getGeneticAlgorithmRunning() {
        return isGeneticAlgorithmRunning;
    }

    public void setGeneticAlgorithmRunning(Boolean geneticAlgorithmRunning) {
        isGeneticAlgorithmRunning = geneticAlgorithmRunning;
    }

    public Boolean getMaster() {
        return isMaster;
    }

    public void setMaster(Boolean master) {
        isMaster = master;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return scheduleId.equals(schedule.scheduleId) &&
                Objects.equals(creationDate, schedule.creationDate) &&
                Objects.equals(isWip, schedule.isWip) &&
                Objects.equals(isAccepted, schedule.isAccepted) &&
                Objects.equals(isGeneticAlgorithmRunning, schedule.isGeneticAlgorithmRunning) &&
                Objects.equals(isMaster, schedule.isMaster) &&
                Objects.equals(creator, schedule.creator) &&
                Objects.equals(scheduledModules, schedule.scheduledModules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheduleId, creationDate, isWip, isAccepted, isGeneticAlgorithmRunning, isMaster, creator, scheduledModules);
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId=" + scheduleId +
                ", creationDate=" + creationDate +
                ", isWip=" + isWip +
                ", isAccepted=" + isAccepted +
                ", isGeneticAlgorithmRunning=" + isGeneticAlgorithmRunning +
                ", isMaster=" + isMaster +
                ", creator=" + creator +
                ", scheduledModules=" + scheduledModules +
                '}';
    }
}
