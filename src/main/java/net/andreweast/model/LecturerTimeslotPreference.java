package net.andreweast.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "lecturer_timeslot_preferences", schema = "public", catalog = "ga_dev")
public class LecturerTimeslotPreference {
    @EmbeddedId
    private LecturerTimeslotPreferencePK id;

    @ManyToOne
    @MapsId("lecturer_id")
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

    @ManyToOne
    @MapsId("timeslot_id")
    @JoinColumn(name = "timeslot_id")
    private Timeslot timeslot;

    @Basic
    @Column(name = "rank", nullable = true)
    private Integer rank;

    public LecturerTimeslotPreferencePK getId() {
        return id;
    }

    public void setId(LecturerTimeslotPreferencePK id) {
        this.id = id;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

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
        return id.equals(that.id) &&
                lecturer.equals(that.lecturer) &&
                timeslot.equals(that.timeslot) &&
                Objects.equals(rank, that.rank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lecturer, timeslot, rank);
    }
}
