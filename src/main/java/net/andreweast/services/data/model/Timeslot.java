package net.andreweast.services.data.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "timeslots", schema = "public", catalog = "ga_dev")
public class Timeslot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timeslot_generator")
    @SequenceGenerator(name = "timeslot_generator", sequenceName = "timeslot_id_sequence", allocationSize = 1)
    @Column(name = "timeslot_id", updatable = false, nullable = false)
    private Long timeslotId;

    /**
     * Values:
     * 0,1,2,3,4,5,6
     * Maps to:
     * Mon,Tues,Wed,Thur,Fri,Sat,Sun
     */
    @Basic
    @Column(name = "day", nullable = false)
    private Integer day;

    /**
     * 8,9,10,11,12,13,14,15,16,17,18 (most common)
     */
    @Basic
    @Column(name = "time", nullable = false)
    private Integer time;

    @OneToMany(mappedBy = "timeslot")
    private List<ScheduledModule> scheduledModules;

    @OneToMany(mappedBy = "timeslot")
    private List<LecturerTimeslotPreference> lecturerTimeslotPreferences;

    public Long getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(Long timeslotId) {
        this.timeslotId = timeslotId;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public List<ScheduledModule> getScheduledModules() {
        return scheduledModules;
    }

    public void setScheduledModules(List<ScheduledModule> scheduledModules) {
        this.scheduledModules = scheduledModules;
    }

    public List<LecturerTimeslotPreference> getLecturerTimeslotPreferences() {
        return lecturerTimeslotPreferences;
    }

    public void setLecturerTimeslotPreferences(List<LecturerTimeslotPreference> lecturerTimeslotPreferences) {
        this.lecturerTimeslotPreferences = lecturerTimeslotPreferences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timeslot timeslot = (Timeslot) o;
        return timeslotId.equals(timeslot.timeslotId) &&
                day.equals(timeslot.day) &&
                time.equals(timeslot.time) &&
                Objects.equals(scheduledModules, timeslot.scheduledModules) &&
                Objects.equals(lecturerTimeslotPreferences, timeslot.lecturerTimeslotPreferences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeslotId, day, time, scheduledModules, lecturerTimeslotPreferences);
    }
}
