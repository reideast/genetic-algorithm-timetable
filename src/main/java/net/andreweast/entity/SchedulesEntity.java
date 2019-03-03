package net.andreweast.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "schedules", schema = "public", catalog = "ga_dev")
public class SchedulesEntity {
    private int scheduleId;
    private Timestamp creationDate;
    private Boolean isWip;
    private Boolean isAccepted;
    private Boolean isGeneticAlgorithmRunning;
    private Boolean isMaster;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schedule_generator")
    @SequenceGenerator(name="schedule_generator", sequenceName = "schedule_id_sequence")
    @Column(name = "schedule_id", updatable = false, nullable = false)
    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Basic
    @Column(name = "creation_date", nullable = true)
    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    @Basic
    @Column(name = "is_wip", nullable = true)
    public Boolean getWip() {
        return isWip;
    }

    public void setWip(Boolean wip) {
        isWip = wip;
    }

    @Basic
    @Column(name = "is_accepted", nullable = true)
    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    @Basic
    @Column(name = "is_genetic_algorithm_running", nullable = true)
    public Boolean getGeneticAlgorithmRunning() {
        return isGeneticAlgorithmRunning;
    }

    public void setGeneticAlgorithmRunning(Boolean geneticAlgorithmRunning) {
        isGeneticAlgorithmRunning = geneticAlgorithmRunning;
    }

    @Basic
    @Column(name = "is_master", nullable = true)
    public Boolean getMaster() {
        return isMaster;
    }

    public void setMaster(Boolean master) {
        isMaster = master;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchedulesEntity that = (SchedulesEntity) o;
        return scheduleId == that.scheduleId &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(isWip, that.isWip) &&
                Objects.equals(isAccepted, that.isAccepted) &&
                Objects.equals(isGeneticAlgorithmRunning, that.isGeneticAlgorithmRunning) &&
                Objects.equals(isMaster, that.isMaster);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheduleId, creationDate, isWip, isAccepted, isGeneticAlgorithmRunning, isMaster);
    }
}
