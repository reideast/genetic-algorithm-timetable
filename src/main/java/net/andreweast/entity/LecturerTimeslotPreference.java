package net.andreweast.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "lecturer_timeslot_preferences", schema = "public", catalog = "ga_dev")
@IdClass(LecturerTimeslotPreferencePK.class)
public class LecturerTimeslotPreference {
    private int lecturerId;
    private int timeslotId;
    private Integer rank;

    @Id
    @Column(name = "lecturer_id", nullable = false)
    public int getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(int lecturerId) {
        this.lecturerId = lecturerId;
    }

    @Id
    @Column(name = "timeslot_id", nullable = false)
    public int getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(int timeslotId) {
        this.timeslotId = timeslotId;
    }

    @Basic
    @Column(name = "rank", nullable = true)
    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LecturerTimeslotPreference that = (LecturerTimeslotPreference) o;
        return lecturerId == that.lecturerId &&
                timeslotId == that.timeslotId &&
                Objects.equals(rank, that.rank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lecturerId, timeslotId, rank);
    }
}
