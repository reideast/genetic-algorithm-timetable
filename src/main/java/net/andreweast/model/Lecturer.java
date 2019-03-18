package net.andreweast.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "lecturers", schema = "public", catalog = "ga_dev")
public class Lecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lecturer_generator")
    @SequenceGenerator(name="lecturer_generator", sequenceName = "lecturer_id_sequence", allocationSize = 1)
    @Column(name = "lecturer_id", updatable = false, nullable = false)
    private Long lecturerId;

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    public Long getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(Long lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecturer that = (Lecturer) o;
        return lecturerId == that.lecturerId &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lecturerId, name);
    }
}
