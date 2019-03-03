package net.andreweast.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "timeslots", schema = "public", catalog = "ga_dev")
public class TimeslotsEntity {
    private int timeslotId;
    private String day;
    private int time;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timeslot_generator")
    @SequenceGenerator(name="timeslot_generator", sequenceName = "timeslot_id_sequence", allocationSize = 1)
    @Column(name = "timeslot_id", updatable = false, nullable = false)
    public int getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(int timeslotId) {
        this.timeslotId = timeslotId;
    }

    @Basic
    @Column(name = "day", nullable = false, length = -1)
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Basic
    @Column(name = "time", nullable = false)
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeslotsEntity that = (TimeslotsEntity) o;
        return timeslotId == that.timeslotId &&
                time == that.time &&
                Objects.equals(day, that.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeslotId, day, time);
    }
}
