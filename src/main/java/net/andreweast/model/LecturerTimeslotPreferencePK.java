package net.andreweast.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LecturerTimeslotPreferencePK implements Serializable {
    @Column(name = "lecturer_id", nullable = false)
    private Long lecturerId;

    @Column(name = "timeslot_id", nullable = false)
    private Long timeslotId;

    public Long getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(Long lecturerId) {
        this.lecturerId = lecturerId;
    }

    public Long getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(Long timeslotId) {
        this.timeslotId = timeslotId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LecturerTimeslotPreferencePK that = (LecturerTimeslotPreferencePK) o;
        return lecturerId.equals(that.lecturerId) &&
                timeslotId.equals(that.timeslotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lecturerId, timeslotId);
    }
}
