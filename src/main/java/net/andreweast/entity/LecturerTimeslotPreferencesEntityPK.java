package net.andreweast.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class LecturerTimeslotPreferencesEntityPK implements Serializable {
    private int lecturerId;
    private int timeslotId;

    @Column(name = "lecturer_id", nullable = false)
    @Id
    public int getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(int lecturerId) {
        this.lecturerId = lecturerId;
    }

    @Column(name = "timeslot_id", nullable = false)
    @Id
    public int getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(int timeslotId) {
        this.timeslotId = timeslotId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LecturerTimeslotPreferencesEntityPK that = (LecturerTimeslotPreferencesEntityPK) o;
        return lecturerId == that.lecturerId &&
                timeslotId == that.timeslotId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lecturerId, timeslotId);
    }
}
