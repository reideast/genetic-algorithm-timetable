package net.andreweast.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "lecturers", schema = "public", catalog = "ga_dev")
public class LecturersEntity {
    private int lecturerId;
    private String name;
    private DepartmentsEntity departmentsByDepartmentId;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lecturer_generator")
    @SequenceGenerator(name="lecturer_generator", sequenceName = "lecturer_id_sequence")
    @Column(name = "lecturer_id", updatable = false, nullable = false)
    public int getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(int lecturerId) {
        this.lecturerId = lecturerId;
    }

    @Basic
    @Column(name = "name", nullable = true, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LecturersEntity that = (LecturersEntity) o;
        return lecturerId == that.lecturerId &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lecturerId, name);
    }

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    public DepartmentsEntity getDepartmentsByDepartmentId() {
        return departmentsByDepartmentId;
    }

    public void setDepartmentsByDepartmentId(DepartmentsEntity departmentsByDepartmentId) {
        this.departmentsByDepartmentId = departmentsByDepartmentId;
    }
}
